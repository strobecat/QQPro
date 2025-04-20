package com.tencent.aio.part.root.panel.content.firstLevel.msglist.mvx.state;

import android.os.Bundle;

import com.tencent.aio.base.mvi.part.MsgListUiState;
import com.tencent.aio.data.msglist.IMsgItem;
import com.tencent.watch.aio_impl.data.WatchAIOMsgItem;

import java.util.Collection;
import java.util.LinkedList;

public class MsgListState extends LinkedList<WatchAIOMsgItem> implements MsgListUiState {
    public long b;
    public int c;
    public Bundle d;
    public MsgListState() {

    }
    public MsgListState(long b, Collection<IMsgItem> collection, int c, Bundle d) {

    }
}
