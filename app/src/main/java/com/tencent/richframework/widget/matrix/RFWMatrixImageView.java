package com.tencent.richframework.widget.matrix;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RFWMatrixImageView extends AppCompatImageView {
    public float getMaximumScale() {
        return 0f;
    }
    public void setMaximumScale(float f) {

    }
    public RFWMatrixImageView(Context context, AttributeSet attributeSet) {
        super(context);
    }
    public void setScale(float f) {

    }
    public float getScale() {
        return 0f;
    }
    public int getActualHeight() {
        return 0;
    }
    public int getActualWidth() {
        return 0;
    }

    public float getMinimumScale() {
        return 0;
    }

}
