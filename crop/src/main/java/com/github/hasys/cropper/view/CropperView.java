package com.github.hasys.cropper.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.github.hasys.cropper.R;

public class CropperView extends FrameLayout {

    private final GestureCropImageView mGestureCropImageView;
    private final OverlayView mViewOverlay;

    public CropperView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CropperView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.cropper_view, this, true);
        mGestureCropImageView = findViewById(R.id.image_view_crop);
        mViewOverlay = findViewById(R.id.view_overlay);

        mGestureCropImageView.setCropBoundsChangeListener(new CropImageView.CropBoundsChangeListener() {
            @Override
            public void onCropBoundsChangedRotate(float cropRatio) {
                if (mViewOverlay != null) {
                    mViewOverlay.setTargetAspectRatio(cropRatio);
                    mViewOverlay.postInvalidate();
                }
            }
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.cropper_CropperView);
        mViewOverlay.processStyledAttributes(a);
        mGestureCropImageView.processStyledAttributes(a);
        a.recycle();
    }

    @Override
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    @NonNull
    public GestureCropImageView getCropImageView() {
        return mGestureCropImageView;
    }

    @NonNull
    public OverlayView getOverlayView() {
        return mViewOverlay;
    }

}