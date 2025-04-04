package com.tencent.watch.aio_impl.ui.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.tencent.qqnt.watch.ui.componet.AbsItem;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class AIOLongClickMenuFragment extends Fragment {
    @NotNull
    public final Function1<MenuItemFactory$ItemEnum, Unit> b = null;
    public List<? extends AbsItem> d = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //Dismiss
    public void p() {
    }
}
