/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package com.clyr.three_sdk.baidumapsdk.demo.geometry;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapDrawFrameCallback;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.clyr.three_sdk.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.opengles.GL10;



/**
 * 此demo用来展示如何在地图绘制的每帧中再额外绘制一些用户自己的内容
 */
public class OpenglDemo extends AppCompatActivity implements OnMapDrawFrameCallback {

    private static final String LTAG = OpenglDemo.class.getSimpleName();

    // 地图相关
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private LatLng latlng1 = new LatLng(39.97923, 116.357428);
    private LatLng latlng2 = new LatLng(39.94923, 116.397428);
    private int mProgramObject;

    private int mTexID;

    // 2D矩形纹理坐标
    private final short[] mTexCoordsData = {
            0, 1,   // top left
            1, 1,   // top right
            0, 0,   // bottom left
            1, 0    // bottom right
    };

    // 标识是否第一次绘制2D矩形
    private boolean mIsFirstDraw2DRectangle = true;

    // 标识是否第一次绘制3D立方体
    private boolean mIsFirstDraw3DCube = true;

    // 3D立方体顶点绘制顺序列表
    private short[] mDrawIndices = {
            0, 4, 5, 0, 5, 1, 1, 5, 6, 1, 6, 2, 2, 6, 7, 2, 7, 3, 3, 7, 4, 3, 4, 0, 4, 7, 6, 4, 6, 5, 3, 0, 1, 3, 1, 2
    };

    // 3D立方体8个顶点颜色值
    private float[] mVertexColors = {
            1f, 1f, 0f, 1f,
            0f, 1f, 1f, 1f,
            1f, 0f, 1f, 1f,
            0f, 0f, 0f, 1f,
            1f, 1f, 1f, 1f,
            1f, 0f, 0f, 1f,
            0f, 1f, 0f, 1f,
            0f, 0f, 1f, 1f
    };

    // 立方体顶点坐标Buffer
    private FloatBuffer mVertextBuffer;

    // 顶点绘制顺序Buffer
    private ShortBuffer mIndexBuffer;

    // 立方体顶点颜色Buffer
    private FloatBuffer mColorBuffer;

    // 3D立方体着色器
    private CubeShader mCubeShader;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opengl);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();

        mBaiduMap.setOnMapDrawFrameCallback(this);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latlng1).zoom(14.0f).overlook(-45.0f);
        MapStatusUpdate msu = MapStatusUpdateFactory.newMapStatus(builder.build());
        mBaiduMap.animateMapStatus(msu);

        mBaiduMap.getUiSettings().setOverlookingGesturesEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();

        mIsFirstDraw2DRectangle = true;
        mIsFirstDraw3DCube = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Deprecated
    public void onMapDrawFrame(GL10 gl, MapStatus drawingMapStatus) {

    }

    @Override
    public void onMapDrawFrame(MapStatus drawingMapStatus) {
        if (null == mBaiduMap.getProjection()) {
            return;
        }

        // 绘制2D 纹理矩形
        drawFrameFor2DRectangle(drawingMapStatus);

        // 绘制3D立方体
        drawFrameFor3DCube(drawingMapStatus, 0.2f, 0.2f, 0.3f);
    }

    private void drawFrameFor2DRectangle(MapStatus drawingMapStatus) {
        // 采用屏幕坐标， 有抖动，有累计误差
        PointF openGLPoint1 = mBaiduMap.getProjection().toOpenGLNormalization(latlng1, drawingMapStatus);
        PointF openGlPoint2 = mBaiduMap.getProjection().toOpenGLNormalization(latlng2, drawingMapStatus);

        // 矩形顶点坐标数据
        float verticesData[] = new float[]{
                openGLPoint1.x, openGLPoint1.y, 0.0f,
                openGlPoint2.x, openGLPoint1.y, 0.0f,
                openGLPoint1.x, openGlPoint2.y, 0.0f,
                openGlPoint2.x, openGlPoint2.y, 0.0f
        };

        FloatBuffer verticesBuffer = ByteBuffer.allocateDirect(verticesData.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        verticesBuffer.put(verticesData).position(0);

        ShortBuffer texCoords = ByteBuffer.allocateDirect(mTexCoordsData.length * 2)
                .order(ByteOrder.nativeOrder())
                .asShortBuffer();
        texCoords.put(mTexCoordsData).position(0);

        if (mIsFirstDraw2DRectangle) {
            comipleShaderAndLinkProgram();
            loadTexture();
            mIsFirstDraw2DRectangle = false;
        }

        GLES20.glUseProgram(mProgramObject);

        GLES20.glVertexAttribPointer(0, 3, GLES20.GL_FLOAT, false, 0, verticesBuffer);
        GLES20.glEnableVertexAttribArray(0);

        GLES20.glVertexAttribPointer(1, 2, GLES20.GL_SHORT, false, 0, texCoords);
        GLES20.glEnableVertexAttribArray(1);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTexID);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
    }


    private void comipleShaderAndLinkProgram() {
        final String vShaderStr = "attribute vec4 a_position;\n"
                + "attribute vec2 a_texCoords;\n"
                + "varying vec2 v_texCoords;\n"
                + "void main()\n"
                + "{\n"
                + "gl_Position = a_position;\n"
                + "v_texCoords = a_texCoords;\n"
                + "}\n";

        final String fShaderStr = "precision mediump float;\n"
                + "uniform sampler2D u_Texture;\n"
                + "varying vec2 v_texCoords;\n"
                + "void main()\n"
                + "{\n"
                + "gl_FragColor = texture2D(u_Texture, v_texCoords);\n"
                + "}\n";

        int[] linked = new int[1];
        // Load the vertex/fragment shaders
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vShaderStr);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fShaderStr);
        // Create the program object
        int programObject = GLES20.glCreateProgram();
        if (programObject == 0) {
            return;
        }

        GLES20.glAttachShader(programObject, vertexShader);
        GLES20.glAttachShader(programObject, fragmentShader);

        // Bind vPosition to attribute 0
        GLES20.glBindAttribLocation(programObject, 0, "a_position");
        GLES20.glBindAttribLocation(programObject, 1, "a_texCoords");

        // Link the program
        GLES20.glLinkProgram(programObject);
        // Check the link status
        GLES20.glGetProgramiv(programObject, GLES20.GL_LINK_STATUS, linked, 0);

        if (linked[0] == 0) {
            Log.e(LTAG, "Error linking program:");
            Log.e(LTAG, GLES20.glGetProgramInfoLog(programObject));
            GLES20.glDeleteProgram(programObject);
            return;
        }

        mProgramObject = programObject;
    }

    private int loadShader(int shaderType, String shaderSource) {
        int shader;
        int[] compiled = new int[1];
        // Create the shader object
        shader = GLES20.glCreateShader(shaderType);
        if (shader == 0) {
            return 0;
        }

        // Load the shader source
        GLES20.glShaderSource(shader, shaderSource);
        // Compile the shader
        GLES20.glCompileShader(shader);
        // Check the compile status
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] == 0) {
            Log.e(LTAG, GLES20.glGetShaderInfoLog(shader));
            GLES20.glDeleteShader(shader);
            return 0;
        }

        return shader;
    }

    private void loadTexture() {
        Bitmap textureBitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ground_overlay);
        if (null == textureBitmap) {
            return;
        }

        int[] texID = new int[1];
        GLES20.glGenTextures(1, texID, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texID[0]);
        mTexID = texID[0];

        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);

        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);

        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, textureBitmap, 0);

        textureBitmap.recycle();
    }

    private void drawFrameFor3DCube(MapStatus drawingMapStatus, float width, float height, float depth) {
        if (null == drawingMapStatus) {
            return;
        }

        if (mIsFirstDraw3DCube) {
            initCubeModelData(width, height, depth);

            initCubeShader();

            mIsFirstDraw3DCube = false;
        }

        drawCube(drawingMapStatus);
    }

    private void initCubeModelData(float width, float height, float depth) {
        // 对标墨卡托坐标
        width = width * 10000 / 2;
        height = height * 10000 / 2;
        depth = depth * 10000 / 2;

        // 立方体8个顶点坐标
        float[] vertices = {
                -width, -height, -0,
                width, -height, -0,
                width, height, -0,
                -width, height, -0,
                -width, -height, depth,
                width, -height, depth,
                width, height, depth,
                -width, height, depth,
        };

        mVertextBuffer = ByteBuffer.allocateDirect(vertices.length * 4)
                .order(ByteOrder.nativeOrder())
                .asFloatBuffer();
        mVertextBuffer.put(vertices).position(0);

        // 立方体顶点绘制顺序Buffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(mDrawIndices.length * 4);
        byteBuffer.order(ByteOrder.nativeOrder());
        mIndexBuffer = byteBuffer.asShortBuffer();
        mIndexBuffer.put(mDrawIndices);
        mIndexBuffer.position(0);

        // 立方体顶点颜色Buffer
        ByteBuffer byteBuffer1 = ByteBuffer.allocateDirect(mVertexColors.length * 4);
        byteBuffer1.order(ByteOrder.nativeOrder());
        mColorBuffer = byteBuffer1.asFloatBuffer();
        mColorBuffer.put(mVertexColors);
        mColorBuffer.position(0);
    }

    private void initCubeShader() {
        mCubeShader = new CubeShader();
        mCubeShader.init();
    }

    private void drawCube(MapStatus drawingMapStatus) {
        if (null == mCubeShader || null == drawingMapStatus) {
            return;
        }

        // Step1 初始化数据
        float[] mvpMatrix = new float[16];
        Matrix.setIdentityM(mvpMatrix, 0);

        // 获取投影矩阵
        float[] projectMatrix = mBaiduMap.getProjectionMatrix();
        // 获取视图矩阵
        float[] viewMatrix = mBaiduMap.getViewMatrix();

        Matrix.multiplyMM(mvpMatrix, 0, projectMatrix, 0, viewMatrix, 0);

        // 绑定地图移动
        PointF p1f = mBaiduMap.getProjection().toOpenGLLocation(latlng1, drawingMapStatus);
        Matrix.translateM(mvpMatrix, 0, p1f.x, p1f.y, 0);

        // 设置缩放比例
        int scale = 1;
        Matrix.scaleM(mvpMatrix, 0, scale, scale, scale);

        // Step2 开始绘制设置
        GLES20.glUseProgram(mCubeShader.mProgram);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // 顶点指针
        GLES20.glEnableVertexAttribArray(mCubeShader.mVertex);
        GLES20.glVertexAttribPointer(mCubeShader.mVertex, 3, GLES20.GL_FLOAT, false, 0, mVertextBuffer);

        // 颜色指针
        GLES20.glEnableVertexAttribArray(mCubeShader.mColor);
        GLES20.glVertexAttribPointer(mCubeShader.mColor, 4, GLES20.GL_FLOAT, false, 0, mColorBuffer);

        GLES20.glUniformMatrix4fv(mCubeShader.mMvpMatrix, 1, false, mvpMatrix, 0);

        // 开始画
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawIndices.length, GLES20.GL_UNSIGNED_SHORT, mIndexBuffer);
        GLES20.glDisableVertexAttribArray(mCubeShader.mVertex);

        GLES20.glDisable(GLES20.GL_DEPTH_TEST);
    }

    /**
     * Convert width to openGL width
     *
     * @param width
     * @return Width in openGL
     */
    public static float toGLWidth2(float width, MapStatus mapStatus) {
        float winRoundW = (float) Math.abs(mapStatus.winRound.right - mapStatus.winRound.left);

        return 2 * width / winRoundW - 1;
    }

    /**
     * Convert height to openGL height
     *
     * @param height
     * @return Height in openGL
     */
    public static float toGLHeight2(float height, MapStatus mapStatus) {
        float winRoundH = (float) Math.abs(mapStatus.winRound.top - mapStatus.winRound.bottom);

        return 1 - 2 * height / winRoundH;
    }

    private class CubeShader {
        int mVertex;
        int mMvpMatrix;
        int mColor;
        int mProgram;

        public CubeShader() {

        }

        String vertexShader = "precision highp float;\n" +
                "        attribute vec3 mVertex;//顶点数组,三维坐标\n" +
                "        attribute vec4 mColor;//颜色数组,三维坐标\n" +
                "        uniform mat4 mMvpMatrix;//mvp矩阵\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_Position = mMvpMatrix * vec4(mVertex, 1.0);\n" +
                "            color = mColor;\n" +
                "        }";

        String fragmentShader = "//有颜色 没有纹理\n" +
                "        precision highp float;\n" +
                "        varying vec4 color;//\n" +
                "        void main(){\n" +
                "            gl_FragColor = color;\n" +
                "        }";

        public void init() {
            int vertexLocation = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
            GLES20.glShaderSource(vertexLocation, vertexShader);
            GLES20.glCompileShader(vertexLocation);

            int fragmentLocation = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
            GLES20.glShaderSource(fragmentLocation, fragmentShader);
            GLES20.glCompileShader(fragmentLocation);

            mProgram = GLES20.glCreateProgram();
            GLES20.glAttachShader(mProgram, vertexLocation);
            GLES20.glAttachShader(mProgram, fragmentLocation);
            GLES20.glLinkProgram(mProgram);

            mVertex = GLES20.glGetAttribLocation(mProgram, "mVertex");
            mMvpMatrix = GLES20.glGetUniformLocation(mProgram, "mMvpMatrix");
            mColor = GLES20.glGetAttribLocation(mProgram, "mColor");
        }
    }
}
