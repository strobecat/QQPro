package com.tencent.qqnt.msg.api.impl;

import com.tencent.qqnt.kernel.nativeinterface.IGetMsgSeqCallback;
import com.tencent.qqnt.kernel.nativeinterface.IMsgOperateCallback;
import com.tencent.qqnt.kernel.nativeinterface.Contact;
import com.tencent.qqnt.msg.api.IMsgService;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MsgServiceImpl implements IMsgService {
    public void getSingleMsg(Contact contact, long j, IMsgOperateCallback callback) {}
    public void getFirstUnreadMsgSeq(Contact contact, IGetMsgSeqCallback callback) {}

}
