package com.yakin.rtp.processor;

import com.yakin.rtp.IRTPGrantHandler;

public interface IProcessor {

    void runPermission(IRTPGrantHandler callback);
}
