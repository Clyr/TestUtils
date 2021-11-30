package com.clyr.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.bumptech.glide.request.transition.Transition;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Decoder.BASE64Decoder;
import Decoder.BASE64Encoder;

public class BitmapUtils {


    private static final String TAG = BitmapUtils.class.getSimpleName();

    /***
     * 图片的缩放方法
     *
     * @param bgimage 源图片资源
     * @param newWidth 缩放后宽度
     * @param newHeight 缩放后高度
     * @return
     */
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    /**
     * 从网络获取图片
     */
    public static Bitmap getImageFromNet(String url) {
        HttpURLConnection conn = null;
        try {
            URL mURL = new URL(url);
            conn = (HttpURLConnection) mURL.openConnection();
            conn.setRequestMethod("GET"); //设置请求方法
            conn.setConnectTimeout(10000); //设置连接服务器超时时间
            conn.setReadTimeout(5000);  //设置读取数据超时时间
            conn.connect(); //开始连接
            int responseCode = conn.getResponseCode(); //得到服务器的响应码
            if (responseCode == 200) {
                //访问成功
                InputStream is = conn.getInputStream(); //获得服务器返回的流数据
                Bitmap bitmap = BitmapFactory.decodeStream(is); //根据流数据 创建一个bitmap对象
                return bitmap;
            } else {
                //访问失败
                Log.d("imagenet", "访问失败===responseCode：" + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect(); //断开连接
            }
        }
        return null;
    }

    /**
     * drawable 转 Bitmap
     *
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        BitmapDrawable bd = (BitmapDrawable) drawable;
        return bd.getBitmap();
    }

    public static Bitmap drawableToBitmap(int drawble, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawble);
        return bitmap;
    }

    /**
     * bitmap 转 drawable
     *
     * @param bitmap
     * @return
     */
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(bitmap);
    }


    /**
     * 图片转化成base64字符串
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @return
     */

    public static String GetImageStr() {
        String imgFile = "C:/Users/Star/Desktop/test.png";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }

    /**
     * base64字符串转化成图片
     * 对字节数组字符串进行Base64解码并生成图片
     *
     * @param imgStr
     * @return
     */

    public static boolean GenerateImage(String imgStr) {
        if (imgStr == null) // 图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = "C:/Users/Star/Desktop/test22.png";// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 通过Glide加载图片 Demo
     * https://muyangmin.github.io/glide-docs-cn/
     *
     * @param context
     * @param imgUrl
     * @param imageView
     */
    public static void getImgForGlide(Context context, String imgUrl, ImageView imageView) {
        //加载
        Glide.with(context).load(imgUrl).into(imageView);
        //取消加载 尽管及时取消不必要的加载是很好的实践，但这并不是必须的操作。实际上，
        //当 Glide.with() 中传入的 Activity 或 Fragment 实例销毁时，Glide 会自动取消加载并回收资源。
        Glide.with(context).clear(imageView);
        //使用解析
        int placeholder = 0;
        RequestOptions cropOptions = new RequestOptions().centerCrop();
        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        RequestBuilder<Drawable> requestBuilder = Glide.with(context).asDrawable();
        requestBuilder.apply(cropOptions);
//        requestBuilder.transition(transitionOptions);

        Glide.with(context)
                .load(imgUrl)
                .placeholder(placeholder)//占位符// R.drawable.placeholder/new ColorDrawable(Color.BLACK)
                .error(placeholder)//错误符(Error) 同上
                .fallback(placeholder)//后备回调符(Fallback)
                .centerCrop()//会裁剪图片将imageView填满
                .fitCenter()//会绽放图像, 将图像全显示出来
                .circleCrop()//圆图
                .apply(cropOptions)//通过RequestOptions配置应用 适用于通配
                .override(200, 200)//制定图片大小
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
                .thumbnail(Glide.with(context)
                        .load(imgUrl).override(10))//缩略图 (Thumbnail) 请求
                .thumbnail(/*sizeMultiplier=*/ 0.25f)//加载相同的图片，但尺寸为 View 或 Target 的某个百分比
                .error(Glide.with(context)
                        .load(imgUrl))//在失败时开始新的请求
                .diskCacheStrategy(DiskCacheStrategy.ALL)//缓存
                .onlyRetrieveFromCache(true)//仅从缓存加载图片
                .skipMemoryCache(true)//跳过缓存
                .listener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target target, boolean isFirstResource) {
                        // Log the GlideException here (locally or with a remote logging framework):
                        Log.e(TAG, "Load failed", e);

                        // You can also log the individual causes:
                        for (Throwable t : e.getRootCauses()) {
                            Log.e(TAG, "Caused by", t);
                        }
                        // Or, to log all root causes locally, you can use the built in helper method:
                        e.logRootCauses(TAG);

                        return false; // Allow calling onLoadFailed on the Target.
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        // Log successes here or use DataSource to keep track of cache hits and misses.

                        return false; // Allow calling onResourceReady on the Target.
                    }

                })
                .into(imageView);

       /* RequestBuilder 也可以被复用于开始多个请求：

        RequestBuilder<Drawable> requestBuilder =
                Glide.with(fragment)
                        .asDrawable()
                        .apply(requestOptions);

        for (int i = 0; i < numViews; i++) {
            ImageView view = viewGroup.getChildAt(i);
            String url = urls.get(i);
            requestBuilder.load(url).into(view);
        }*/


        Glide.with(context)
                .load(imgUrl)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setImageBitmap(drawableToBitmap(resource));
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
        /*Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(myImageView);*/
        /*定制请求
        Glide 提供了许多可应用于单一请求的选项，包括变换、过渡、缓存选项等。

        默认选项可以直接应用于请求上：

        Glide.with(fragment)
                .load(myUrl)
                .placeholder(placeholder)
                .fitCenter()
                .into(imageView);
        选项也可以通过 RequestOptions 类来在多个请求之间共享：

        com.bumptech.glide.request.RequestOptions sharedOptions =
                new RequestOptions()
                        .placeholder(placeholder)
                        .fitCenter();

        Glide.with(fragment)
                .load(myUrl)
                .apply(sharedOptions)
                .into(imageView1);

        Glide.with(fragment)
                .load(myUrl)
                .apply(sharedOptions)
                .into(imageView2);*/
        /* TODO 在 ListView 和 RecyclerView 中的使用
        *
        在 ListView 或 RecyclerView 中加载图片的代码和在单独的 View 中加载完全一样。Glide 已经自动处理了 View 的复用和请求的取消：

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String url = urls.get(position);
            Glide.with(fragment)
                .load(url)
                .into(holder.imageView);
        }
        对 url 进行 null 检验并不是必须的，如果 url 为 null，Glide 会清空 View 的内容，或者显示 placeholder Drawable 或 fallback Drawable 的内容。

        Glide 唯一的要求是，对于任何可复用的 View 或 Target ，如果它们在之前的位置上，用 Glide 进行过加载操作，那么在新的位置上要去执行一个新的加载操作，或调用 clear() API 停止 Glide 的工作。

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (isImagePosition(position)) {
                String url = urls.get(position);
                Glide.with(fragment)
                    .load(url)
                    .into(holder.imageView);
            } else {
                Glide.with(fragment).clear(holder.imageView);
                holder.imageView.setImageDrawable(specialDrawable);
            }
        }*/
        /*TODO 后台线程

        在后台线程加载图片也是直接使用 submit(int, int)：

        FutureTarget<Bitmap> futureTarget =
                Glide.with(context)
                        .asBitmap()
                        .load(url)
                        .submit(width, height);

        Bitmap bitmap = futureTarget.get();

        // Do something with the Bitmap and then when you're done with it:
        Glide.with(context).clear(futureTarget);
        如果你不想让 Bitmap 和 Drawable 自身在后台线程中，你也可以使用和前台线程一样的方式来开始异步加载：

        Glide.with(context)
                .asBitmap()
                .load(url)
                .into(new Target<Bitmap>() {
                     ...
                });*/
    }


    public static Bitmap rToBitmap(int drawble, Context context) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), drawble);
        return bitmap;
    }

    public static void setBitmap(Context context,Object imgUrl,ImageView imageView){
        Glide.with(context)
                .load(imgUrl)
                .centerCrop()
                .placeholder(R.drawable.back)
                .into(imageView);
    }
}
