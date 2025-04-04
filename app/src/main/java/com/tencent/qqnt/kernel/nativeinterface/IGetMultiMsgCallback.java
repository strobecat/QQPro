package com.tencent.qqnt.kernel.nativeinterface;

import java.util.ArrayList;

public interface IGetMultiMsgCallback {
    void onResult(int i, String str, ArrayList<MsgRecord> arrayList);
}
