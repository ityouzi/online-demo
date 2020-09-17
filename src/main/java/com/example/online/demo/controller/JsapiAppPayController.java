package com.example.online.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.online.demo.common.ResultCodeBean;
import com.example.online.demo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**微信公众号、小程序支付测试demo
 * 该demo 解决同步请求的签名验签 以及参数组装的问题，其他接口类似，请严格按照demo步骤
 * @author yangx
 * @description TODO
 * @date 2020/9/9
 */
@RestController
@Slf4j
public class JsapiAppPayController {


    /**微信公众号 小程序接口
     * //TODO
     * @author yangx
     * @date 18:14  2020/9/10
     **/
    public static void main(String[] args) {
        //1、组装报文
        Map<String, String> mapData = new HashMap<String, String>();

        //1.1 组装报文-外层报文也就是api文档中的请求参数
        mapData.put("partner_id", "shanghu_test");//本demo的商户号 请勿修改
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
        json.put("seller_id", "shanghu_test");
        json.put("seller_name", "银盛支付商户测试公司");
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
            String sign = YsPaySignUtils.sign(mapData);
            mapData.put("sign", sign);
        } catch (Exception e) {
            log.error("签名异常，异常信息{}",e);
        }

        //1.4 组装报文完毕，发送请求到银盛网关  地址如果访问不通，请联系银盛技术
        String result = HttpRequest.sendPost("http://localhost:8085/openapi_gateway/gateway.do", CommonUtil.mapToString(mapData));
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
