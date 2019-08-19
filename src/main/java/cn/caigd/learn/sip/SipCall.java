package cn.caigd.learn.sip;

import javax.sip.SipFactory;
import javax.sip.SipStack;
import java.util.Properties;

public class SipCall {
    public static void main(String[] args) throws Exception {
        SipFactory sipFactory = SipFactory.getInstance();
        Properties properties = new Properties();
        properties.setProperty("javax.sip.STACK_NAME", "7030");
        SipStack sipStack = sipFactory.createSipStack(properties);
    }
}
