package com.tencent.qqnt.watch.ui.componet;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbsItem implements View.OnClickListener {
    @Nullable
    private final String elementId;
    public AbsItem(@Nullable String str) {
        this.elementId = str;
    }
    @NotNull
    public View createView(@NotNull Context context, @NotNull ViewGroup parent) {
        return null;
    }
    public abstract int getIconResId();

    public abstract int getText();
}
