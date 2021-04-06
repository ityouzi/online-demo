package com.example.online.demo.controller;


import com.alibaba.fastjson.JSONObject;
import com.example.online.demo.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**协议签约
 * @author yangx
 * @description TODO
 * @date 2020/9/9
 */
@RestController
@Slf4j
public class YSSignAgreeController {

    /**
     * //TODO
     * @author yangx
     * @date 18:14  2020/9/10
     **/
    public static String signAgreeController(Map<String,String> param) throws Exception {
        //1、组装报文
        Map<String, String> mapData = new HashMap<String, String>();

        //1.1 组装报文
        mapData.put("method", "ysepay.trusteeship.sign");
        mapData.put("partner_id", "cws123456");//本demo的商户号 请勿修改
        mapData.put("timestamp", DateUtil.getDateNow());
        mapData.put("charset", "utf-8");
        mapData.put("sign_type", "RSA");
        mapData.put("version", "3.0");

        JSONObject json = new JSONObject();
        //商户生成的订单号，生成规则前 8 位，必须为交易日期，如 20180525，范围跨度支持包含当天在内的前后一天，且只能由大小写英文字母、数字、下划线及横杠组成
        json.put("out_trade_no", param.get("out_trade_no"));
        json.put("seller_id", "cws123456");  //收款方银盛支付用户号
        json.put("seller_name", "杭州杭榕企业发展有限公司");    //收款方银盛支付客户名


        json.put("buyer_name", SrcDesUtil.encryptData("cws123456", param.get("buyer_name"))); //付款方银行姓名，使用 DES 加密，密钥为商户号前 8 位，不足 8 位在商户号前补空格
       /* String data=SrcDesUtil.encryptData("cws123456",param.get("buyer_card_number"));
        data=data.replaceAll("\\+","%2B");
        json.put("buyer_card_number",data);  //付款方银行账号，使用 DES 加密，密钥为商户号前 8 位，不足 8 位在商户号前补空格*/
        json.put("buyer_card_number",SrcDesUtil.encryptData("cws123456",param.get("buyer_card_number")));  //付款方银行账号，使用 DES 加密，密钥为商户号前 8 位，不足 8 位在商户号前补空格
        json.put("buyer_mobile", SrcDesUtil.encryptData("cws123456",param.get("buyer_mobile")));   //付款方银行绑定手机号码，使用 DES加密，密钥为商户号前 8 位，不足 8位在商户号前补空格
        json.put("pyerIDNo", SrcDesUtil.encryptData("cws123456",param.get("pyerIDNo")));   //证件号码，目前只支持身份证，使用DES 加密，密钥为商户号前 8 位，不足 8 位在商户号前补空格
        json.put("user_id", param.get("user_id"));    //唯一客户标识，商户旗下客户号

//        json.put("total_amount", "0.01");   //该 笔 订 单 的 资 金 总 额 ， 单 位 为RMB-Yuan 。 取 值 范 围 为 [0.01 ，100000000.00]，精确到小数点后两位。Number(10,2)指 10 位长度，2位精度

        mapData.put("biz_content",  json.toString());

        //1.3 组装报文-对除sign之外的外层报文参数进行签名,签名之后将sign组装到报文
        try {
            System.out.println("待签名内容map:" + mapData.toString());
            String sign = YsPaySignUtils.sign(mapData);
            mapData.put("sign",  sign);
            //mapData.put("sign",  URLEncoder.encode(sign,"utf-8"));
            //mapData.put("biz_content",  URLEncoder.encode(mapData.get("biz_content"),"utf-8"));
            System.out.println("签名完成内容：" + mapData.toString());
        } catch (Exception e) {
            System.out.println("签名异常，异常信息{" + e);
        }
        //1.4 组装报文完毕，发送请求到银盛网关
        String result = HttpRequest.sendPost("https://trusteeship.ysepay.com/gateway.do", CommonUtil.mapToString(mapData));
        //String result = Https.httpsSend("https://trusteeship.ysepay.com/gateway.do",mapData);
//        String result = Https.httpsSend("https://trusteeship.ysepay.com/gateway.do", mapData);
        System.out.println("同步返回结果:" + result);
        return result;
    }


    //ysepay_trusteeship_sign_response":{"code":"10000","msg":"Success","out_trade_no":"20210327147583","trade_status":"WAIT_CONFIRM"}}
    //18792175550
    public static void main(String[] args) throws Exception {

        Map<String,String> param = new HashMap<>();

        param.put("out_trade_no",DateUtil.getDateNowYmd() + DateUtil.getRandom(6));

       /* param.put("buyer_name","叶鹏");
        param.put("buyer_card_number","6230580000110362295");
        param.put("buyer_mobile","18792175550");
        param.put("pyerIDNo","342921199612262610");
        param.put("user_id","1879217555011");*/
        param.put("buyer_name","钟久亮");
        //6217906500020724322
        param.put("buyer_card_number","6217906500020724322");
        param.put("buyer_mobile","18797813533");
        param.put("pyerIDNo","360781199706100011");
        param.put("user_id","1118797813533");

        String result = signAgreeController(param);
    }

}
