package com.xinye.thirdplatform.pay;

import java.io.Serializable;

/**
 * 订单
 *
 * @author wangheng
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 4934595622446107416L;

    /**
     * 订单号 *
     */
    private String orderNo;

    /**
     * 订单主题(标题) *
     */
    private String subject;

    /**
     * 订单详情 *
     */
    private String detail;

    /**
     * 订单总价 *
     */
    private String price;

    /**
     * 超时时间 - 分钟数 *
     */
    private int timeoutMinutes;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getTimeoutMinutes() {
        return timeoutMinutes;
    }

    public void setTimeoutMinutes(int timeoutMinutes) {
        this.timeoutMinutes = timeoutMinutes;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Order [orderNo=" + orderNo + ", subject=" + subject
                + ", detail=" + detail + ", price=" + price
                + ", timeoutMinutes=" + timeoutMinutes + "]";
    }
}
