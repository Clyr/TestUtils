package com.clyr.three_sdk.baidumapsdk.demo.geometry;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.animation.AlphaAnimation;
import com.baidu.mapapi.animation.Animation;
import com.baidu.mapapi.animation.AnimationSet;
import com.baidu.mapapi.animation.RotateAnimation;
import com.baidu.mapapi.animation.ScaleAnimation;
import com.baidu.mapapi.animation.SingleScaleAnimation;
import com.baidu.mapapi.animation.Transformation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;


/**
 * 演示Marker动画：缩放、平移、旋转、透明、组合动画
 */
public class MarkerAnimationDemo extends AppCompatActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Marker mMarkerA;
    private Marker mMarkerB;
    private Marker mMarkerC;
    private Marker mMarkerD;
    private Marker mMarkerE;
    private Marker mMarkerF;
    private Marker mMarkerG;
    private Point mScreenCenterPoint;

    // 初始化全局 bitmap 信息，不用时及时 recycle
    private BitmapDescriptor bitmapA = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);
    private BitmapDescriptor bitmapB = BitmapDescriptorFactory.fromResource(R.drawable.icon_markb);
    private BitmapDescriptor bitmapC = BitmapDescriptorFactory.fromResource(R.drawable.icon_markc);
    private BitmapDescriptor bitmapD = BitmapDescriptorFactory.fromResource(R.drawable.icon_markd);
    private BitmapDescriptor bitmapE = BitmapDescriptorFactory.fromResource(R.drawable.icon_marke);
    private BitmapDescriptor bitmapF = BitmapDescriptorFactory.fromResource(R.drawable.icon_markf);
    private BitmapDescriptor bitmapG = BitmapDescriptorFactory.fromResource(R.drawable.icon_markg);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_animation);
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                initOverlay();
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeStart(MapStatus status, int reason) {

            }

            @Override
            public void onMapStatusChange(MapStatus status) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus status) {
                if (null == mMarkerF) {
                    return;
                }
                mMarkerF.setAnimation(getTransformationPoint());
                mMarkerF.startAnimation();
            }
        });
    }

    /**
     * 开启动画
     */
    public void startAnimation(View view) {
        int id = view.getId();
        if (id == R.id.btn_rotate) {// 旋转动画
            startRotateAnimation();
        } else if (id == R.id.btn_scale) {// 缩放动画
            startScaleAnimation();
        } else if (id == R.id.btn_transformation) {// 平移动画
            startTransformation();
        } else if (id == R.id.btn_alpha) {// 透明动画
            startAlphaAnimation();
        } else if (id == R.id.btn_singleScale) {// 组合动画
            startSingleScaleAnimation();
        } else if (id == R.id.btn_animationSet) {// 组合动画
            startAnimationSet();
        }
    }

    /**
     * 初始化Overlay
     */
    public void initOverlay() {
        // add marker overlay
        LatLng latLngA = new LatLng(40.023537, 116.289429);
        LatLng latLngB = new LatLng(40.022211, 116.406137);
        LatLng latLngC = new LatLng(40.022211, 116.499274);
        LatLng latLngD = new LatLng(39.847829, 116.289429);
        LatLng latLngE = new LatLng(39.862009, 116.394064);
        LatLng latLngG = new LatLng(39.856691, 116.503873);

        MarkerOptions markerOptionsA = new MarkerOptions().position(latLngA).icon(bitmapA);
        mMarkerA = (Marker) (mBaiduMap.addOverlay(markerOptionsA));

        MarkerOptions markerOptionsB = new MarkerOptions().position(latLngB).icon(bitmapB);
        mMarkerB = (Marker) (mBaiduMap.addOverlay(markerOptionsB));

        MarkerOptions markerOptionsC = new MarkerOptions().position(latLngC).icon(bitmapC);
        mMarkerC = (Marker) (mBaiduMap.addOverlay(markerOptionsC));

        MarkerOptions markerOptionsD = new MarkerOptions().position(latLngD).icon(bitmapD);
        mMarkerD = (Marker) (mBaiduMap.addOverlay(markerOptionsD));

        MarkerOptions markerOptionsE = new MarkerOptions().position(latLngE).icon(bitmapE);
        mMarkerE = (Marker) (mBaiduMap.addOverlay(markerOptionsE));

        if (null != mBaiduMap.getMapStatus()) {
          LatLng  latLngF = mBaiduMap.getMapStatus().target;
            mScreenCenterPoint = mBaiduMap.getProjection().toScreenLocation(latLngF);
            MarkerOptions markerOptionsF = new MarkerOptions().position(latLngF).icon(bitmapF).perspective(true)
                    .fixedScreenPosition(mScreenCenterPoint);
            mMarkerF = (Marker) (mBaiduMap.addOverlay(markerOptionsF));
        }

        MarkerOptions markerOptionsG = new MarkerOptions().position(latLngG).icon(bitmapG);
        mMarkerG = (Marker) (mBaiduMap.addOverlay(markerOptionsG));

        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.zoomTo(12.0f);
        mBaiduMap.setMapStatus(mapStatusUpdate);
    }

    /**
     * 开启旋转动画
     */
    public void startRotateAnimation() {
        mMarkerA.setAnimation(getRotateAnimation());
        mMarkerA.startAnimation();
    }

    /**
     * 开启缩放动画
     */
    public void startScaleAnimation() {
        mMarkerB.setAnimation(getScaleAnimation());
        mMarkerB.startAnimation();
    }

    /**
     * 开启平移动画
     */
    public void startTransformation() {
        mMarkerC.setAnimation(getTransformation());
        mMarkerC.startAnimation();
    }

    /**
     * 开启单边缩放动画 X或Y方向
     */
    public void startSingleScaleAnimation() {
        mMarkerG.setAnimation(getSingleScaleAnimation());
        mMarkerG.startAnimation();
    }

    /**
     * 添加透明动画
     */
    public void startAlphaAnimation() {
        mMarkerD.setAnimation(getAlphaAnimation());
        mMarkerD.startAnimation();
    }

    /**
     * 得到单独缩放动画类
     */
    public Animation getSingleScaleAnimation() {
        SingleScaleAnimation mSingleScale = new SingleScaleAnimation(SingleScaleAnimation.ScaleType.SCALE_X, 1f, 2f, 1f);
        mSingleScale.setDuration(1000);
        mSingleScale.setRepeatCount(1);
        mSingleScale.setRepeatMode(Animation.RepeatMode.RESTART);
        return mSingleScale;
    }

    /**
     * 添加组合动画
     */
    public void startAnimationSet() {
        AnimationSet animationSet = new AnimationSet();
        animationSet.addAnimation(getAlphaAnimation());
        animationSet.addAnimation(getRotateAnimation());
        animationSet.addAnimation(getSingleScaleAnimation());
        animationSet.addAnimation(getScaleAnimation());
        animationSet.setAnimatorSetMode(0);
        animationSet.setInterpolator(new LinearInterpolator());
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });

        mMarkerE.setAnimation(animationSet);
        mMarkerE.startAnimation();
    }

    /**
     * 创建缩放动画
     */
    private Animation getScaleAnimation() {
        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 2f, 1f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setRepeatMode(Animation.RepeatMode.RESTART);// 动画重复模式
        scaleAnimation.setRepeatCount(1);// 动画重复次数
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });
        return scaleAnimation;
    }

    /**
     * 创建旋转动画
     */
    private Animation getRotateAnimation() {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 360f);
        rotateAnimation.setDuration(1000);// 设置动画旋转时间
        rotateAnimation.setRepeatMode(Animation.RepeatMode.RESTART);// 动画重复模式
        rotateAnimation.setRepeatCount(1);// 动画重复次数
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });

        return rotateAnimation;
    }

    /**
     * 创建平移动画
     */
    private Animation getTransformation() {
        LatLng latLngA = new LatLng(40.022211, 116.499274);
        Point point = mBaiduMap.getProjection().toScreenLocation(latLngA);
        LatLng latLngB = mBaiduMap.getProjection().fromScreenLocation(new Point(point.x, point.y - 100));
        Transformation transformation = new Transformation(latLngA, latLngB, latLngA);
        transformation.setDuration(500);
        transformation.setRepeatMode(Animation.RepeatMode.RESTART);// 动画重复模式
        transformation.setRepeatCount(1);// 动画重复次数
        transformation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {

            }
        });

        return transformation;
    }

    /**
     * 创建平移坐标动画
     */
    private Animation getTransformationPoint() {
        if (null != mScreenCenterPoint) {
            Point pointTo = new Point(mScreenCenterPoint.x, mScreenCenterPoint.y - 100);
            Transformation transformation = new Transformation(mScreenCenterPoint, pointTo, mScreenCenterPoint);
            transformation.setDuration(500);
            transformation.setRepeatMode(Animation.RepeatMode.RESTART);// 动画重复模式
            transformation.setRepeatCount(1);// 动画重复次数
            transformation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart() {
                }

                @Override
                public void onAnimationEnd() {
                }

                @Override
                public void onAnimationCancel() {
                }

                @Override
                public void onAnimationRepeat() {

                }
            });
            return transformation;
        }

        return null;
    }

    /**
     * 创建透明度动画
     */
    private Animation getAlphaAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0f, 1f);
        alphaAnimation.setDuration(3000);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.RepeatMode.RESTART);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart() {
            }

            @Override
            public void onAnimationEnd() {
            }

            @Override
            public void onAnimationCancel() {
            }

            @Override
            public void onAnimationRepeat() {
            }
        });

        return alphaAnimation;
    }

    @Override
    protected void onPause() {
        super.onPause();
        // MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMarkerA.cancelAnimation();
        mMarkerB.cancelAnimation();
        mMarkerC.cancelAnimation();
        mMarkerD.cancelAnimation();
        mMarkerE.cancelAnimation();
        mMarkerF.cancelAnimation();
        mMarkerG.cancelAnimation();

        // MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy()
        mMapView.onDestroy();
        markerRemove();
        // 回收 bitmap 资源
        bitmapA.recycle();
        bitmapB.recycle();
        bitmapC.recycle();
        bitmapD.recycle();
        bitmapE.recycle();
        bitmapF.recycle();
        bitmapG.recycle();
    }

    public void markerRemove() {
        mMarkerA.remove();
        mMarkerB.remove();
        mMarkerC.remove();
        mMarkerD.remove();
        mMarkerE.remove();
        mMarkerG.remove();
    }
}
