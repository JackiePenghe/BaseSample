package com.sscl.basesample.activities.widget;

import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.sscl.baselibrary.activity.BaseAppCompatActivity;
import com.sscl.baselibrary.utils.BaseManager;
import com.sscl.baselibrary.utils.DebugUtil;
import com.sscl.baselibrary.widget.CircleProgressBar;
import com.sscl.basesample.R;

/**
 * @author pengh
 */
public class CircleProgressBarActivity extends BaseAppCompatActivity {

    private static final String TAG = CircleProgressBarActivity.class.getSimpleName();
    private CircleProgressBar circleProgressBar;

    /**
     * 标题栏的返回按钮被按下的时候回调此方法
     */
    @Override
    protected boolean titleBackClicked() {
        onBackPressed();
        return true;
    }

    /**
     * 在设置布局之前需要进行的操作
     */
    @Override
    protected void doBeforeSetLayout() {

    }

    /**
     * 设置布局
     *
     * @return 布局id
     */
    @Override
    protected int setLayout() {
        return R.layout.activity_circle_progress_bar;
    }

    /**
     * 在设置布局之后，进行其他操作之前，所需要初始化的数据
     */
    @Override
    protected void doBeforeInitOthers() {

    }

    /**
     * 初始化布局控件
     */
    @Override
    protected void initViews() {
        circleProgressBar = findViewById(R.id.circle_progress_bar);
    }

    /**
     * 初始化控件数据
     */
    @Override
    protected void initViewData() {

    }

    /**
     * 初始化其他数据
     */
    @Override
    protected void initOtherData() {

    }

    /**
     * 初始化事件
     */
    @Override
    protected void initEvents() {

    }

    /**
     * 在最后进行的操作
     */
    @Override
    protected void doAfterAll() {
        circleProgressBar.setAnimateProgressDelay(1);
        circleProgressBar.setAnimateTimeMillis(1000);
        double maxProgress = circleProgressBar.getMaxProgress();
        DebugUtil.warnOut(TAG,"maxProgress = " + maxProgress);
        circleProgressBar.setProgress(1000);

        BaseManager.getHandler().postDelayed(() -> circleProgressBar.setProgressWithAnimate(3000), 2000);
    }

    /**
     * 设置菜单
     *
     * @param menu 菜单
     * @return 只是重写 public boolean onCreateOptionsMenu(Menu menu)
     */
    @Override
    protected boolean createOptionsMenu(@NonNull Menu menu) {
        return false;
    }

    /**
     * 设置菜单监听
     *
     * @param item 菜单的item
     * @return true表示处理了监听事件
     */
    @Override
    protected boolean optionsItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
