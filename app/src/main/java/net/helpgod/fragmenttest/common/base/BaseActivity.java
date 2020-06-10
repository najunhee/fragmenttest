package net.helpgod.fragmenttest.common.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;

import net.helpgod.fragmenttest.common.log.MyLog;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class BaseActivity extends AppCompatActivity {

    protected MyLog Log = new MyLog(this.getClass());
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
    }

    public BaseApplication app() {
        return (BaseApplication)getApplicationContext();
    }

    public MyLog getLog() {
        return Log;
    }

    private boolean isScreenOn() {
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean result = false;
        if ( Build.VERSION.SDK_INT >= 20 ) result = powerManager.isInteractive();
        else if ( Build.VERSION.SDK_INT < 20 ) result = powerManager.isScreenOn();
        return result;
    }

    //3초후 앱 종료처리
    public void startFinish(){
        Handler delayHandler = new Handler();
        delayHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);
    }
}
