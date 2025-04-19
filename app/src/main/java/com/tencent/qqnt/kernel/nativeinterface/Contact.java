package com.tencent.qqnt.kernel.nativeinterface;

public class Contact {
    public int chatType;
    public String guildId;
    public String peerUid;

    public Contact(int chatType, String peerUid, String guildId) {
        this.chatType = chatType;
        this.peerUid = peerUid;
        this.guildId = guildId;
    }
}
