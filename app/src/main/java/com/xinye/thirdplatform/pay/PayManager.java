package com.xinye.thirdplatform.pay;

import android.app.Activity;

/**
 * 支付管理器
 *
 * @author wangheng
 */
public final class PayManager {

    private PayManager() {

    }

    /**
     * 仅仅为了单例而存在的静态内部类
     *
     * @author wangheng
     */
    private static final class Singleton {
        private static final PayManager INSTANCE = new PayManager();
    }

    /**
     * 得到单例
     *
     * @return
     */
    public static PayManager getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 使用支付宝支付
     *
     * @param payActivity 要执行支付操作的Activity
     * @param order       支付的订单数据
     * @param callback    支付结果回调
     */
    public void payByAlipay(Activity payActivity, Order order, PayCallback callback) {
        VendorFactory.createAlipayVendor().pay(payActivity, order, callback);
    }
}
