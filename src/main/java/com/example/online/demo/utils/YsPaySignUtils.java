package com.example.online.demo.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import java.io.InputStream;
import java.net.URLEncoder;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Enumeration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class YsPaySignUtils {

	// 缓存公钥和私钥
	public static Map<String, Object> certMap = new ConcurrentHashMap<String, Object>();

	/**
	 * 读取PKCS12格式的key（私钥）pfx格式
	 * 
	 * @param password
	 * @param ins
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static PrivateKey getPrivateKeyFromPKCS12(String password, InputStream ins) throws Exception {

		PrivateKey priKey = null;
		KeyStore keystoreCA = KeyStore.getInstance("PKCS12");
		try {
			// 读取CA根证书
			keystoreCA.load(ins, password.toCharArray());

			Enumeration<?> aliases = keystoreCA.aliases();
			String keyAlias = null;
			if (aliases != null) {
				while (aliases.hasMoreElements()) {
					keyAlias = (String) aliases.nextElement();
					// 获取CA私钥
					priKey = (PrivateKey) (keystoreCA.getKey(keyAlias, password.toCharArray()));
					if (priKey != null) {
						break;
					}
				}
			}
		} catch (Exception e) {
			if (ins != null){
				ins.close();
			}
			throw e;
		} finally {
			if (ins != null) {
				ins.close();
			}
		}

		return priKey;
	}

	/**
	 * 读取公钥，x509格式
	 * 
	 * @param ins
	 * @return
	 * @throws Exception
	 * @see
	 */
	public static PublicKey getPublicKeyFromCert(InputStream ins) throws Exception {

		PublicKey pubKey = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Certificate cac = cf.generateCertificate(ins);
			pubKey = cac.getPublicKey();
		} catch (Exception e) {
			if (ins != null){
				ins.close();
			}
			throw e;
		} finally {
			if (ins != null) {
				ins.close();
			}
		}

		return pubKey;
	}

	/**
	 * 获取缓存中公钥
	 * 
	 * @return
	 * @throws Exception
	 */
	private static PublicKey getYsPayPublicKey() throws Exception {

		PublicKey publicKey = (PublicKey) certMap.get("YsPayPublicKey");
		if (publicKey == null) {

			InputStream publicCertFileInputStream = YsPaySignUtils.class.getResourceAsStream("/cert/shanghu_test.cer");
			publicKey = getPublicKeyFromCert(publicCertFileInputStream);
			certMap.put("YsPayPublicKey", publicKey);

			if (publicCertFileInputStream != null) {

				publicCertFileInputStream.close();
			}
		}

		return publicKey;
	}

	/**
	 * 同步请求参数签名
	 */
	public static String sign(Map<String, String> mapData) throws Exception {
		//商户是根据商户号来获取自己的公私钥证书，本例子中的商户号为shanghu_test，公私钥证书的名称也是shanghu_test，
		//所以请勿修改商户号，否则会取不到证书
		//1.3.1 根据商户号从内存中获取私钥证书
        String partnerId=mapData.get("partner_id");
		PrivateKey privateKey = (PrivateKey) certMap.get( partnerId);
		//1.3.2 如果内存中不存在，则重新加载获取
		if (privateKey == null) {
            String path="/cert/"+partnerId+".pfx";
			InputStream publicpfxFileInputStream = YsPaySignUtils.class.getResourceAsStream(path);
			privateKey = getPrivateKeyFromPKCS12("123456", publicpfxFileInputStream);
			certMap.put(partnerId, privateKey);
			if (publicpfxFileInputStream != null) {
				publicpfxFileInputStream.close();
			}
		}
		//1.3.3  将所有外层报文参数（sign除外）进行字典排序，组成字符串得到待签名内容
		String signContent = CommonUtil.getSignContent(mapData);
		log.info("待签名内容：{}",signContent);
		//1.3.4 执行签名
		String sign=rsaSign(privateKey,signContent,"utf-8");
		//1.3.5 对sign进行url编码
		return URLEncoder.encode(sign,"utf-8");
	}


	/**
	 * 异步通知参数签名
	 */
	public static String asynSign(Map<String, String> mapData) throws Exception {
		PrivateKey privateKey = (PrivateKey) certMap.get( "shanghu_test");
		//1.3.2 如果内存中不存在，则重新加载获取
		if (privateKey == null) {
			String path="/cert/shanghu_test.pfx";
			InputStream publicpfxFileInputStream = YsPaySignUtils.class.getResourceAsStream(path);
			privateKey = getPrivateKeyFromPKCS12("123456", publicpfxFileInputStream);
			certMap.put("shanghu_test", privateKey);
			if (publicpfxFileInputStream != null) {
				publicpfxFileInputStream.close();
			}
		}
		//1.3.3  将所有外层报文参数（sign除外）进行字典排序，组成字符串得到待签名内容
		String signContent = CommonUtil.getSignContent(mapData);
		log.info("待签名内容：{}",signContent);
		//1.3.4 执行签名
		String sign=rsaSign(privateKey,signContent,"utf-8");
		//1.3.5 对sign进行url编码
		return sign;
	}

	//使用RSA的加密算法对以上字符串进行签名并进行Base64编码
	public static String rsaSign(PrivateKey privateKey, String content, String charset) throws Exception {
			java.security.Signature signet = java.security.Signature.getInstance("SHA1WithRSA");
			signet.initSign(privateKey);
			signet.update(content.getBytes(charset));
			byte[] signed = signet.sign();
			return new String(Base64.encodeBase64(signed),charset);
	}

	/**
	 * 同步请求参数签名
	 */
	public static String sign(String readySignContent) throws Exception {

		PrivateKey privateKey = (PrivateKey) certMap.get("shanghu_test");
		if (privateKey == null) {
			InputStream publicpfxFileInputStream = YsPaySignUtils.class.getResourceAsStream("/cert/shanghu_test.pfx");
			privateKey = getPrivateKeyFromPKCS12("123456", publicpfxFileInputStream);
			certMap.put("shanghu_test", privateKey);

			if (publicpfxFileInputStream != null) {

				publicpfxFileInputStream.close();
			}
		}

		Signature signature = Signature.getInstance("SHA1WithRSA");
		signature.initSign(privateKey);

		if (StringUtils.isEmpty("UTF-8")) {
			signature.update(readySignContent.getBytes());
		} else {
			signature.update(readySignContent.getBytes("UTF-8"));
		}

		byte[] signed = signature.sign();

		String sign = new String(Base64.encodeBase64(signed), "UTF-8");

		return URLEncoder.encode(sign);
		//return sign;
	}

	/**
	 * 同步返回参数验签
	 *
	 * @throws Exception
	 */
	/*public static boolean resultVerify(String resTest, String repMethod) throws Exception {

		boolean flag = false;

		// String methodStr = ReUtil.get("ysepay(.*?)response", responseJsonStr,
		// 0);

		JsonParser parser = new JsonParser();
		JsonObject jsonObject = parser.parse(resTest).getAsJsonObject();
		System.out.println("jsonObject:" + jsonObject);
		String content = jsonObject.get(repMethod).toString();
		String sign = jsonObject.get("sign").toString();
		System.out.println("sign:" + sign+"content:" + content);
		PublicKey ysPayPublicKey = getYsPayPublicKey();

		Signature signetcheck = Signature.getInstance("SHA1WithRSA");
		signetcheck.initVerify(ysPayPublicKey);
		signetcheck.update(content.getBytes("UTF-8"));
		if (signetcheck.verify(Base64.decodeBase64(sign.getBytes("UTF-8")))) {
			flag = true;
		}

		return flag;
	}*/

	public static boolean rsaCheckContent(PublicKey pubKey,
										  String content, String sign, String charset) throws Exception {
		System.out.println("进入验证签名方法: content[" + content + "], sign[" + sign
				+ "], charset[" + charset + "]");
		boolean bFlag = false;
		try {
			java.security.Signature signetcheck = java.security.Signature
					.getInstance("SHA1WithRSA");
			signetcheck.initVerify(pubKey);
			signetcheck.update(content.getBytes(charset));
			if (signetcheck
					.verify(Base64.decodeBase64(sign.getBytes(charset)))) {
				bFlag = true;
			}

			System.out.println("进入验证签名方法: content[" + content + "], sign["
					+ sign + "], charset[" + charset + "], result[" + bFlag
					+ "]");
		} catch (Exception e) {
			System.out.println("验证签名异常" + ": content[" + content + "], sign["
					+ sign + "], charset[" + charset + "]");
			throw new Exception("验证签名异常");
		}

		return bFlag;
	}
	/**
	 * 同步返回参数验签
	 *
	 * @throws Exception
	 */
	public static boolean resultVerify(String resTest, String repMethod) throws Exception {

		boolean flag = false;
		JSONObject jsonObject= JSON.parseObject(resTest, Feature.OrderedField);
		System.out.println("jsonObject:" + jsonObject);
		String content = jsonObject.get(repMethod).toString();
		String sign = jsonObject.get("sign").toString();
		System.out.println("sign:" + sign+"content:" + content);
		PublicKey ysPayPublicKey = getYsPayPublicKey();

		Signature signetcheck = Signature.getInstance("SHA1WithRSA");
		signetcheck.initVerify(ysPayPublicKey);
		signetcheck.update(content.getBytes("UTF-8"));
		if (signetcheck.verify(Base64.decodeBase64(sign.getBytes("UTF-8")))) {
			flag = true;
		}

		return flag;
	}

	/**
	 * 异步通知参数验签
	 */
	public static boolean asynVerifyYs(Map<String, String> reqMap) throws Exception {
		boolean flag = false;

		PublicKey ysPayPublicKey = getYsPayPublicKey();

		String sign = reqMap.get("sign");
		System.out.println("签名原文:"+reqMap);
		String content = StringUtil.createLinkString(CommonUtil.paraFilter(reqMap));
		System.out.println("验签数据"+content+","+sign);
		Signature signetcheck = Signature.getInstance("SHA1WithRSA");
		signetcheck.initVerify(ysPayPublicKey);
		signetcheck.update(content.getBytes("UTF-8"));
		if (signetcheck.verify(Base64.decodeBase64(sign.getBytes("UTF-8")))) {
			flag = true;
		}

		return flag;
	}

}
