package com.shepeliev.chip8emu;

import com.shepeliev.chip8emu.emu.Chip8;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Controller;

import java.io.FileInputStream;
import java.io.InputStream;

@Controller
public class EmulatorRunner implements CommandLineRunner {

    private static final byte[] TEST_APP = new byte[] {0x60,
                                                       0x00,
                                                       0x61,
                                                       0x00,
                                                       (byte) 0xA2,
                                                       0x22,
                                                       (byte) 0xC2,
                                                       0x01,
                                                       0x32,
                                                       0x01,
                                                       (byte) 0xA2,
                                                       0x1E,
                                                       (byte) 0xD0,
                                                       0x14,
                                                       0x70,
                                                       0x04,
                                                       0x30,
                                                       0x40,
                                                       0x12,
                                                       0x04,
                                                       0x60,
                                                       0x00,
                                                       0x71,
                                                       0x04,
                                                       0x31,
                                                       0x20,
                                                       0x12,
                                                       0x04,
                                                       0x12,
                                                       0x00,
                                                       (byte) 0x80,
                                                       0x40,
                                                       0x20,
                                                       0x10,
                                                       0x20,
                                                       0x40,
                                                       (byte) 0x80,
                                                       0x10};
    private final Chip8 chip8;

    @Autowired
    public EmulatorRunner(Chip8 chip8) {
        this.chip8 = chip8;
    }

    @Override
    public void run(String... args) throws Exception {
        if (args.length == 0) {
            chip8.load(TEST_APP);
        } else {
            InputStream is = new FileInputStream(args[0]);
            chip8.load(is);
            is.close();
        }
        chip8.run();
    }
}
