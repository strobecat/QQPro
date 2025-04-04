package com.tencent.watch.aio_impl.ui.cell.base;

import androidx.fragment.app.Fragment;

import com.tencent.watch.aio_impl.ui.menu.MenuItemFactory$ItemEnum;

import org.jetbrains.annotations.NotNull;

public class WatchAIOGroupWidgetItemCell extends BaseWatchItemCell {
    public boolean l(@NotNull MenuItemFactory$ItemEnum result, @NotNull Fragment fragment) {
        return false;
    }
}
