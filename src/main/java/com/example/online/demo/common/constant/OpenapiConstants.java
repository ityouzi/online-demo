/*
 * 文件名：Constants.java
 * 版权：Copyright by www.ysepay.com
 * 修改人：Cindy
 * 修改时间：2016年5月5日
 * 修改内容：
 */

package com.example.online.demo.common.constant;

public class OpenapiConstants {
  /** 指定支付方式 no_credit指不支持信用卡支付 */
  public static final String NO_CREDIT = "no_credit";
  /** 密码空间的动态随机码key */
  public static final String MCRYPT_KEY = "mcrypt_key";

  /** 是否需要验证码 */
  public static final String CHECK_CODE_FLAG = "checkCodeFlag";

  /** 账户里面可用资金类型 20：可提现金额。 28：可用交易金额 24：可提现+不可提现（转账） */
  public static final String ACCOUNT_TRADE_AMOUNT_TYPE = "28";

  /** 短信校验funCode:找回支付密码 */
  public static final String FUNCODE_FIND = "01";

  /**
   * 短信校验funCode:注册
   *
   * <p>扫码支付方式
   */
  public static final String FUNCODE_REG = "07";

  /** 短信校验funCode:银盛快捷支付 */
  public static final String FUNCODE_YSFASTPAY = "19";

  /** 短信校验funCode:银盛余额支付 */
  public static final String FUNCODE_YSACCOUNTPAY = "10";

  /** 短信校验funCode:银盛快捷签约 */
  public static final String FUNCODE_YSFASTPAYSIGN = "20";

  /** 短信校验funCode:更换手机 */
  public static final String FUNCODE_YSFASTPAYMOBILE = "21";

  /** 借记卡 */
  public static final String CARD_DEBIT = "11";

  /** 贷记卡 */
  public static final String CARD_CREDIT = "12";

  /** 对私存折 */
  public static final String DEPOSIT_PRI = "13";

  /** 对公借记卡 */
  public static final String DEBIT_TB = "21";

  /** 对公贷记卡 */
  public static final String CREDIT_TB = "22";

  /** 对公存折 */
  public static final String DEPOSIT_PUB = "23";

  /** 银联-快捷-PC间联 */
  public static final String PAGATE_UPOP = "90020002";

  /** 银联全渠道-快捷-PC间联 */
  public static final String PAGATE_UPMP = "90030002";
  /** openapi 到收银台请求的参数key tradeCode */
  public static final String TRADECODE = "tradeCode";
  /** openapi 到收银台请求的参数key method */
  public static final String METHOD = "method";
  /** 扫码支付 - 微信 */
  public static final String WECHATTYPE = "1902000";
  /** 扫码支付 - 支付宝 */
  public static final String ALIPAYTYPE = "1903000";

  /** 接口途径标识 */
  public static final String INTERFACEWAY = "Y";

  /** 0 银行借记账户 */
  public static final String DEBIT_CARD = "0";

  /** 1 银行贷记账户 */
  public static final String CREDIT_CARD = "1";

  /** 01 4.0版本中证件类型中的身份证 */
  public static final String TYPE_ID_CARD = "01";

  /** 中国银联 */
  public static final String CHINA_UNIONPAY = "9001000";

  /** 银联扫码 */
  public static final String UNIONPAY_SCAN = "9001002";

  /** 撤销状态 */
  public static final String CANCEL_ACTION = "success";

  /** 分账对账文件检查成功 */
  public static final String DIVISION_CHECK_FILE_SUCCESS = "0000";

  /** 分账对账文件下载接口返回map对应的key */
  public static final String DIVISION_FILE_PARAM_FILE_PATH = "filePath";

  public static final String DIVISION_FILE_PARAM_STATE = "state";

  public static final String DIVISION_FILE_PARAM_FILE_NAME = "fileName";

  public static final String DIVISION_FILE_PARAM_FILE_GROUP = "fileGroup";

  /** 00 为身份证人像面, 30 为身份证国徽面 35 为银行卡正面, 36 为银行卡反面 */
  public static final String PICTURE_SCAN_TYPE_00 = "00";

  public static final String PICTURE_SCAN_TYPE_30 = "30";

  public static final String PICTURE_SCAN_TYPE_35 = "35";

  public static final String PICTURE_SCAN_TYPE_36 = "36";

  public static final String FETCH_OPENID_TRADE_SOURCE = "00";
}
