package com.shepeliev.chip8emu.emu;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("timer")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TimerImpl implements Timer {

    private long setTime;
    private int  initValue;

    @Override
    public void setTimer(byte value) {
        this.initValue = value;
        setTime = System.currentTimeMillis();
    }

    @Override
    public byte getTimerValue() {
        long delta = System.currentTimeMillis() - setTime;
        int ticks = (int) (delta / (1000 / 60));
        return (byte) Math.max(initValue - ticks, 0);
    }
}
