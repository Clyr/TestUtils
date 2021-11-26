package com.clyr.view.textdialog;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.clyr.base.interfaces.SetBackground;
import com.clyr.view.R;


/**
 * @author clyr
 * 自定义居中弹出dialog
 */
public class TextDialog extends Dialog implements View.OnClickListener {

    private Context context;

    private int layoutResID;

    /**
     * 要监听的控件id
     */
    private int[] listenedItems;

    private OnCenterItemClickListener listener;
    private SetBackground mSetBackground;
    public TextDialog(Context context, int layoutResID, int[] listenedItems, SetBackground mSetBackground) {
        super(context, R.style.MyDialog);
        this.context = context;
        this.layoutResID = layoutResID;
        this.listenedItems = listenedItems;
        this.mSetBackground = mSetBackground;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setGravity(Gravity.CENTER); // 此处可以设置dialog显示的位置为居中
//        window.setWindowAnimations(R.style.bottom_menu_animation); // 添加动画效果
        window.setWindowAnimations(R.style.dialog_animation); // 添加动画效果

        setContentView(layoutResID);
        // 宽度全屏
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = display.getWidth() * 3 / 4; // 设置dialog宽度为屏幕的4/5
        getWindow().setAttributes(lp);
        // 点击Dialog外部消失
        setCanceledOnTouchOutside(true);
        for (int id : listenedItems) {
            findViewById(id).setOnClickListener(this);
        }
        mSetBackground.backgroundAlpha(0.5f);//interface方法调用
    }

    public interface OnCenterItemClickListener {

        void OnCenterItemClick(TextDialog dialog, View view);

    }

    @Override
    public void cancel() {
        mSetBackground.backgroundAlpha(1f);
        super.cancel();
    }

    @Override
    public void dismiss() {
        mSetBackground.backgroundAlpha(1f);
        super.dismiss();
    }

    public void setOnCenterItemClickListener(OnCenterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View view) {
        dismiss();
        listener.OnCenterItemClick(this, view);
    }

}

