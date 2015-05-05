package com.shepeliev.chip8emu.emu;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.terminal.Terminal;

public final class TerminalHelper {

    public static final Terminal TERMINAL_INSTANCE = TerminalFacade.createSwingTerminal(64, 32);

    private TerminalHelper() {
    }
}
