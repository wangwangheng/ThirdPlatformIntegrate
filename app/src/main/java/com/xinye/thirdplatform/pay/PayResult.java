package com.xinye.thirdplatform.pay;

import java.io.Serializable;

/**
 * 支付结果
 *
 * @author wangheng
 */
public class PayResult implements Serializable {

    private static final long serialVersionUID = 4203082238685332205L;

    /**
     * 支付状态
     *
     * @author wangheng
     */
    public enum PayResultStatus {
        /**
         * 支付成功 *
         */
        SUCCESS,
        /**
         * 处理中 *
         */
        PROCESSING,
        /**
         * 支付失败 *
         */
        FAILED,
        /**
         * 支付被取消 *
         */
        CANCELED,
        /**
         * 网络错误 *
         */
        NETWORK_ERROR
    }

    /**
     * 支付结果状态码 *
     */
    private String resultStatus;

    /**
     * 本次操作返回的结果数据 *
     */
    private String result;

    /**
     * 提示信息 *
     */
    private String memo;

    /**
     * 被支付的订单 *
     */
    private Order order;

    /**
     * 得到支付状态 *
     */
    private PayResultStatus payResultStatus;

    /**
     * 得到支付结果状态
     *
     * @return
     */
    public PayResultStatus getPayResultStatus() {
        return payResultStatus;
    }

    public void setPayResultStatus(PayResultStatus payResultStatus) {
        this.payResultStatus = payResultStatus;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return "PayResult [resultStatus=" + resultStatus + ", result=" + result
                + ", memo=" + memo + ", order=" + order + ", payResultStatus="
                + payResultStatus + "]";
    }

}
