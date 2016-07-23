package com.malin.like;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * 原文地址:
 * http://www.jianshu.com/p/03fdcfd3ae9c
 * 练习
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private HeartLayout mHeartLayout;
    private RelativeLayout mRelativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initView() {
        mRelativeLayout = (RelativeLayout) findViewById(R.id.rl_layout);
        mHeartLayout = (HeartLayout) findViewById(R.id.heartLayout);
    }

    private void initListener() {
        mRelativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.rl_layout: {
                mHeartLayout.addAnimationHeart();
                break;
            }

            default: {
                break;
            }
        }

    }
}
