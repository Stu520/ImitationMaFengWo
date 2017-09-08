package com.imitationmafengwo.base;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.imitationmafengwo.R;
import com.imitationmafengwo.utils.log.L;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * Created by Stu on 2016/11/21.
 */

public class BaseActivity extends SupportActivity {
    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;

    protected View topStatusBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
//            PushAgent.getInstance(this).onAppStart();
        }catch (OutOfMemoryError error){}

        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        this.hideTopStatusBar();
    }

    protected void hideTopStatusBar() {
        try {
            topStatusBar = findViewById(R.id.top_status_bar);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT && topStatusBar != null) {
            topStatusBar.setVisibility(View.GONE);
        }
    }

    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            if(onBackPressedAgainToExit()){
                if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                    finish();
                } else {
                    TOUCH_TIME = System.currentTimeMillis();
                    Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
                }
            }else{
                finish();
            }
        }
    }

    public boolean onBackPressedAgainToExit(){
        return false;
    }


    @Override
    protected void onResume(){
        super.onResume();
        L.e("--==>>[show]"+this);
    }
}
