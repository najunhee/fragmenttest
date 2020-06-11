package net.helpgod.fragmenttest;

import android.os.Bundle;
import android.os.Handler;

import net.helpgod.fragmenttest.common.base.BaseActivity;
import net.helpgod.fragmenttest.common.base.BaseApplication;

import androidx.annotation.Nullable;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if(BuildConfig.DEBUG){
//            app().putLogLayout(this);
//        }

    }

    @Override
    public void onBackPressed() {
        Log.d("### MainActivity onBackPressed()");
        super.onBackPressed();

//        finishAffinity();
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                System.exit(0);
//            }
//        }, 200);
    }

    public void finishApp(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                System.exit(0);
            }
        }, 200);
    }

    public BaseApplication app() {
        return (BaseApplication)getApplicationContext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
