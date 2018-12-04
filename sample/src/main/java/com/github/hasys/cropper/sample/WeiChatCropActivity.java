package com.github.hasys.cropper.sample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.github.hasys.cropper.Cropper;
import com.github.hasys.cropper.util.BitmapLoadUtils;
import com.github.hasys.cropper.view.CropImageView;
import com.github.hasys.cropper.view.GestureCropImageView;
import com.github.hasys.cropper.view.OverlayView;
import com.github.hasys.cropper.view.TransformImageView;
import com.github.hasys.cropper.view.CropperView;

import java.io.OutputStream;

/**
 * Created by zhouwk on 2016/3/30 0030.
 */
public class WeiChatCropActivity extends AppCompatActivity {

    private static final String TAG = "WeiChatCropActivity";

    CropperView mCropperView;
    GestureCropImageView mGestureCropImageView;
    OverlayView mOverlayView;
    TextView saveTv;

    private Uri mOutputUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weichat_crop);

        initView();
        final Intent intent = getIntent();
        setImageData(intent);
        initEvent();
    }

    private void setImageData(Intent intent) {
        Uri inputUri = intent.getParcelableExtra(Cropper.EXTRA_INPUT_URI);
        mOutputUri = intent.getParcelableExtra(Cropper.EXTRA_OUTPUT_URI);

        if (inputUri != null && mOutputUri != null) {
            try {
                mGestureCropImageView.setImageUri(inputUri);
            } catch (Exception e) {
                setResultException(e);
                finish();
            }
        } else {
            setResultException(new NullPointerException("Both input and output Uri must be specified"));
            finish();
        }

        // 设置裁剪宽高比
        if (intent.getBooleanExtra(Cropper.EXTRA_ASPECT_RATIO_SET, false)) {
            float aspectRatioX = intent.getFloatExtra(Cropper.EXTRA_ASPECT_RATIO_X, 0);
            float aspectRatioY = intent.getFloatExtra(Cropper.EXTRA_ASPECT_RATIO_Y, 0);

            if (aspectRatioX > 0 && aspectRatioY > 0) {
                mGestureCropImageView.setTargetAspectRatio(aspectRatioX / aspectRatioY);
            } else {
                mGestureCropImageView.setTargetAspectRatio(CropImageView.SOURCE_IMAGE_ASPECT_RATIO);
            }
        }

        // 设置裁剪的最大宽高
        if (intent.getBooleanExtra(Cropper.EXTRA_MAX_SIZE_SET, false)) {
            int maxSizeX = intent.getIntExtra(Cropper.EXTRA_MAX_SIZE_X, 0);
            int maxSizeY = intent.getIntExtra(Cropper.EXTRA_MAX_SIZE_Y, 0);

            if (maxSizeX > 0 && maxSizeY > 0) {
                mGestureCropImageView.setMaxResultImageSizeX(maxSizeX);
                mGestureCropImageView.setMaxResultImageSizeY(maxSizeY);
            } else {
                Log.w(TAG, "EXTRA_MAX_SIZE_X and EXTRA_MAX_SIZE_Y must be greater than 0");
            }
        }
    }

    private void initView() {
        mCropperView = (CropperView) this.findViewById(R.id.weixin_act_cropper);
        saveTv = (TextView) this.findViewById(R.id.weixin_act_crop_tv_save);
        mGestureCropImageView = mCropperView.getCropImageView();
        mOverlayView = mCropperView.getOverlayView();

        // 设置允许缩放
        mGestureCropImageView.setScaleEnabled(true);
        // 设置禁止旋转
        mGestureCropImageView.setRotateEnabled(false);
        // 设置剪切后的最大宽度
//        mGestureCropImageView.setMaxResultImageSizeX(300);
        // 设置剪切后的最大高度
//        mGestureCropImageView.setMaxResultImageSizeY(300);

        // 设置外部阴影颜色
        mOverlayView.setDimmedColor(Color.parseColor("#AA000000"));
        // 设置周围阴影是否为椭圆(如果false则为矩形)
        mOverlayView.setOvalDimmedLayer(false);
        // 设置显示裁剪边框
        mOverlayView.setShowCropFrame(true);
        // 设置不显示裁剪网格
        mOverlayView.setShowCropGrid(false);
    }

    private void initEvent() {
        mGestureCropImageView.setTransformImageListener(mImageListener);
        saveTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cropAndSaveImage();
            }
        });
    }

    private void cropAndSaveImage() {
        OutputStream outputStream = null;
        try {
            final Bitmap croppedBitmap = mGestureCropImageView.cropImage();
            if (croppedBitmap != null) {
                outputStream = getContentResolver().openOutputStream(mOutputUri);
                croppedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream);
                croppedBitmap.recycle();

                setResultUri(mOutputUri, mGestureCropImageView.getTargetAspectRatio());
                finish();
            } else {
                setResultException(new NullPointerException("CropImageView.cropImage() returned null."));
            }
        } catch (Exception e) {
            setResultException(e);
            finish();
        } finally {
            BitmapLoadUtils.close(outputStream);
        }
    }

    private TransformImageView.TransformImageListener mImageListener = new TransformImageView.TransformImageListener() {
        @Override
        public void onRotate(float currentAngle) {
//            setAngleText(currentAngle);
        }

        @Override
        public void onScale(float currentScale) {
//            setScaleText(currentScale);
        }

        @Override
        public void onLoadComplete() {
            Animation fadeInAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.cropper_fade_in);
            fadeInAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    mCropperView.setVisibility(View.VISIBLE);
                    mGestureCropImageView.setImageToWrapCropBounds();
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }
            });
            mCropperView.startAnimation(fadeInAnimation);
        }

        @Override
        public void onLoadFailure(Exception e) {
            setResultException(e);
            finish();
        }

    };

    private void setResultUri(Uri uri, float resultAspectRatio) {
        setResult(RESULT_OK, new Intent()
                .putExtra(Cropper.EXTRA_OUTPUT_URI, uri)
                .putExtra(Cropper.EXTRA_OUTPUT_CROP_ASPECT_RATIO, resultAspectRatio));
    }

    private void setResultException(Throwable throwable) {
        setResult(Cropper.RESULT_ERROR, new Intent().putExtra(Cropper.EXTRA_ERROR, throwable));
    }
}
