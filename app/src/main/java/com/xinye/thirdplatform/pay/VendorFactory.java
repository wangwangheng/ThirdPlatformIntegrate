package com.xinye.thirdplatform.pay;

import com.xinye.thirdplatform.pay.alipay.AlipayVendor;

/**
 * 支付提供商工厂
 *
 * @author wangheng
 */
public final class VendorFactory {

    private VendorFactory() {

    }

    /**
     * 创建支付宝支付商
     *
     * @return
     */
    public static Vendor createAlipayVendor() {
        return new AlipayVendor();
    }

}
