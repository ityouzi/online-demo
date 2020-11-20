package com.example.online.demo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author yangx
 * @description TODO
 * @date 2020/9/10
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignVerifyResponse implements Serializable {

    private static final long serialVersionUID = 4718062183728761461L;

    /**请求信息**/
    private String content;

    /**带签名内容**/
    private String signContent;


    /**签名**/
    private String sign;

    /**同步响应信息**/
    private String resResult;


    /**验签结果*/
    private boolean verifyResult;
}
