package com.example.test8;

import java.io.IOException;


import java.util.List;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Toast;
import android.text.format.Time;

import android.util.DisplayMetrics;

import android.content.Context;
import android.content.Intent;

public class Camera_Preview extends SurfaceView implements SurfaceHolder.Callback{
	Camera_Activity ca;
	
	private static final String LOG_TAG = "CameraPreviewSample";
    private Activity mActivity;
    private SurfaceHolder mHolder;
    private Camera mCamera;

    public Camera_Preview(Camera_Activity activity) {
        super(activity); // Always necessary
        ca=activity;
        mActivity = activity;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        this.setOnClickListener(onClickListener);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (null == mCamera) {
            mCamera = Camera.open();
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();

        Log.v(LOG_TAG, "Desired Preview Size - w: " + width + ", h: " + height);

        Parameters mParam = mCamera.getParameters();

        // Set orientation
        boolean portrait = isPortrait();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.FROYO) {
            // 2.1 and before
            if (portrait) {
                mParam.set("orientation", "portrait");
            } else {
                mParam.set("orientation", "landscape");
            }
        } else {
            // 2.2 and later
            if (portrait) {
                mCamera.setDisplayOrientation(90);
            } else {
                mCamera.setDisplayOrientation(0);
            }
        }

        // Set width & height
        int previewWidth = width;
        int previewHeight = height;
        // Meaning of width and height is switched for preview when portrait,
        // while it is the same as user's view for surface and metrics.
        // That is, width must always be larger than height for setPreviewSize.
        if (portrait) {
            previewWidth = height;
            previewHeight = width;
        }

        // Actual preview size will be one of the sizes obtained by getSupportedPreviewSize.
        // It is the one that has the largest width among the sizes of which both width and
        // height no larger than given size in setPreviewSize.
        List<Size> sizes = mParam.getSupportedPreviewSizes();
        int tmpHeight = 0;
        int tmpWidth = 0;
        for (Size size : sizes) {
            if ((size.width > previewWidth) || (size.height > previewHeight)) {
                continue;
            }
            if (tmpHeight < size.height) {
                tmpWidth = size.width;
                tmpHeight = size.height;
            }
        }
        previewWidth = tmpWidth;
        previewHeight = tmpHeight;
        
        // Only for debugging
        for (Size size : sizes) {
            Log.v(LOG_TAG, "w: " + size.width + ", h: " + size.height);
        }
        
        mParam.setPreviewSize(previewWidth, previewHeight);

        Log.v(LOG_TAG, "Preview Actual Size - w: " + previewWidth + ", h: " + previewHeight);

        // Adjust SurfaceView size
        float layoutHeight, layoutWidth;
        if (portrait) {
            layoutHeight = previewWidth;
            layoutWidth = previewHeight;
        } else {
            layoutHeight = previewHeight;
            layoutWidth = previewWidth;
        }

        float factH, factW, fact;
        factH = height / layoutHeight;
        factW = width / layoutWidth;
        // Select smaller factor, because the surface cannot be set to the size larger than display metrics.
        if (factH < factW) {
            fact = factH;
        } else {
            fact = factW;
        }
        ViewGroup.LayoutParams layoutParams = this.getLayoutParams();
        layoutParams.height = (int)(layoutHeight * fact);
        layoutParams.width = (int)(layoutWidth * fact);
        this.setLayoutParams(layoutParams);

        Log.v(LOG_TAG, "Preview Layout Size - w: " + layoutParams.width + ", h: " + layoutParams.height);
        
        mCamera.setParameters(mParam);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (null == mCamera) {
            return;
        }
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

    protected boolean isPortrait() {
        return (ca.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
    }
    
    private OnClickListener onClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// オートフォーカス
			if (mCamera != null) {
				mCamera.autoFocus(autoFocusCallback);
			}
		}
	};
	
	private AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {
		@Override
		public void onAutoFocus(boolean success, Camera camera) {
			if (success) {
				// 現在のプレビューをデータに変換
				camera.setOneShotPreviewCallback(previewCallback);
			}
		}
	};
	
	private PreviewCallback previewCallback = new PreviewCallback() {
		@Override
		public void onPreviewFrame(byte[] data, Camera camera) {
			// TODO バーコード読み取り
			// 読み込む範囲
			int previewWidth = camera.getParameters().getPreviewSize().width;
			int previewHeight = camera.getParameters().getPreviewSize().height;

			// プレビューデータから BinaryBitmap を生成 
			PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(
					data
					,previewWidth
					,previewHeight
					,0
					,0
					,previewWidth, previewHeight, false
					);

			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			// バーコードを読み込む
			Reader reader = new MultiFormatReader();
			Result result = null;
			try {
				result = reader.decode(bitmap);
				String text = result.getText();
				Toast.makeText(ca, text, Toast.LENGTH_LONG).show();
				ca.checkId(text);
//				ca.moveActivity();
			} catch (Exception e) {
				Toast.makeText(ca, "Not Found", Toast.LENGTH_SHORT).show();
				
			}
		}
	};
	

}
