package com.clyr.view.loadingdialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.StyleRes;

import com.clyr.view.R;


/**
 * description:弹窗浮动加载进度条
 */
@SuppressLint("InflateParams")
public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showLoading(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.loadingdialog, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText(msg);
        return getDialog(context, view);
    }

    public static Dialog showLoading(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.loadingdialog, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText("加载中...");
        mLoadingDialog = new Dialog(context, R.style.ProgressDialog);

        Window dialogWindow = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        //lp.x = 100; // 新位置X坐标
        //lp.y = 100; // 新位置Y坐标
        lp.width = 240; // 宽度
        lp.height = 240; // 高度
        //lp.alpha = 0.7f; // 透明度

// 当Window的Attributes改变时系统会调用此函数,可以直接调用以应用上面对窗口参数的更改,也可以用setAttributes
// dialog.onWindowAttributesChanged(lp);
        dialogWindow.setAttributes(lp);

        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
// WindowManager m = getWindowManager();
// Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
// WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
// p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
// p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.95
// dialogWindow.setAttributes(p);

        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
//      mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.setContentView(view);


        mLoadingDialog.show();
        return mLoadingDialog;
    }


    public static Dialog showLoadingBall(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialogball, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText("加载中...");
        return getDialog(context, view);
    }

    public static Dialog showLoadingBall(Activity context, String msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialogball, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        if (msg != null && !msg.equals("")) {
            loadingText.setText(msg);
        } else {
            loadingText.setText("加载中...");
        }
        return getDialog(context, view);
    }

    public static Dialog showLoadingBall(Activity context, int style) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialogballtrans, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText("加载中...");
        return getDialog(context, view, style);
    }

    public static Dialog showLoadingBall(Activity context, String msg, int style) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialogballtrans, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        if (msg != null && msg != "") {
            loadingText.setText(msg);
        } else {
            loadingText.setText("加载中...");
        }
        return getDialog(context, view, style);
    }

    private static Dialog getDialog(Activity context, View view) {
        mLoadingDialog = new Dialog(context, R.style.ProgressDialog);
        Window dialogWindow = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        lp.width = 240; // 宽度
        lp.height = 240; // 高度
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    private static Dialog getDialog(Activity context, View view, int style) {
        mLoadingDialog = new Dialog(context, style);
        Window dialogWindow = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        lp.width = 240; // 宽度
        lp.height = 240; // 高度
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view);
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showLoading(Activity context, @StyleRes int proStyle) {
        View view = LayoutInflater.from(context).inflate(R.layout.loadingdialog, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText("加载中...");
        mLoadingDialog = new Dialog(context, proStyle);

        Window dialogWindow = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        lp.width = 240; // 宽度
        lp.height = 240; // 高度
        dialogWindow.setAttributes(lp);

        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view);

        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showLoading(Activity context, @StyleRes int proStyle, String IndicatorName) {
        View view = LayoutInflater.from(context).inflate(R.layout.loadingdialog, null);
        TextView loadingText = view.findViewById(R.id.tv_reminder);
        loadingText.setText("加载中...");
        AVLoadingIndicatorView indicatorView = view.findViewById(R.id.avi);
        indicatorView.setIndicator(IndicatorName);
        mLoadingDialog = new Dialog(context, proStyle);

        Window dialogWindow = mLoadingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);

        lp.width = 240; // 宽度
        lp.height = 240; // 高度
        dialogWindow.setAttributes(lp);

        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view);

        mLoadingDialog.show();
        return mLoadingDialog;
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }

    private static final String[] INDICATORS = new String[]{
            "BallPulseIndicator",
            "BallGridPulseIndicator",
            "BallClipRotateIndicator",
            "BallClipRotatePulseIndicator",
            "SquareSpinIndicator",
            "BallClipRotateMultipleIndicator",
            "BallPulseRiseIndicator",
            "BallRotateIndicator",
            "CubeTransitionIndicator",
            "BallZigZagIndicator",
            "BallZigZagDeflectIndicator",
            "BallTrianglePathIndicator",
            "BallScaleIndicator",
            "LineScaleIndicator",
            "LineScalePartyIndicator",
            "BallScaleMultipleIndicator",
            "BallPulseSyncIndicator",
            "BallBeatIndicator",
            "LineScalePulseOutIndicator",
            "LineScalePulseOutRapidIndicator",
            "BallScaleRippleIndicator",
            "BallScaleRippleMultipleIndicator",
            "BallSpinFadeLoaderIndicator",
            "LineSpinFadeLoaderIndicator",
            "TriangleSkewSpinIndicator",
            "PacmanIndicator",
            "BallGridBeatIndicator",
            "SemiCircleSpinIndicator",
            "com.wang.avi.sample.MyCustomIndicator"
    };
}
