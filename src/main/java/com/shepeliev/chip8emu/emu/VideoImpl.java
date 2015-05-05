package com.shepeliev.chip8emu.emu;

import com.googlecode.lanterna.terminal.Terminal;
import org.springframework.stereotype.Service;

@Service("video")
public class VideoImpl implements Video {

    private static final char        PIXEL_CHARACTER = '*';
    private static final char        EMPTY_CHARACTER = ' ';
    private static final int         WIDTH           = 64;
    private static final int         HEIGHT          = 32;
    private final        boolean[][] memory          = new boolean[HEIGHT][WIDTH];
    private final        Terminal    terminal        = TerminalHelper.TERMINAL_INSTANCE;

    public VideoImpl() {
        terminal.enterPrivateMode();
        terminal.clearScreen();
        terminal.setCursorVisible(false);
    }

    @Override
    public boolean drawSprite(byte x, byte y, byte... sprite) {
        if (x >= WIDTH || x < 0 || y >= HEIGHT || y < 0) {
            return false;
        }
        boolean ret = false;
        int r = y;
        for (int s : sprite) {
            int mask = 0x80;
            for (int c = x; c < WIDTH && mask > 0; c++) {
                boolean oldPixel = memory[r][c];
                boolean newPixel = (s & mask) == mask;
                memory[r][c] = oldPixel ^ newPixel;
                mask >>= 1;
                if (oldPixel && !newPixel) {
                    ret = true;
                }
                terminal.moveCursor(c, r);
                terminal.putCharacter(memory[r][c] ? PIXEL_CHARACTER : EMPTY_CHARACTER);
            }
            r++;
            if (r == HEIGHT) {
                break;
            }
        }
        terminal.flush();
        return ret;
    }

    @Override
    public void clearScreen() {
        terminal.clearScreen();
    }

    @Override
    public void close() {
        terminal.exitPrivateMode();
    }
}
