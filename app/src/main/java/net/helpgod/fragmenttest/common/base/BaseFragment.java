package net.helpgod.fragmenttest.common.base;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.helpgod.fragmenttest.common.log.MyLog;
import net.helpgod.fragmenttest.common.snackbar.TopSnackbar;

import androidx.fragment.app.Fragment;

public class BaseFragment extends Fragment {

    protected MyLog Log = new MyLog(this.getClass());
    public BaseActivity activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (BaseActivity) getActivity();
        Log.setScrollView(((BaseApplication) getApplication()).getLogScrollView(activity));


    }

    public Application getApplication(){
        return getActivity().getApplication();
    }

    public BaseApplication app() {
        return (BaseApplication) getContext().getApplicationContext();
    }

    public MyLog getLog() {
        return this.Log;
    }

    protected void hideIme() {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    /**
     * 기본 토스트(스넥바)
     */
    public void showToast(String strMessage) {

        TopSnackbar.make(activity.getCurrentFocus(), strMessage, TopSnackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void onResume() {
        super.onResume();

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
//        getView().setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                //This is the filter
//                if (event.getAction() != KeyEvent.ACTION_DOWN)
//                    return true;
//
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    onBackPressed();
//                    return true;
//                }
//                return false;
//            }
//        });
    }

//    public synchronized void onBackPressed() {
//        activity.onBackPressed();
//    }


}
