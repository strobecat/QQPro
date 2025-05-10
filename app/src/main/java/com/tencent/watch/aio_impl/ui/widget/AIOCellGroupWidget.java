package com.tencent.watch.aio_impl.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

public class AIOCellGroupWidget extends FrameLayout {
    /**
     * nickContentDistance
     */
    public final int g = 0;
    /**
     * formatDateTime
     */
    public CharSequence f;

    public AIOCellGroupWidget(@NonNull Context context) {
        super(context);
    }

    public View getContentWidget() {
        return null;
    }
    public View getNickWidget() { return null; }

    private static final int LOCATION_OTHER = 0;
    public int getLocationType() {
        return 0;
    }
}
