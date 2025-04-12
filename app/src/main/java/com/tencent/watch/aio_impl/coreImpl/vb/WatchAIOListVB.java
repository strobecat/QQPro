package com.tencent.watch.aio_impl.coreImpl.vb;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.tencent.aio.api.list.IListUIOperationApi;
import com.tencent.aio.base.mvi.part.MsgListUiState;
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.vb.core.MsgListVBWrapper;
import com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.vb.ui.adapter.inner.IMsgListBuild;
import com.tencent.mvi.api.help.CreateViewParams;

import org.jetbrains.annotations.NotNull;

public class WatchAIOListVB extends MsgListVBWrapper implements IMsgListBuild {
    public RecyclerView H;
    @NotNull
    public View h(@NotNull CreateViewParams createViewParams, @NotNull View childView, @NotNull IListUIOperationApi uiHelper) {
        return null;
    }
    public void n(@NotNull MsgListUiState state, @NotNull IListUIOperationApi uiHelper) {
    }

}
