package com.example.online.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online.demo.utils.CommonUtil;
import com.example.online.demo.utils.DateUtil;
import com.example.online.demo.utils.HttpRequest;
import com.example.online.demo.utils.YsPaySignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**微信公众号、小程序支付测试demo
 * 该demo 解决同步请求的签名验签 以及参数组装的问题，其他接口类似(除云商服进件以及PC网站支付即时到账页面接入)，
 * 请严格按照demo步骤
 * @author yangx
 * @description TODO
 * @date 2020/9/9
 */
@RestController
@Slf4j
public class JsapiAppPayController {


    /**微信公众号 小程序接口
     *  请看懂demo,代码没有问题，生产联调，同步请求的签名请使用自己申请的私钥证书，同时必须在银盛门户上传自己的公钥证书
     *  同步响应以及异步通知的验签请使用银盛下发的银盛公钥证书
     *  如果碰到签名验签问题，不要慌，严格按照demo来，并且确认证书没有问题之后，问题都会得到解决，如果没有解决就再仔细看一下demo
     *  如果不清楚公私钥证书体系，又不知道怎么替换证书，请百度公私钥证书
     * @author yangx
     * @date 18:14  2020/9/10
     **/
    public static void main(String[] args) {
        //1、组装报文
        Map<String, String> mapData = new HashMap<String, String>();

        //1.1 组装报文-外层报文也就是api文档中的请求参数
        //特别注意 商户在生产环境联调测试，请替换成自己在银盛正式环境申请的商户号（partner_id）
        mapData.put("partner_id", "test");
        mapData.put("timestamp", DateUtil.getDateNow());
        mapData.put("charset", "utf-8");
        mapData.put("sign_type", "RSA");
        mapData.put("notify_url", "http://127.0.0.1");
        mapData.put("version", "3.0");
        mapData.put("method", "ysepay.online.weixin.pay");

        //1.2 组装报文-内层报文也就是api文档中的业务参数
        JSONObject json = new JSONObject();
        json.put("out_trade_no", "123456789");
        json.put("subject", "公众号");
        json.put("total_amount", "2.99");
        json.put("currency", "CNY");
        json.put("seller_id", "hyfz_test2");
        json.put("seller_name", "银盛支付服务股份有限公司行业发展部");
        json.put("timeout_express","96h");
        json.put("business_code", "01000010");
        json.put("is_minipg", "1");
        json.put("appid", "wxf71930fbcc125f64");
        json.put("sub_openid","oc0KJ5Kcr5lhbKgmkqXPENrGzsdw");

        mapData.put("biz_content", json.toString());

        List<JSONObject> list1=new ArrayList<JSONObject>();

        //1.3 组装报文-对除sign之外的外层报文参数进行签名,签名之后将sign组装到外层报文
        try {
            log.info("待签名内容map:{}",mapData.toString());
            //特别注意 商户在生产环境联调测试做签名，请替换成自己操作下载的私钥证书
            String sign = YsPaySignUtils.sign(mapData);
            mapData.put("sign", sign);
        } catch (Exception e) {
            log.error("签名异常，异常信息{}",e);
        }
        log.info("签名之后的报文：{}", CommonUtil.mapToString(mapData));
        //1.4 组装报文完毕，发送请求到银盛网关
        //目前银盛网关没有测试环境可以联调，联调需要访问银盛网关生产环境，生产地址  https://qrcode.ysepay.com/gateway.do
        String result = HttpRequest.sendPost("https://qrcode.ysepay.com/gateway.do", CommonUtil.mapToString(mapData));
        log.info("同步返回结果:{}",result);

        if (StringUtils.isBlank(result)) {
            log.error("同步返回结果为空");
        }

        boolean resultVerify = false;
        try {
            //执行验签， repMethod 各个接口的值均不同，可以从返回结果中查看
            resultVerify = YsPaySignUtils.resultVerify(result, "ysepay_online_weixin_pay_response");
            log.info("同步请求验签结果:{}",resultVerify);
        } catch (Exception e) {
            log.error("同步请求验签异常");
        }
    }




}
