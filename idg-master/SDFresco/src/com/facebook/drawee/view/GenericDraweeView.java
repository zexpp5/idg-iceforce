package com.facebook.drawee.view;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.AutoRotateDrawable;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.drawee.view.AspectRatioMeasure.Spec;


public class GenericDraweeView extends DraweeView<GenericDraweeHierarchy> {
    private float mAspectRatio = 0.0F;
    private final Spec mMeasureSpec = new Spec();

    public GenericDraweeView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context);
        this.setHierarchy(hierarchy);
    }

    public GenericDraweeView(Context context) {
        super(context);
        this.inflateHierarchy(context, (AttributeSet)null);
    }

    public GenericDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.inflateHierarchy(context, attrs);
    }

    public GenericDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.inflateHierarchy(context, attrs);
    }

    private void inflateHierarchy(Context context, @Nullable AttributeSet attrs) {
        Resources resources = context.getResources();
        int fadeDuration = 300;
        int placeholderId = 0;
        ScalingUtils.ScaleType placeholderScaleType = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        int retryImageId = 0;
        ScalingUtils.ScaleType retryImageScaleType = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        int failureImageId = 0;
        ScalingUtils.ScaleType failureImageScaleType = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        int progressBarId = 0;
        ScalingUtils.ScaleType progressBarScaleType = GenericDraweeHierarchyBuilder.DEFAULT_SCALE_TYPE;
        ScalingUtils.ScaleType actualImageScaleType = GenericDraweeHierarchyBuilder.DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        int backgroundId = 0;
        int overlayId = 0;
        int pressedStateOverlayId = 0;
        boolean roundAsCircle = false;
        int roundedCornerRadius = 0;
        boolean roundTopLeft = true;
        boolean roundTopRight = true;
        boolean roundBottomRight = true;
        boolean roundBottomLeft = true;
        int roundWithOverlayColor = 0;
        int roundingBorderWidth = 0;
        int roundingBorderColor = 0;
        int progressBarAutoRotateInterval = 0;
        if(attrs != null) {

            TypedArray builder = context.obtainStyledAttributes(attrs, R.styleable.GenericDraweeView);

            try {
                fadeDuration = builder.getInt(R.styleable.GenericDraweeView_fadeDuration, fadeDuration);
                this.mAspectRatio = builder.getFloat(R.styleable.GenericDraweeView_viewAspectRatio, this.mAspectRatio);
                placeholderId = builder.getResourceId(R.styleable.GenericDraweeView_placeholderImage, placeholderId);
                placeholderScaleType = getScaleTypeFromXml(builder, R.styleable.GenericDraweeView_placeholderImageScaleType, placeholderScaleType);
                retryImageId = builder.getResourceId(R.styleable.GenericDraweeView_retryImage, retryImageId);
                retryImageScaleType = getScaleTypeFromXml(builder, R.styleable.GenericDraweeView_retryImageScaleType, retryImageScaleType);
                failureImageId = builder.getResourceId(R.styleable.GenericDraweeView_failureImage, failureImageId);
                failureImageScaleType = getScaleTypeFromXml(builder, R.styleable.GenericDraweeView_failureImageScaleType, failureImageScaleType);
                progressBarId = builder.getResourceId(R.styleable.GenericDraweeView_progressBarImage, progressBarId);
                progressBarScaleType = getScaleTypeFromXml(builder, R.styleable.GenericDraweeView_progressBarImageScaleType, progressBarScaleType);
                progressBarAutoRotateInterval = builder.getInteger(R.styleable.GenericDraweeView_progressBarAutoRotateInterval, 0);
                actualImageScaleType = getScaleTypeFromXml(builder, R.styleable.GenericDraweeView_actualImageScaleType, actualImageScaleType);
                backgroundId = builder.getResourceId(R.styleable.GenericDraweeView_backgroundImage, backgroundId);
                overlayId = builder.getResourceId(R.styleable.GenericDraweeView_overlayImage, overlayId);
                pressedStateOverlayId = builder.getResourceId(R.styleable.GenericDraweeView_pressedStateOverlayImage, pressedStateOverlayId);
                roundAsCircle = builder.getBoolean(R.styleable.GenericDraweeView_roundAsCircle, roundAsCircle);
                roundedCornerRadius = builder.getDimensionPixelSize(R.styleable.GenericDraweeView_roundedCornerRadius, roundedCornerRadius);
                roundTopLeft = builder.getBoolean(R.styleable.GenericDraweeView_roundTopLeft, roundTopLeft);
                roundTopRight = builder.getBoolean(R.styleable.GenericDraweeView_roundTopRight, roundTopRight);
                roundBottomRight = builder.getBoolean(R.styleable.GenericDraweeView_roundBottomRight, roundBottomRight);
                roundBottomLeft = builder.getBoolean(R.styleable.GenericDraweeView_roundBottomLeft, roundBottomLeft);
                roundWithOverlayColor = builder.getColor(R.styleable.GenericDraweeView_roundWithOverlayColor, roundWithOverlayColor);
                roundingBorderWidth = builder.getDimensionPixelSize(R.styleable.GenericDraweeView_roundingBorderWidth, roundingBorderWidth);
                roundingBorderColor = builder.getColor(R.styleable.GenericDraweeView_roundingBorderColor, roundingBorderColor);
            } finally {
                builder.recycle();
            }
        }

        GenericDraweeHierarchyBuilder builder1 = new GenericDraweeHierarchyBuilder(resources);
        builder1.setFadeDuration(fadeDuration);
        if(placeholderId > 0) {
            builder1.setPlaceholderImage(resources.getDrawable(placeholderId), placeholderScaleType);
        }

        if(retryImageId > 0) {
            builder1.setRetryImage(resources.getDrawable(retryImageId), retryImageScaleType);
        }

        if(failureImageId > 0) {
            builder1.setFailureImage(resources.getDrawable(failureImageId), failureImageScaleType);
        }

        if(progressBarId > 0) {
            Object roundingParams = resources.getDrawable(progressBarId);
            if(progressBarAutoRotateInterval > 0) {
                roundingParams = new AutoRotateDrawable((Drawable)roundingParams, progressBarAutoRotateInterval);
            }

            builder1.setProgressBarImage((Drawable)roundingParams, progressBarScaleType);
        }

        if(backgroundId > 0) {
            builder1.setBackground(resources.getDrawable(backgroundId));
        }

        if(overlayId > 0) {
            builder1.setOverlay(resources.getDrawable(overlayId));
        }

        if(pressedStateOverlayId > 0) {
            builder1.setPressedStateOverlay(this.getResources().getDrawable(pressedStateOverlayId));
        }

        builder1.setActualImageScaleType(actualImageScaleType);
        if(roundAsCircle || roundedCornerRadius > 0) {
            RoundingParams roundingParams1 = new RoundingParams();
            roundingParams1.setRoundAsCircle(roundAsCircle);
            if(roundedCornerRadius > 0) {
                roundingParams1.setCornersRadii(roundTopLeft?(float)roundedCornerRadius:0.0F, roundTopRight?(float)roundedCornerRadius:0.0F, roundBottomRight?(float)roundedCornerRadius:0.0F, roundBottomLeft?(float)roundedCornerRadius:0.0F);
            }

            if(roundWithOverlayColor != 0) {
                roundingParams1.setOverlayColor(roundWithOverlayColor);
            }

            if(roundingBorderColor != 0 && roundingBorderWidth > 0) {
                roundingParams1.setBorder(roundingBorderColor, (float)roundingBorderWidth);
            }

            builder1.setRoundingParams(roundingParams1);
        }

        this.setHierarchy(builder1.build());
    }

    private static ScalingUtils.ScaleType getScaleTypeFromXml(TypedArray attrs, int attrId, ScalingUtils.ScaleType defaultScaleType) {
        String xmlType = attrs.getString(attrId);

        return xmlType != null?ScalingUtils.ScaleType.fromString(xmlType):defaultScaleType;
    }

    public void setAspectRatio(float aspectRatio) {
        if(aspectRatio != this.mAspectRatio) {
            this.mAspectRatio = aspectRatio;
            this.requestLayout();
        }
    }

    public float getAspectRatio() {
        return this.mAspectRatio;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.mMeasureSpec.width = widthMeasureSpec;
        this.mMeasureSpec.height = heightMeasureSpec;
        AspectRatioMeasure.updateMeasureSpec(this.mMeasureSpec, this.mAspectRatio, this.getLayoutParams(), this.getPaddingLeft() + this.getPaddingRight(), this.getPaddingTop() + this.getPaddingBottom());
        super.onMeasure(this.mMeasureSpec.width, this.mMeasureSpec.height);
    }
}
