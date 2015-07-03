package com.xinye.thirdplatform.pay;

/**
 * 支付完成回调
 *
 * @author wangheng
 */
public interface PayCallback {
    /**
     * 支付完成的回调
     *
     * @param result 支付结果
     */
    public void onPayCompleted(PayResult result);
}
