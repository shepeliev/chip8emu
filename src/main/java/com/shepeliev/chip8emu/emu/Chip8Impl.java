package com.shepeliev.chip8emu.emu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;

@Service("chip8")
public class Chip8Impl implements Chip8 {

    private static final int            MEMORY_SIZE       = 1024 * 4;
    private static final int            NUMBER_REGISTRIES = 16;
    private final        byte[]         memory            = new byte[MEMORY_SIZE];
    private final        byte[]         vRegs             = new byte[NUMBER_REGISTRIES];
    private final        Stack<Integer> stack             = new Stack<Integer>();
    private final        Random         random            = new Random(System.currentTimeMillis());
    private              short          iReg              = 0;
    private              int            pc                = 0x200;

    private final Video    video;
    private final Keyboard keyboard;
    private final Timer    delayTimer;
    private final Timer    soundTimer;

    @Autowired
    public Chip8Impl(Video video, Keyboard keyboard, Timer delayTimer, Timer soundTimer) {
        this.video = video;
        this.keyboard = keyboard;
        this.delayTimer = delayTimer;
        this.soundTimer = soundTimer;
        System.arraycopy(FontSet.FONT, 0, memory, 0, FontSet.FONT.length);
    }

    private int execOp(int op) {
        byte opCode = (byte) ((op & 0xF000) >> 12);
        switch (opCode) {
            case 0x0:
                return exec0(op);
            case 0x1:
                return exec1(op);
            case 0x2:
                return exec2(op);
            case 0x3:
                return exec3(op);
            case 0x4:
                return exec4(op);
            case 0x5:
                return exec5(op);
            case 0x6:
                return exec6(op);
            case 0x7:
                return exec7(op);
            case 0x8:
                return exec8(op);
            case 0x9:
                return exec9(op);
            case 0xa:
                return execA(op);
            case 0xb:
                return execB(op);
            case 0xc:
                return execC(op);
            case 0xd:
                return execD(op);
            case 0xe:
                return execE(op);
            case 0xf:
                return execF(op);
            default:
                throw new IllegalArgumentException("" + opCode);
        }
    }

    private int execF(int op) {
        int x = (op & 0x0f00) >> 8;
        int opCode = op & 0x00ff;
        switch (opCode) {
            case 0x7:
                vRegs[x] = delayTimer.getTimerValue();
                break;
            case 0xa:
                do {
                    vRegs[x] = keyboard.waitForKeyPressed();
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } while (vRegs[x] < 0);
                break;
            case 0x15:
                delayTimer.setTimer(vRegs[x]);
                break;
            case 0x18:
                soundTimer.setTimer(vRegs[x]);
                break;
            case 0x1e:
                iReg += vRegs[x];
                break;
            case 0x29:
                iReg = (short) (vRegs[x] * 5);
                break;
            case 0x33:
                int centuries = vRegs[x] / 100;
                int decades = (vRegs[x] - centuries * 100) / 10;
                int units = vRegs[x] - centuries * 100 - decades * 10;
                memory[iReg] = (byte) centuries;
                memory[iReg + 1] = (byte) decades;
                memory[iReg + 2] = (byte) units;
                break;
            case 0x55:
                System.arraycopy(vRegs, 0, memory, iReg, x + 1);
                break;
            case 0x65:
                System.arraycopy(memory, iReg, vRegs, 0, x + 1);
                break;
            default:
                throw new IllegalArgumentException("" + opCode);
        }
        return pc + 2;
    }

    private int execE(int op) {
        int x = (op & 0x0f00) >> 8;
        int opCode = op & 0x00ff;
        switch (opCode) {
            case 0x9e:
                return keyboard.keyIsPressed(vRegs[x]) ? pc + 4 : pc + 2;
            case 0xa1:
                return keyboard.keyIsPressed(vRegs[x]) ? pc + 2 : pc + 4;
        }
        throw new IllegalArgumentException("" + opCode);
    }

    private int execD(int op) {
        int x = (op & 0x0f00) >> 8;
        int y = (op & 0x00f0) >> 4;
        int n = op & 0x000f;
        byte[] sprite = Arrays.copyOfRange(memory, iReg, iReg + n);
        vRegs[0xf] = (byte) (video.drawSprite(vRegs[x], vRegs[y], sprite) ? 1 : 0);
        return pc + 2;
    }

    private int execC(int op) {
        int x = (op & 0x0f00) >> 8;
        int val = op & 0x00ff;
        vRegs[x] = (byte) (random.nextInt(256) & val);
        return pc + 2;
    }

    private int execB(int op) {
        int addr = op & 0x0fff;
        return addr + vRegs[0];
    }

    private int execA(int op) {
        iReg = (short) (op & 0x0fff);
        return pc + 2;
    }

    private int exec9(int op) {
        int x = (op & 0x0f00) >> 8;
        int y = (op & 0x00f0) >> 4;
        return vRegs[x] != vRegs[y] ? pc + 4 : pc + 2;
    }

    private int exec8(int op) {
        int opCode = op & 0x000f;
        int x = (op & 0x0f00) >> 8;
        int y = (op & 0x00f0) >> 4;
        switch (opCode) {
            case 0x0:
                vRegs[x] = vRegs[y];
                break;
            case 0x1:
                vRegs[x] |= vRegs[y];
                break;
            case 0x2:
                vRegs[x] &= vRegs[y];
                break;
            case 0x3:
                vRegs[x] ^= vRegs[y];
                break;
            case 0x4:
                vRegs[x] += vRegs[y];
                vRegs[0xf] = (byte) (vRegs[x] < 0 ? 1 : 0);
                break;
            case 0x5:
                vRegs[0xf] = (byte) (vRegs[x] >= vRegs[y] ? 1 : 0);
                vRegs[x] -= vRegs[y];
                break;
            case 0x6:
                vRegs[0xf] = (byte) (vRegs[x] & 0x01);
                vRegs[x] >>= 1;
                break;
            case 0x7:
                vRegs[0xf] = (byte) (vRegs[y] >= vRegs[x] ? 1 : 0);
                vRegs[x] = (byte) (vRegs[y] - vRegs[x]);
                break;
            case 0xe:
                vRegs[0xf] = (byte) (vRegs[x] & 0x80);
                vRegs[x] <<= 1;
                break;
            default:
                throw new IllegalArgumentException("" + opCode);
        }
        return pc + 2;
    }

    private int exec7(int op) {
        int x = (op & 0x0f00) >> 8;
        int val = op & 0x00ff;
        vRegs[x] += val;
        vRegs[0xf] = (byte) (vRegs[x] < 0 ? 1 : 0);
        vRegs[x] &= 0xff;
        return pc + 2;
    }

    private int exec6(int op) {
        int x = (op & 0x0f00) >> 8;
        int val = op & 0x00ff;
        vRegs[x] = (byte) val;
        return pc + 2;
    }

    private int exec5(int op) {
        int x = (op & 0x0f00) >> 8;
        int y = (op & 0x00f0) >> 4;
        return vRegs[x] == vRegs[y] ? pc + 4 : pc + 2;
    }

    private int exec4(int op) {
        int x = (op & 0x0f00) >> 8;
        int val = op & 0x00ff;
        return vRegs[x] != val ? pc + 4 : pc + 2;
    }

    private int exec3(int op) {
        int x = (op & 0x0f00) >> 8;
        int val = op & 0x00ff;
        return vRegs[x] == val ? pc + 4 : pc + 2;
    }

    private int exec2(int op) {
        stack.push(pc + 2);
        return op & 0x0fff;
    }

    private int exec1(int op) {
        return op & 0x0fff;
    }

    private int exec0(int op) {
        switch (op) {
            case 0x00e0:
                video.clearScreen();
                break;
            case 0x00EE:
                return stack.pop();
        }
        return pc + 2;
    }

    @Override
    public void run() throws InterruptedException {
        while (true) {
            if (pc >= MEMORY_SIZE || pc < 0x200) {
                throw new AddressingException();
            }
            int op = memory[pc] & 0xff;
            op <<= 8;
            op |= memory[pc + 1] & 0xff;
            pc = execOp(op);
        }
    }

    @Override
    public void load(byte[] application) {
        System.arraycopy(application, 0, memory, 0x200, application.length);
    }

    @Override
    public void load(InputStream inputStream) throws IOException {
        inputStream.read(memory, 0x200, memory.length - 0x200);
    }
}
