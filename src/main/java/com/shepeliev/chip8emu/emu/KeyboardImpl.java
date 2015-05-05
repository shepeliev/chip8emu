package com.shepeliev.chip8emu.emu;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.event.KeyEvent;

@Service("keyboard")
public class KeyboardImpl implements Keyboard {

    private byte pressedKey = -1;

    public KeyboardImpl() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent ke) {
                switch (ke.getID()) {
                    case KeyEvent.KEY_PRESSED:
                        synchronized (KeyboardImpl.class) {
                            if (ke.getKeyCode() >= KeyEvent.VK_0 && ke.getKeyCode() <= KeyEvent.VK_9) {
                                pressedKey = (byte) (ke.getKeyCode() - KeyEvent.VK_0);
                            }
                            if (ke.getKeyCode() >= KeyEvent.VK_A && ke.getKeyCode() <= KeyEvent.VK_F) {
                                pressedKey = (byte) (ke.getKeyCode() - KeyEvent.VK_A + 10);
                            }
                            try {
                                Thread.sleep(40);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case KeyEvent.KEY_RELEASED:
                        synchronized (KeyboardImpl.class) {
                            pressedKey = -1;
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean keyIsPressed(byte code) {
        synchronized (KeyboardImpl.class) {
            return pressedKey == code;
        }
    }

    @Override
    public byte waitForKeyPressed() {
        return pressedKey;
    }
}
