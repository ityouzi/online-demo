package com.example.online.demo.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.online.demo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**快捷协议签约
 * @author yangx
 * @description TODO
 * @date 2021/4/6
 */
@Slf4j
public class FastSignController {

    /** 快捷协议签约接口
     *  请看懂demo,代码没有问题，生产联调，同步请求的签名请使用自己申请的私钥证书，同时必须在银盛门户上传自己的公钥证书
     *  同步响应以及异步通知的验签请使用银盛下发的银盛公钥证书
     *  如果碰到签名验签问题，不要慌，严格按照demo来，并且确认证书没有问题之后，问题都会得到解决，如果没有解决就再仔细看一下demo
     *  如果不清楚公私钥证书体系，又不知道怎么替换证书，请百度公私钥证书
     * @author yangx
     * @date 18:14  2020/9/10
     **/
    public static void main(String[] args) throws Exception {
        //1、组装报文
        Map<String, String> mapData = new HashMap<String, String>();

        //1.1 组装报文-外层报文也就是api文档中的请求参数
        //特别注意 商户在生产环境联调测试，请替换成自己在银盛正式环境申请的商户号（partner_id）
        mapData.put("method", "ysepay.trusteeship.sign");
        mapData.put("partner_id", "cws123456");
        mapData.put("timestamp", DateUtil.getDateNow());
        mapData.put("sign_type", "RSA");
        mapData.put("charset", "utf-8");
        mapData.put("version", "3.0");

        //1.2 组装报文-内层报文也就是api文档中的业务参数
        JSONObject json = new JSONObject();
        json.put("out_trade_no", "20180919659842");
        json.put("seller_id", "cws123456");
        json.put("seller_name", "杭州杭榕企业发展有限公司");
        json.put("user_id", "1879217555011");
//        json.put("imei", "18797813533");

        try {
            json.put("buyer_name", SrcDesUtil.encryptData("cws123456", "叶鹏"));
            json.put("buyer_card_number",  SrcDesUtil.encryptData("cws123456", "6230580000110362295"));
            //json.put("", SrcDesUtil.decryptExtraData("anguangtingche", "yRrsyL7eujs="));
            json.put("buyer_mobile", SrcDesUtil.encryptData("cws123456", "18792175550"));
            json.put("pyerIDNo",SrcDesUtil.encryptData("cws123456", "342921199612262610"));
        } catch (Exception e1) {
            System.out.println("加密是异常");

        }

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
        String result = Https.httpsSend("https://trusteeship.ysepay.com/gateway.do", mapData);
        log.info("同步返回结果:{}",result);

        if (StringUtils.isBlank(result)) {
            log.error("同步返回结果为空");
        }

        boolean resultVerify = false;
        try {
            //执行验签， repMethod 各个接口的值均不同，可以从返回结果中查看
            resultVerify = YsPaySignUtils.resultVerify(result, "ysepay_trusteeship_sign_response");
            log.info("同步请求验签结果:{}",resultVerify);
        } catch (Exception e) {
            log.error("同步请求验签异常");
        }
    }
}
