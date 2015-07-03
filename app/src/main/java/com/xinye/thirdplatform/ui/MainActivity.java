package com.xinye.thirdplatform.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xinye.thirdplatform.R;
import com.xinye.thirdplatform.pay.Order;
import com.xinye.thirdplatform.pay.PayCallback;
import com.xinye.thirdplatform.pay.PayManager;
import com.xinye.thirdplatform.pay.PayResult;

/**
 * @author wangheng
 * @describe
 * @date 2015-07-03 12-34
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private Button mAliplayButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mAliplayButton = (Button) findViewById(R.id.btnAlipay);
        mAliplayButton.setOnClickListener(MainActivity.this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAlipay:
                payByAlipay();
                break;
        }
    }

    /**
     * 通过支付宝支付
     */
    private void payByAlipay() {
        Order order = new Order();
        order.setOrderNo("12345566789");
        order.setDetail("Android Thread Book");
        order.setSubject("Buy book");
        order.setTimeoutMinutes(30);
        order.setPrice("0.1");

        PayManager.getInstance().payByAlipay(MainActivity.this, order, new PayCallback() {
            @Override
            public void onPayCompleted(PayResult result) {
                switch(result.getPayResultStatus()){
                    case SUCCESS:
                        Toast.makeText(MainActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                        break;
                    case PROCESSING:
                        Toast.makeText(MainActivity.this,"PROCESSING",Toast.LENGTH_SHORT).show();
                        break;
                    case FAILED:
                        Toast.makeText(MainActivity.this,"FAILED",Toast.LENGTH_SHORT).show();
                        break;
                    case CANCELED:
                        Toast.makeText(MainActivity.this,"CANCELED",Toast.LENGTH_SHORT).show();
                        break;
                    case NETWORK_ERROR:
                        Toast.makeText(MainActivity.this,"NETWORK_ERROR",Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }
}
