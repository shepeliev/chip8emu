package com.shepeliev.chip8emu.emu;

public interface Timer {

    void setTimer(byte value);

    byte getTimerValue();
}
