package com.xinye.thirdplatform.pay.alipay;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.xinye.thirdplatform.pay.Order;
import com.xinye.thirdplatform.pay.PayCallback;
import com.xinye.thirdplatform.pay.PayResult;
import com.xinye.thirdplatform.pay.Vendor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 支付宝支付
 *
 * @author wangheng
 */
public class AlipayVendor implements Vendor {

    private static final String TAG = AlipayVendor.class.getSimpleName();

    /**
     * 商户PID *
     */
    public static final String PARTNER = "";

    /**
     * 商户收款账号 *
     */
    public static final String SELLER = "";

    /**
     * 商户私钥，pkcs8格式 *
     */
    public static final String RSA_PRIVATE = "";

    /**
     * 支付宝公钥 *
     */
    public static final String RSA_PUBLIC = "";

    /**
     * 支付完成通知的URL *
     */
    private static final String NOTIFY_HOST_URL = "";

    /**
     * 订单支付成功 *
     */
    private static final String STATUS_SUCCESS = "9000";

    /**
     * 正在处理中 *
     */
    private static final String STATUS_PROCESSING = "8000";

    /**
     * 订单支付失败 *
     */
    private static final String STATUS_FAILED = "4000";

    /**
     * 用户中途取消 *
     */
    private static final String STATUS_CANCELED = "6001";

    /**
     * 网络连接出错 *
     */
    private static final String STATUS_NETWORK_ERROR = "6002";

    /**
     * 保证Callback在主线程调用(无论用户调用支付API在任何线程) *
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void pay(final Activity payActivity, final Order order,
                    final PayCallback callback) {

        // 订单
        String orderInfo = getOrderInfo(payActivity, order);
        Log.i(TAG, "加密前获取到的数据是：" + orderInfo);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();
        Log.i(TAG,"加密后获取到的数据是：" + payInfo);
        Runnable task = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(payActivity);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                final PayResult payResult = convertResultToPayResult(result,
                        order);

                if (callback != null) {
                    mHandler.post(new Runnable() {

                        @Override
                        public void run() {
                            callback.onPayCompleted(payResult);
                        }
                    });
                }
            }
        };
        ExecutorService service = Executors.newSingleThreadExecutor();
        service.execute(task);
        service.shutdown();
    }

    /**
     * 转换支付结果为通用的支付结果
     *
     * @param rawResult
     * @return
     */
    private PayResult convertResultToPayResult(String rawResult, Order order) {

        final PayResult payResult = new PayResult();
        String[] resultParams = rawResult.split(";");
        for (String resultParam : resultParams) {
            if (resultParam.startsWith("resultStatus")) {
                payResult
                        .setResultStatus(getValue(resultParam, "resultStatus"));
            }
            if (resultParam.startsWith("result")) {
                payResult.setResult(getValue(resultParam, "result"));
            }
            if (resultParam.startsWith("memo")) {
                payResult.setMemo(getValue(resultParam, "memo"));
            }
        }

        payResult.setOrder(order);

        payResult.setPayResultStatus(convertResutStatus(payResult
                .getResultStatus()));

        return payResult;
    }

    private String getValue(String content, String key) {
        String prefix = key + "={";
        return content.substring(content.indexOf(prefix) + prefix.length(),
                content.lastIndexOf("}"));
    }

    /**
     * 转换alipay支付状态为通用支付状态
     *
     * @param alipayResult
     * @return
     */
    private PayResult.PayResultStatus convertResutStatus(String alipayResult) {
        if (TextUtils.equals(STATUS_SUCCESS, alipayResult)) {
            return PayResult.PayResultStatus.SUCCESS;
        } else if (TextUtils.equals(STATUS_FAILED, alipayResult)) {
            return PayResult.PayResultStatus.FAILED;
        } else if (TextUtils.equals(STATUS_CANCELED, alipayResult)) {
            return PayResult.PayResultStatus.CANCELED;
        } else if (TextUtils.equals(STATUS_NETWORK_ERROR, alipayResult)) {
            return PayResult.PayResultStatus.NETWORK_ERROR;
        } else if (TextUtils.equals(STATUS_PROCESSING, alipayResult)) {
            return PayResult.PayResultStatus.PROCESSING;
        }
        return null;
    }

    private String getOrderInfo(Activity activity, Order order) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + order.getOrderNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + order.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + order.getDetail() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order.getPrice() + "\"";
        // notify url
        orderInfo += "&notify_url=" + "\"" + NOTIFY_HOST_URL + "\"";


        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"" + order.getTimeoutMinutes() + "m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        // orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        // appenv
        orderInfo += "&appenv=\"system=android^version="
                + getAppVersionName(activity) + "\"";

        // orderInfo+="&Authorization="+ "\"" + token+ "\"";
        return orderInfo;
    }

    /**
     * 返回当前程序版本名
     *
     * @param context
     * @return
     */
    public String getAppVersionName(Context context) {
        String versionName = "1.0";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "1.0";
            }
        } catch (Exception e) {
            return "1.0";
        }
        return versionName;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
