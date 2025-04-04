package com.tencent.watch.aio_impl.ui.cell.base;

import android.view.View;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.tencent.watch.aio_impl.data.WatchAIOMsgItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BaseWatchItemCell {
    public WatchAIOMsgItem f() { throw new RuntimeException(); }
    public void i(
            View view,
            WatchAIOMsgItem item,
            int p3,
            List<?> p4,
            Lifecycle p5,
            LifecycleOwner p6
    ) {
    }

    @Nullable
    public final LifecycleOwner h() {
        return null;
    }

    public void reply_item(@Nullable View view, @NotNull WatchAIOMsgItem watchAIOMsgItem) {}
}
