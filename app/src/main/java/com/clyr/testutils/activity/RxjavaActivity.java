package com.clyr.testutils.activity;

import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RxjavaActivity extends BaseActivity {
    private static final String TAG = "RxJavaTag";
    private Disposable mDisposable;

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rxjava);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void initView() {
        initBar();
        textView = findViewById(R.id.textView);

        Observable<Integer> observable = Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
        });
        observable.subscribe(System.out::println);
        Observable.range(0, 10).map(String::valueOf).forEach(System.out::println);
        //下面的操作可以每个3秒的时间发送一个整数，整数从0开始：
        Observable.interval(3, TimeUnit.SECONDS).subscribe(System.out::println);
    }

    private void initRxjava() {
        //被观察者
        Observable obs = Observable.create((emitter) -> {
            emitter.onNext("连载1");
            emitter.onNext("连载2");
            emitter.onNext("连载3");
            emitter.onComplete();
        });

        //观察者
        Observer<String> observer = new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable = d;
                Log.e(TAG, "onSubscribe");
                textView.setText(textView.getText().toString().trim() + "\n" + "onSubscribe()");
            }

            @Override
            public void onNext(String value) {
                if ("2".equals(value)) {
                    mDisposable.dispose();
                    return;
                }
                Log.e(TAG, "onNext:" + value);
                textView.setText(textView.getText().toString().trim() + "\n" + value);
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG, "onError=" + e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.e(TAG, "onComplete()");
                textView.setText(textView.getText().toString().trim() + "\n" + "onComplete()");
            }
        };
        //订阅
        obs.subscribe(observer);
    }

    //RxJava链式使用
    private void rxJavaChainUse() {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                emitter.onNext("连载1");
                emitter.onNext("连载2");
                emitter.onNext("连载3");
                emitter.onComplete();
            }
        })
                .observeOn(AndroidSchedulers.mainThread())//回调在主线程
                .subscribeOn(Schedulers.io())//执行在io线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(String value) {
                        Log.e(TAG, "onNext:" + value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError=" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete()");
                    }
                });
    }


    //定时操作
    private void timeDoSomething() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(123);
                sleep(3000);
                emitter.onNext(456);
            }
        }).observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.e(TAG, integer + "");
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                }, new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                });

    }

    //
    private void complicatedDoSomething(final int[] drawableRes) {
        Observable.create(new ObservableOnSubscribe<Drawable>() {
            @Override
            public void subscribe(ObservableEmitter<Drawable> emitter) throws Exception {
                for (int i = 0; i < drawableRes.length; i++) {
                    Drawable drawable = getResources().getDrawable(drawableRes[i]);
                    //第6个图片延时3秒后架子
                    if (i == 5) {
                        sleep(3000);
                    }
                    //复制第7张图片到sd卡
                    if (i == 6) {
                        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                        saveBitmap(bitmap, "test.png", Bitmap.CompressFormat.PNG);
                    }
                    //上传到网络
                    if (i == 7) {
                        updateIcon(drawable);
                    }
                    emitter.onNext(drawable);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Drawable>() {
                    @Override
                    public void accept(Drawable drawable) throws Exception {
                        //回调后在UI界面上展示出来
                    }
                });
    }

    private void updateIcon(Drawable drawable) {
    }


    /**
     * 将Bitmap以指定格式保存到指定路径
     *
     * @param bitmap
     * @param name
     */
    public void saveBitmap(Bitmap bitmap, String name, Bitmap.CompressFormat format) {
        // 创建一个位于SD卡上的文件
        File file = new File(Environment.getExternalStorageDirectory(),
                name);
        FileOutputStream out = null;
        try {
            // 打开指定文件输出流
            out = new FileOutputStream(file);
            // 将位图输出到指定文件
            bitmap.compress(format, 100,
                    out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}