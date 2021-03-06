package net.helpgod.fragmenttest.common.base;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;

import com.markjmind.propose.Propose;

import net.helpgod.fragmenttest.BuildConfig;
import net.helpgod.fragmenttest.R;
import net.helpgod.fragmenttest.common.log.SimpleLog;
import net.helpgod.fragmenttest.common.util.Jwc;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;
    protected SimpleLog Log = new SimpleLog(this.getClass());

    /**
     * 현재 로그인한 사용자 정보 객체.
     */
    private String userToken;
    private String appVersion;
    private String newAppVersion;
    private boolean userLoginState = false;
    private boolean webLoginState = false;

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), 0);
            appVersion = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        mInstance = this;
    }

    /**
     * singleton
     * @return singleton
     */
    public static BaseApplication getBaseApplicationContext() {
        if (mInstance == null)
            throw new IllegalStateException("this application does not inherit BaseApplication");
        return mInstance;
    }

    /**
     * appVersion
     * @return appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }



    private static final int LOW_DPI_STATUS_BAR_HEIGHT = 19;
    private static final int MEDIUM_DPI_STATUS_BAR_HEIGHT = 25;
    private static final int HIGH_DPI_STATUS_BAR_HEIGHT = 38;

    /**
     * putLogLayout
     * @return void
     * desc : debug시 앱 로그화면
     */
    public void putLogLayout(Activity activity) {
        if (BuildConfig.DEBUG) {
            if (activity.findViewById(R.id.log_scrollView) == null) {
                final ViewGroup log_layout = (ViewGroup) Jwc.getInfalterView(activity, R.layout.log_layout);

                int another = (int) Math.ceil((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? 24 : 25) * (activity.getResources().getDisplayMetrics().density));

                Log.d("Device Density : " + activity.getResources().getDisplayMetrics().density);
                Log.d("스크린 높이 : " + getScreenHeight(activity));
                Log.d("네비게이션바 높이 : " + getNavigationBarHeight(activity));
                Log.d("상태바 높이 : " + getStatusBarSizeOnCreate(activity));
                Log.d("액션바 높이 : " + getActionBarSize(activity));

                writeScreenValue(activity);

                float start = -(Jwc.getWindowHeight(activity) - Jwc.getPix(activity, 50));
//                float start = -(Jwc.getWindowHeight(activity) - another);
//                float start = -(getScreenHeight(activity)-getStatusBarSizeOnCreate(activity));

                float end = 0f;
                log_layout.setY(start);
                activity.addContentView(log_layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                View log_title = log_layout.findViewById(R.id.log_title);
                Propose propose = new Propose(this);
                ObjectAnimator up = ObjectAnimator.ofFloat(log_layout, View.TRANSLATION_Y, start, end);
                up.setDuration(500);
                propose.motionDown.play(up, -start);
                propose.motionDown.enableFling(false).enableSingleTabUp(true).enableTabUp(false);
                start = 0f;
                end = Jwc.getWindowWidth(activity);
                ObjectAnimator right = ObjectAnimator.ofFloat(log_title, View.TRANSLATION_X, start, end);
                right.setDuration(500);
                propose.motionRight.play(right, end - start);
                propose.motionRight.enableFling(false).enableSingleTabUp(false).enableTabUp(false).enableReverse(true);
//                log_title.setX(end-Jwc.getPix(activity, 100));
                propose.motionRight.move(end - Jwc.getPix(activity, 140));
                log_title.setOnTouchListener(propose);

                TextView log_build_date = (TextView) log_layout.findViewById(R.id.log_build_date);
                log_build_date.setText("Ver. " + BuildConfig.buildDate);

                log_layout.findViewById(R.id.log_clear).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((ViewGroup) log_layout.findViewById(R.id.log_group)).removeAllViews();
                    }
                });


                SharedPreferences prefs = getSharedPreferences("log", MODE_PRIVATE);
                boolean isReal = prefs.getBoolean("isReal", false);
                final Switch log_server = (Switch) log_layout.findViewById(R.id.log_server);
                log_server.setChecked(isReal);
                if (isReal) {
                    log_server.setText("Real");
                } else {
                    log_server.setText("Test");
                }
                log_server.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        SharedPreferences prefs = getSharedPreferences("log", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean("isReal", isChecked);
                        editor.commit();
                        if (isChecked) {
                            log_server.setText("Real");
                        } else {
                            log_server.setText("Test");
                        }
                    }
                });

                log_layout.findViewById(R.id.log_hide).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (log_layout.findViewById(R.id.log_body).getVisibility() == View.VISIBLE) {
                            log_layout.findViewById(R.id.log_body).setVisibility(View.GONE);
                            ((Button) v).setText("Show");
                        } else {
                            log_layout.findViewById(R.id.log_body).setVisibility(View.VISIBLE);
                            ((Button) v).setText("Hide");
                            ((ScrollView) log_layout.findViewById(R.id.log_scrollView)).fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }
                });
            }
        }
    }
    public ScrollView getLogScrollView(Activity acitivity) {
        return (ScrollView) acitivity.findViewById(R.id.log_scrollView);
    }


    public static int getNavigationBarHeight(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int usableHeight = metrics.heightPixels;
            activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
            int realHeight = metrics.heightPixels;
            if (realHeight > usableHeight)
                return realHeight - usableHeight;
            else
                return 0;
        }
        return 0;
    }

    private int getStatusBarSizeOnCreate(Activity activity){
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    private int getActionBarSize(Activity activity){
        int actionBarHeight = 0;
        final TypedArray styledAttributes = activity.getTheme().obtainStyledAttributes(
                new int[] { android.R.attr.actionBarSize }
        );
        actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return actionBarHeight;
    }

    private int getScreenHeight(Activity activity){
        int screenHeight = 0;

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        screenHeight = metrics.heightPixels;

        return screenHeight;
    }

    private void writeScreenValue(Activity activity){

        Display display = activity.getWindowManager().getDefaultDisplay();
        String displayName = display.getName();  // minSdkVersion=17+
        Log.i("displayName  = " + displayName);

// display size in pixels
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.i("width        = " + width);
        Log.i("height       = " + height);

// pixels, dpi
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int heightPixels = metrics.heightPixels;
        int widthPixels = metrics.widthPixels;
        int densityDpi = metrics.densityDpi;
        float xdpi = metrics.xdpi;
        float ydpi = metrics.ydpi;
        Log.i("widthPixels  = " + widthPixels);
        Log.i("heightPixels = " + heightPixels);
        Log.i("densityDpi   = " + densityDpi);
        Log.i("xdpi         = " + xdpi);
        Log.i("ydpi         = " + ydpi);

// deprecated
        int screenHeight = display.getHeight();
        int screenWidth = display.getWidth();
        Log.i("screenHeight = " + screenHeight);
        Log.i("screenWidth  = " + screenWidth);

// orientation (either ORIENTATION_LANDSCAPE, ORIENTATION_PORTRAIT)
        int orientation = getResources().getConfiguration().orientation;
        Log.i("orientation  = " + orientation);
    }



    /**
     * UncatchException Handler
     * */
    private Thread.UncaughtExceptionHandler androidDefaultUEH;
    private UncaughtExceptionHandler unCatchExceptionHandler;

    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return unCatchExceptionHandler;
    }
    public class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            // 이곳에서 로그를 남기는 작업을 하면 된다.
            ex.printStackTrace();
            Log.e("UncaughtException : " + ex.getMessage());

            //
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(10);

            //androidDefaultUEH.uncaughtException(thread, ex);

        }
    }




//    protected PopupBasic mPopupBasic;
//
//    public PopupBasic getPopup(){
//        return this.mPopupBasic;
//    }
//
//    public void showPopup(Context context, int contentRes) {
//        showPopup(context, PopupBasic.TITLE_UNDEFINED, contentRes, PopupBasic.ONE_BUTTON_DEFAULT, null);
//    }
//    /**
//     *
//     * @param context : it is need that context is activity. it is not support with application context.
//     * @param titleRes
//     * @param content
//     */
//    public void alert(Context context, int titleRes, String content) {
//        mPopupBasic = new PopupBasic.Builder(context, PopupBasic.TYPE_ONE_BUTTON)
//                .setTitle(titleRes)
//                .setContents(content)
//                .create();
//        mPopupBasic.show();
//    }
//
//    public void alert(Context context, String title, String content) {
//        mPopupBasic = new PopupBasic.Builder(context, PopupBasic.TYPE_ONE_BUTTON)
//                .setTitle(title)
//                .setContents(content)
//                .create();
//        mPopupBasic.show();
//    }
//
//    public void showPopup(Context context, int titleRes, int contentRes, int bottomRes, View.OnClickListener onClickListener ) {
//        mPopupBasic = new PopupBasic.Builder(context, PopupBasic.TYPE_ONE_BUTTON)
//                .setTitle(titleRes)
//                .setContents(contentRes)
//                .setOneButtonText(bottomRes)
//                .setOneButtonListener(onClickListener)
//                .create();
//        mPopupBasic.show();
//    }
//
//    public void showPopup(Context context, int contentRes, int leftButtonRes, View.OnClickListener onLeftOnClickListener, int rightButtonRes, View.OnClickListener onRightOnClickListener) {
//        showPopup(context, PopupBasic.TITLE_UNDEFINED, contentRes, leftButtonRes, onLeftOnClickListener, rightButtonRes, onRightOnClickListener);
//    }
//
//    public void showPopup(Context context, int contentRes, View.OnClickListener leftOnClickListener, View.OnClickListener rightOnClickListener) {
//        showPopup(context, PopupBasic.TITLE_UNDEFINED, contentRes, PopupBasic.TWO_BUTTON_LEFT_DEFAULT, leftOnClickListener, PopupBasic.TWO_BUTTON_RIGHT_DEFAULT, rightOnClickListener);
//    }
//
//    public void showPopup(Context context, int titleRes, int contentRest, View.OnClickListener leftOnClickListener, View.OnClickListener rightOnClickListener) {
//        showPopup(context, titleRes, contentRest, PopupBasic.TWO_BUTTON_LEFT_DEFAULT, leftOnClickListener, PopupBasic.TWO_BUTTON_RIGHT_DEFAULT, rightOnClickListener);
//    }
//
//    public void showPopup(Context context, int titleRes, int contentRes, int leftButtonRes, View.OnClickListener onLeftOnClickListener, int rightButtonRes, View.OnClickListener onRightOnClickListener) {
//        mPopupBasic = new PopupBasic.Builder(context, PopupBasic.TYPE_TWO_BUTTON)
//                .setTitle(titleRes)
//                .setContents(contentRes)
//                .setLeftButtonText(leftButtonRes)
//                .setLeftButtonListener(onLeftOnClickListener)
//                .setRightButtonText(rightButtonRes)
//                .setRightButtonListener(onRightOnClickListener)
//                .create();
//        mPopupBasic.show();
//    }
}
