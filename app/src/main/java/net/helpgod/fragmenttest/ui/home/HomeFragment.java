package net.helpgod.fragmenttest.ui.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.helpgod.fragmenttest.MainActivity;
import net.helpgod.fragmenttest.R;
import net.helpgod.fragmenttest.common.base.BaseFragment;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

public class HomeFragment extends BaseFragment {

    private static final int NH_THREAD_MESSAGE_APPCLOSETIME = 0; /* BackPress 2회후 종료를 위해서 */

    // 뒤로가기 종료기능위한
    private boolean checkFinish = false;
    private MyMassgeHandler mHandler;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d("HomeFragment onCreateView");

        mHandler = new MyMassgeHandler();

        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(HomeFragment.this)
                        .navigate(R.id.action_HomeFragment_to_blankFragment2);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
//                 Handle the back button event
                if (!checkFinish) {
                    showToast(getString(R.string.destory_app_message));
                    checkFinish = true;
                    mHandler.sendEmptyMessageDelayed(NH_THREAD_MESSAGE_APPCLOSETIME, 1000 * 2);
                    // 2초 후에 handleMessage에 메시지를 전달한다.
                } else {
//                  //App 종료처리.
                    ((MainActivity)activity).finishApp();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
    }

    /**
     * Handler 클래스
     */
    class MyMassgeHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case NH_THREAD_MESSAGE_APPCLOSETIME:/* BackPress 2초후 false 처리 */
                    checkFinish = false;
                    // 2초가 지나면 다시 Falg 를 false로 바꾼다.
                    Log.d("handleMessage mFlag : " + checkFinish);
                    break;
            }
        }
    }
}
