package com.shepeliev.chip8emu;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class App {

    static {
        System.out.println(java.awt.GraphicsEnvironment.isHeadless());
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
