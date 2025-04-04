package com.tencent.watch.aio_impl.ui.widget;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import momoi.mod.qqpro.lib.MutableState;

public class AIOCellGroupWidget extends FrameLayout {
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
