package com.xinye.thirdplatform.pay;

import android.app.Activity;

/**
 * 支付厂商可以提供的行为
 *
 * @author wangheng
 */
public interface Vendor {

    /**
     * 支付
     *
     * @param payActivity 要执行支付操作的Activity
     * @param order       支付的订单数据
     * @param callback    支付结果回调
     */
    public void pay(Activity payActivity, Order order, PayCallback callback);
}
