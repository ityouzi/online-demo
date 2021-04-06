package com.example.online.demo.controller;

import com.alibaba.fastjson.JSON;
import com.example.online.demo.common.ResultCodeBean;
import com.example.online.demo.common.ReturnCode;
import com.example.online.demo.response.SignVerifyResponse;
import com.example.online.demo.utils.YsPaySignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
@RestController
public class SignVerifyController {

    /**
     * //异步验签demo1  ,两种均可，只是接收参数的处理方式不一样
     *   请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     *   请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     *    请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     * @author yangx
     * @date 16:51  2020/9/10
     **/
    @RequestMapping(value = "applyAsynVerify", method = RequestMethod.POST)
    public ResultCodeBean<?> applyAsynVerify(HttpServletRequest request){
        Map<String, String> data = parseRequest(request);
        log.info("请求参数：{}", JSON.toJSONString(data));
        boolean verifyResult=false;
        try {
            verifyResult = YsPaySignUtils.asynVerifyYs(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SignVerifyResponse signVerifyResponse=SignVerifyResponse.builder().verifyResult(verifyResult).build();
        return new ResultCodeBean(ReturnCode.success,"操作成功",signVerifyResponse);
    }

    /**
     * //异步验签demo2  ，两种均可，只是接收参数的处理方式不一样
     *  请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     *  请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     *  请看懂demo,代码没有问题，如果碰到签名验签问题，请仔细核对是否有替换成自己的证书，是否有严格按照demo代码来
     * @author yangx
     * @date 16:51  2020/9/10
     **/
    @RequestMapping(value = "applyAsynVerify1", method = RequestMethod.POST)
    public ResultCodeBean<?> applyAsynVerify1(@RequestParam Map<String,String> data, HttpServletRequest request){
        log.info("请求参数：{}", JSON.toJSONString(data));
        boolean verifyResult=false;
        try {
            //特别注意 商户在生产环境联调做异步通知验签，请替换成银盛下发的银盛公钥证书
            verifyResult = YsPaySignUtils.asynVerifyYs(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SignVerifyResponse signVerifyResponse=SignVerifyResponse.builder().verifyResult(verifyResult).build();
        return new ResultCodeBean(ReturnCode.success,"操作成功",signVerifyResponse);
    }


    protected Map<String, String> parseRequest(HttpServletRequest request) {
        Map<String, String> map = new HashMap<String, String>();
        Enumeration<String> paramNames = request.getParameterNames();//获取所有的参数名
        while (paramNames.hasMoreElements()) {
            String name = paramNames.nextElement();//得到参数名
            String value = request.getParameter(name);//通过参数名获取对应的值
            System.out.println(MessageFormat.format("{0}={1}", name, value));
            map.put(name, value);
        }

        return map;
    }


    public static void main(String[] args) {
         String urlRegex = "^((([hH][tT][tT][pP][sS]?|[fF][tT][pP])\\:\\/\\/)?([\\w\\.\\-]+(\\:[\\w\\.\\&%\\$\\-]+)*@)?((([^\\s\\(\\)\\<\\>\\\"\\.\\[\\]\\,@;:]+)(\\.[^\\s\\(\\)\\<\\>\\\"\\.\\[\\]\\,@;:]+)*(\\.[a-zA-Z]{2,4}))|((([01]?\\d{1,2}|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d{1,2}|2[0-4]\\d|25[0-5])))(\\b\\:(6553[0-5]|655[0-2]\\d|65[0-4]\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}|0)\\b)?((\\/[^\\/][\\w\\.\\,\\?\\'\\\\/\\+&%\\$#\\=~_\\-@]*)*[^\\.\\,\\?\"\\'\\(\\)\\[\\]!;<>{}\\s\\x7F-\\xFF])?)$";
        String url="https://ys.shenzhoufu.online/ysepay/asynReceiveReturn";
        boolean flag= StringUtils.trim(url).matches(urlRegex);
        System.out.println(flag);
    }







}
