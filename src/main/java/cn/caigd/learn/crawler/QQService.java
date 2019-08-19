package cn.caigd.learn.crawler;

import com.google.common.collect.Maps;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * 使用方法，将需要上传的图片名称传入，然后在handle 方法中调用结果，
 * 具体处理流程为，登录页面获取到基本cookie，获取图片cookie及图片，上传七牛云，起异步线程根据cookie轮询接口，30次轮询，1秒之间，然后
 * 成功的扫码可以直接获取到cookie信息和token信息，在handle中执行业务代码
 * 入口方法：waitForScanQQ("qq.png");
 */
@Service
@Slf4j
public class QQService {
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36";
    private static final String CAPTCHA_PATH = "https://ssl.ptlogin2.qq.com/ptqrshow?appid=15000103&e=2&l=M&s=3&d=72&v=4&t=0.278903546136678&pt_3rd_aid=0";
    private static final String BASE_URL = "https://xui.ptlogin2.qq.com/cgi-bin/xlogin?appid=15000103&s_url=https://e.qq.com";
    String accessKey = "";
    String secretKey = "";
    String bucket = "";
    final OkHttpClient client = new OkHttpClient().newBuilder()
            // .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            .connectTimeout(30, TimeUnit.SECONDS) //连接超时
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followSslRedirects(false)
            .build();

    /**
     * 通过cookie里的skey获取到gtoken
     * @param key cookie中的skey
     * @return
     */
    public static Integer convertKey(String key) {
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        int t = 5381;
        for (int i = 0; i < key.length(); i++) {
            int code = key.charAt(i);
            t += (t << 5) + code;
        }

        return t & 2147483647;
    }
    /**
     * 调用的入口方法：参数为图片名称，可以拓展参数方便异步回调的时候匹配数据
     */
    public String waitForScanQQ(String imagePath) {
        Request request = new Request.Builder().get()
                .addHeader("User-Agent", USER_AGENT)
                .url(BASE_URL)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
        } catch (IOException e) {
            log.error("调用处理失败", e);
        }
        Map<String, String> cookie = getCookieMap(response);
        log.info("base cookie:{}",cookie);
        Map<String, String> cookie2 = getImageAndUpload(imagePath);
        for (Map.Entry<String, String> entry : cookie2.entrySet()) {
            cookie.put(entry.getKey(), entry.getValue());
        }
        log.info("base cookieImage:{}",cookie);
        String queryUrl = initQueryParam(cookie);
        log.info("base async roll call url:{}",queryUrl);
        log.info("base async roll call cookie:{}",cookie);
        CompletableFuture.runAsync(() -> {
            for (int i = 0; i < 30; i++) {
                try {
                    Request request2 = new Request.Builder().get()
                            .addHeader("User-Agent", USER_AGENT)
                            .addHeader("cookie", cookieMap2Str(cookie))
                            .url(queryUrl)
                            .build();
                    Response responseCur = client.newCall(request2).execute();
                    String bodyStr = responseCur.body().string();
                    if (bodyStr.startsWith("ptuiCB('0'")) {
                        log.info("login result body:{}",bodyStr);
                        Map<String, String> cookieMap = getCookieMap(responseCur);
                        log.info("login result cookie:{}",cookieMap);
                        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
                            cookie.put(entry.getKey(), entry.getValue());
                        }
                        String skey = cookie.get("skey");
                        Integer token = convertKey(skey);
                        String cookieStr = cookieMap2Str(cookieMap);
                        handle(cookieStr, token);
                        break;
                    }
                    TimeUnit.SECONDS.sleep(1);
                } catch (Exception e) {
                    log.error("sleep failed", e);
                }
            }
        });
        return imagePath;
    }

    private String getUrlFromResponseBodyStr(String str) {
        String[] split = str.split(",'");
        if (split.length < 2) {
            return "";
        }
        if (split[2].length() <= 0) {
            return "";
        }
        return split[2].substring(0, split[2].length() - 1);
    }

    /**
     * 异步回调业务方法的入口，可以拓展入参，下面的是一个异步测试接口
     * @param cookieStr: header里面的cookie
     * @param token:gtoken
     * @throws Exception
     */
    public void handle(String cookieStr, Integer token) throws Exception{
        Request request = new Request.Builder().get()
                .addHeader("User-Agent", USER_AGENT)
                .addHeader("cookie",cookieStr)
                .url("https://e.qq.com/gaea/api.php?mod=message&act=getlist&status=1&cateid=0&page=1&pagesize=999&g_tk="+token)
                .build();
        System.out.println(client.newCall(request).execute().body().string());
    }

    /**
     * 根据cookie 初始化轮询接口的路径
     * @param cookieMap
     * @return
     */
    private String initQueryParam(Map<String, String> cookieMap) {
        String queryUrl = "https://ssl.ptlogin2.qq.com/ptqrlogin?u1=https%3A%2F%2Fgraph.qq.com%2Foauth2.0%2Flogin_jump&";
        StringBuffer sb = new StringBuffer();
        sb.append(queryUrl);
        sb.append("h=1").append("&t=1").append("&g=1").append("&from_ui=1").append("&ptlang=2052").append("&ptredirect=0");//通用的参数
        sb.append("&js_type=1").append("&pt_uistyle=40").append("&action=0-0-").append(new Date().getTime()).append("&js_ver=10291");//一些js，ui加载参数，需要注意最后一个js_ver
        sb.append("&authid=0").append("&pt_3rd_aid=101477621").append("&daid=").append("&aid=15000103");//一些访问路径相关的授权参数 与BASE_URL有关
        sb.append("&login_sig=").append("&ptqrtoken=").append(hash33(cookieMap.get("qrsig")));
        return sb.toString();
    }
    /**
     * 解析出ptqrtoken的hash算法，具体为cookie["qrsig"]-hash33->ptqrtoken
     */
    public Integer hash33(String key) {
        log.info(key);
        int e = 0;
        for (int i = 0; i < key.length(); i++) {
            e += (e << 5) + Character.codePointAt(key, i);
        }
        return 2147483647 & e;
    }
    /**
     * cookiemap 专换header里面的cookie string
     */
    public String cookieMap2Str(Map<String, String> cookieMap) {
        String str = "";
        for (Map.Entry<String, String> entry : cookieMap.entrySet()) {
            str += entry.getKey() + "=" + entry.getValue() + "; ";
        }
        return str;
    }
    /**
     * 通过header里面的set-cookie 解析出cookiemap
     */
    public Map<String, String> getCookieMap(Response response) {
        List<String> cookiesNew = response.headers("set-cookie");
        Map<String, String> resultMap = Maps.newHashMap();
        for (String cook : cookiesNew) {
            log.info(cook);
            String s = cook.split("; ")[0];
            if (s.contains("Path")) {
                s = s.split(";Path")[0];
            }
            String[] split = s.split("=");
            if (split != null && split.length > 0) {
                if (split.length == 1) {
                    resultMap.put(split[0], "");
                } else {
                    resultMap.put(split[0], split[1]);
                }

            }
        }
        return resultMap;
    }
    /**
     * 获取登录扫码图片和其对应的cookie，并上传七牛云
     */
    public Map<String, String> getImageAndUpload(String requestSign) {
        Map<String, String> qrsigMap = Maps.newHashMap();
        try {
            Request request = new Request.Builder().get()
                    .addHeader("User-Agent", USER_AGENT)
                    .url(CAPTCHA_PATH)
                    .build();
            Response imageResponse = client.newCall(request).execute();
            qrsigMap = getCookieMap(imageResponse);
            InputStream inputStream = imageResponse.body().byteStream();
            Configuration cfg = new Configuration(Zone.zone0());
            UploadManager uploadManager = new UploadManager(cfg);
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                uploadManager.put(inputStream, requestSign, upToken, null, null);
            } catch (QiniuException ex) {
                log.error("上传七牛失败");
            }
        } catch (IOException ex) {
            log.error("抓取微信图片失败", ex);
        }
        return qrsigMap;
    }

    public static void main(String[] args) throws Exception {
        QQService wechatService = new QQService();
        System.out.println(wechatService.waitForScanQQ("qq.png"));
//        System.out.println(wechatService.waitForScanQQ("high-qq.png"));
        TimeUnit.SECONDS.sleep(100);
    }
}
