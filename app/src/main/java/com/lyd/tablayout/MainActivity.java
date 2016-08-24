package com.lyd.tablayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import view.TabLayout;

public class MainActivity extends AppCompatActivity {

    private TabLayout mTabLayout;
    String[] mTitles = new String[]{"流行音乐", "经典音乐", "轻音乐"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);

        mTabLayout.setData(mTitles);
        mTabLayout.setRedPointVisible(0);

        mTabLayout.setTabSelectListener(new TabLayout.OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

                Toast.makeText(MainActivity.this, position + "", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
