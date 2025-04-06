package com.tencent.qqnt.kernel.nativeinterface;

public interface IKernelTicketService {
    void forceFetchClientKey(String str, IClientKeyCallback iClientKeyCallback);
}
