package com.shepeliev.chip8emu.emu;

public interface Video {

    boolean drawSprite(byte x, byte y, byte... sprite);

    void clearScreen();

    void close();
}
