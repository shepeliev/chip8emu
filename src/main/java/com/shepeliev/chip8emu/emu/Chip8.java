package com.shepeliev.chip8emu.emu;

import java.io.IOException;
import java.io.InputStream;

public interface Chip8 {

    void run() throws InterruptedException;

    void load(byte[] application);

    void load(InputStream inputStream) throws IOException;
}
