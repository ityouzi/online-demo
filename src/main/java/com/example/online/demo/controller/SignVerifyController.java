package com.example.online.demo.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.online.demo.utils.YsPaySignUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**异步验签demo
 * @author yangx
 * @description TODO
 * @date 2020/9/9
 */
@Slf4j
public class SignVerifyController {


    /**
     * //异步验签 ，异步通知到商户，商户对异步通知内容进行验签
     *
     * @author yangx
     * @date 16:51  2020/9/10
     **/

    public static void main(String[] args) throws Exception {
        //模拟银盛端得签名 --start
        //异步通知得到内容
        Map<String, String> param = new HashMap<String, String>();
        param.put("settlement_amount","0.01");
        param.put("openid","ofrbF5KScc9PkL5_bs9qcbUtc-xk");
        param.put("account_date","20200602");
        param.put("trade_no","01O200602084006871");
        param.put("notify_type","directpay.status.sync");
        param.put("total_amount","0.01");
        param.put("out_trade_no","202006021591062782686-7046");
        param.put("channel_recv_sn","4200000542202006020751161803");
        param.put("notify_time","2020-06-02 09:53:33");
        param.put("channel_recv_sn","4200000542202006020751161803");
        param.put("trade_status","TRADE_SUCCESS");
        param.put("payer_fee","0.00");
        param.put("fee","0.00");
        param.put("card_type","debit");
        param.put("partner_fee","0.00");
        param.put("paygate_no","9000010");
        param.put("sign_type","RSA");
        param.put("channel_send_sn","1012006020840068711");

        try {
            log.info("待签名内容map:{}",param.toString());
            String sign = YsPaySignUtils.asynSign(param);
            param.put("sign", sign);
        } catch (Exception e) {
            log.error("签名异常，异常信息{}",e);
        }
        //模拟银盛端得签名 --end
        log.info(param.toString());
        //商户端验签，重点看这里
        boolean verifyResult = YsPaySignUtils.asynVerifyYs(param);
        log.info("异步验签结果：{}",verifyResult);
    }





}
