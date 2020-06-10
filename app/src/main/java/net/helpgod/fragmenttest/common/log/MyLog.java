package net.helpgod.fragmenttest.common.log;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.text.util.Linkify;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import net.helpgod.fragmenttest.R;


/**
 * 프로젝트명 :
 * 파일명 : MyLog
 * 작성자 :
 * 작성일 :
 * Description :
 */
public class MyLog extends SimpleLog implements SimpleLog.LogListener {
    ScrollView scrollView;
    ViewGroup log_group;
    int maxCount = 200;
    Handler handler = new Handler();

    public MyLog(Class<?> tagClass) {
        super(tagClass);
        setCallNum(6);
    }

    public void setScrollView(ScrollView scrollView){
        if(scrollView!=null) {
            this.scrollView = scrollView;
            this.log_group = (ViewGroup) scrollView.findViewById(R.id.log_group);
            this.setLogListener(this);
        }else{
            this.scrollView = null;
            this.log_group = null;
            this.setLogListener(null);
        }
    }

    @Override
    public void log(final String state, String tag, final String log) {
        if(check()) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    TextView text = new TextView(log_group.getContext());
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    text.setLayoutParams(params);
                    text.setText(log);
                    text.setTextSize(12);
                    Linkify.addLinks(text, Linkify.WEB_URLS);
                    text.setLinkTextColor(Color.parseColor("#00ffff"));
                    if(state.equals("e")) {
                        text.setTextColor(Color.RED);
                    }else if(state.equals("w")) {
                        text.setTextColor(Color.WHITE);
                    }else if(state.equals("d")) {
                        text.setTextColor(Color.YELLOW);
                    }else if(state.equals("i")) {
                        text.setTextColor(Color.GREEN);
                    }
                    if(!check()){
                        return;
                    }
                    if (maxCount < log_group.getChildCount()) {
                        log_group.removeViewAt(0);
                    }
                    log_group.addView(text);
                    scrollView.fullScroll(ScrollView.FOCUS_DOWN);

                }
            });

        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    boolean check(){
        return log_group !=null && log_group.isAttachedToWindow();
    }
}
