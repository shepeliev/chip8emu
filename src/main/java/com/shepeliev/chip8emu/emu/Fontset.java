package com.shepeliev.chip8emu.emu;

public final class FontSet {

    public static final byte[] FONT = {(byte) 0xF0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xF0, // 0
                                       0x20, 0x60, 0x20, 0x20, 0x70, // 1
                                       (byte) 0xF0, 0x10, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // 2
                                       (byte) 0xF0, 0x10, (byte) 0xF0, 0x10, (byte) 0xF0, // 3
                                       (byte) 0x90, (byte) 0x90, (byte) 0xF0, 0x10, 0x10, // 4
                                       (byte) 0xF0, (byte) 0x80, (byte) 0xF0, 0x10, (byte) 0xF0, // 5
                                       (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 6
                                       (byte) 0xF0, 0x10, 0x20, 0x40, 0x40, // 7
                                       (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0xF0, // 8
                                       (byte) 0xF0, (byte) 0x90, (byte) 0xF0, 0x10, (byte) 0xF0, // 9
                                       (byte) 0xF0, (byte) 0x90, (byte) 0xF0, (byte) 0x90, (byte) 0x90, // A
                                       (byte) 0xE0, (byte) 0x90, (byte) 0xE0, (byte) 0x90, (byte) 0xE0, // B
                                       (byte) 0xF0, (byte) 0x80, (byte) 0x80, (byte) 0x80, (byte) 0xF0, // C
                                       (byte) 0xE0, (byte) 0x90, (byte) 0x90, (byte) 0x90, (byte) 0xE0, // D
                                       (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0xF0, // E
                                       (byte) 0xF0, (byte) 0x80, (byte) 0xF0, (byte) 0x80, (byte) 0x80  // F
    };

    private FontSet() {
    }
}
