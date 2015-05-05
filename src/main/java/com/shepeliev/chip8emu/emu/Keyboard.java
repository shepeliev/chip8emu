package com.shepeliev.chip8emu.emu;

public interface Keyboard {

    boolean keyIsPressed(byte code);

    byte waitForKeyPressed();
}
