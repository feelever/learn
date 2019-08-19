package cn.caigd.learn.crawler;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 使用方法，将需要上传的图片名称传入，然后在handle 方法中调用结果，
 * 具体处理流程为，登录页面获取到基本cookie，获取图片cookie及图片，上传七牛云，起异步线程根据cookie查询接口，50s connect time out，然后
 * 成功的扫码可以直接获取到cookie信息和token信息，在handle中执行业务代码
 */
@Service
@Slf4j
public class WechatService {
    private static final String DELIEVERY_URL = "http://a.weixin.qq.com/cgi-bin/agency/redirect_mp?appid=%s&amp;g_tk=%s&amp;mgr_type=1";
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.80 Safari/537.36";
    private static final String WECHAT_IMAGE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=wx5cb34fe58d130615&scope=snsapi_login&redirect_uri=https%3A%2F%2Fa.weixin.qq.com%2Findex.html&state=test&login_type=jssdk&self_redirect=default&href=https://wximg.qq.com/wxp/assets/css/agencyweb_login.css";
    private static final String WECHAT_IMAGE_CODE_URL = "https://long.open.weixin.qq.com/connect/l/qrconnect?uuid=%s";
    private static final String WECHAT_LOGIN_URL = "https://a.weixin.qq.com/cgi-bin/agency/login_auth?code=%s";
    private static final String WECHAT_CAPTCHA_BASE_URL = "https://open.weixin.qq.com%s";
    String accessKey = "pNf8Uh3lsdBw4BGIUE1tgWT8dPKDDyzuOGEgtEJi";
    String secretKey = "1IFNnCDTvWhPCr7gdE-nkODK5nV5_NdBJgXKSZ32";
    String bucket = "jdsp";
    final OkHttpClient client = new OkHttpClient().newBuilder()
            .followRedirects(false)  //禁制OkHttp的重定向操作，我们自己处理重定向
            .connectTimeout(30, TimeUnit.SECONDS) //连接超时
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .followSslRedirects(false)
            .build();

    public Map<String, String> getDeliveryCookies(String gToken, String appId, String cookies) {
        Map<String, String> resultMap = new HashMap<>();
        String url = String.format(DELIEVERY_URL, appId, gToken);
        log.info("getDeliveryCookies:token:{};wechatId:{};cookie:{};url:{}", gToken, appId, cookies, url);
        Request request = new Request.Builder().get()
                .addHeader("Cookie", cookies)
                .addHeader("User-Agent", USER_AGENT)
                .url(url)
                .build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (301 == response.code() || 302 == response.code()) {
                String location = response.header("Location");
                resultMap.put("cookie", "");
                resultMap.put("token", "");
                resultMap = redirectLocation(location, cookies, resultMap);
            }
        } catch (Exception e) {
            log.error("Error", e);
        } finally {
            if (response != null) {
                response.body().close();
                response.close();
            }
        }
        log.info("getDeliveryCookies result:{}", JSON.toJSONString(resultMap));
        return resultMap;
    }

    private Map<String, String> redirectLocation(String url, String cookies, Map<String, String> resultMap) {
        try {
            url = url.replaceAll("&amp;", "&");
            Request request = new Request.Builder().get()
                    .addHeader("Cookie", cookies)
                    .addHeader("Host", getHost(url))
                    .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
                    .addHeader("User-Agent", USER_AGENT)
                    .url(url)
                    .build();

            Response response = client.newCall(request).execute();
            if (301 == response.code() || 302 == response.code()) {
                List<String> cookiesNew = response.headers("Set-Cookie");
                for (String cook : cookiesNew) {
                    String s = cook.split("; ")[0];
                    String result = resultMap.get("cookie");
                    result = result + s + "; ";
                    resultMap.put("cookie", result);
                }

                String location = response.header("Location");
                resultMap = redirectLocation(location, cookies, resultMap);
            } else if (url != null) {
                String token = url.split("token=")[1];
                resultMap.put("token", token);
            }
        } catch (Exception e) {
            log.error("Error", e);
        }
        return resultMap;
    }


    public static String getHost(String url) {
        if (url == null || url.trim().equals("")) {
            return "";
        }
        String host = "";
        Pattern p = Pattern.compile("(?<=//|)((\\w)+\\.)+\\w+(:\\d*)?");
        Matcher matcher = p.matcher(url);
        if (matcher.find()) {
            host = matcher.group();
        }
        return host;
    }

    public String getCookie(Response response) {
        List<String> cookiesNew = response.headers("Set-Cookie");
        String result = "";
        for (String cook : cookiesNew) {
            String s = cook.split("; ")[0];
            result = result + s + "; ";
        }
        return result;
    }

    private Integer getToken(Response response) {
        List<String> cookiesNew = response.headers("Set-Cookie");
        log.info("cookies:{}",cookiesNew.toString());
        String result = "";
        try {
            for (String cook : cookiesNew) {
                if (cook.startsWith("MMAD_TICKET")) {
                    result = cook.split("MMAD_TICKET=")[1].split("; ")[0];
                }
            }
        } catch (Exception e) {
            log.error("解析token失败：{}", JSON.toJSONString(cookiesNew), e);
        }
        log.info("MMAD_TICKET:{}", result);
        return convertKey(result);
    }

    public static Integer convertKey(String key) {
        String value = "";
        int t = 5381;
        for (int i = 0; i < key.length(); i++) {
            int code = key.charAt(i);
            t += (t << 5) + code;
        }

        return t & 2147483647;
    }

    /**
     * 启动扫描微信;
     * @param requestSign
     * @return
     */
    public String waitForScanWechat(String requestSign) {
        String imageInfo = getImageAndUpload(requestSign);
        String url = String.format(WECHAT_IMAGE_CODE_URL, imageInfo);
        CompletableFuture.runAsync(() -> {
            Request request = new Request.Builder().get()
                    .addHeader("User-Agent", USER_AGENT)
                    .url(url)
                    .build();
            try {
                String string = client.newCall(request).execute().body().string();
                log.info("wait for scan ok:{}", string);
                TimeUnit.MILLISECONDS.sleep(1000);//必须sleep一下不然下面请求会不正常执行，估计是微信内部逻辑延迟；
                String resultString = client.newCall(request).execute().body().string();
                log.info("wait for reconnect ok:{}", resultString);
                String resultSplit = resultString.split("wx_code=")[1];
                String result = resultSplit.substring(1, resultSplit.length() - 2);
                String loginUrl = String.format(WECHAT_LOGIN_URL, result);
                log.info("wait for split ok:{}", loginUrl);
                Request request2 = new Request.Builder().get()
                        .addHeader("User-Agent", USER_AGENT)
                        .url(loginUrl)
                        .post(RequestBody.create(null, ""))
                        .build();
                Response response = client.newCall(request2).execute();
                log.info(response.body().string());
                String cookie = getCookie(response);
                Integer token = getToken(response);
                handle(cookie, token);
            } catch (Exception e) {
                log.error("获取code异常", e);
            }
        });
        return imageInfo;
    }

    /**
     * 调用实际处理类
     * @param cookie
     * @param token
     */
    public void handle(String cookie, Integer token) {
        System.out.println(cookie + "" + token);

    }

    public String getImageAndUpload(String requestSign) {
        String responseStr = "";
        try {
            Document html = Jsoup.connect(WECHAT_IMAGE_URL).get();
            Elements qrcode = html.getElementsByClass("qrcode");
            String captUrl = qrcode.get(0).attr("src");
            String captchaUrl = String.format(WECHAT_CAPTCHA_BASE_URL, captUrl);
            responseStr = captUrl.split("qrcode/")[1];
            log.info("image url:{}", captchaUrl);
            Request request = new Request.Builder().get()
                    .addHeader("User-Agent", USER_AGENT)
                    .url(captchaUrl)
                    .build();
            Response imageResponse = client.newCall(request).execute();
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
        return responseStr;
    }

    public static void main(String[] args) throws Exception {
        WechatService wechatService = new WechatService();
        Integer integer = WechatService.convertKey("AoL3XfYMQHf8BRKT6I39LFHGryZxV1RObOqllleN2fnqCrdkwxY8IBvJ86MG5CYyYtlvzejpzBc=");
        System.out.println(integer);
    }
}
