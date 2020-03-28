package com.hxxzt.recruitment;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@MapperScan("com.hxxzt.recruitment.**.mapper")
@EnableAsync
@Slf4j
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        log.info(
                "启动成功,欢迎访问博客 https://penint.vip \n"+
                        ".______    _______ .__   __.  __  .__   __. .___________.____    ____  __  .______   \n" +
                        "|   _  \\  |   ____||  \\ |  | |  | |  \\ |  | |           |\\   \\  /   / |  | |   _  \\  \n" +
                        "|  |_)  | |  |__   |   \\|  | |  | |   \\|  | `---|  |----` \\   \\/   /  |  | |  |_)  | \n" +
                        "|   ___/  |   __|  |  . `  | |  | |  . `  |     |  |       \\      /   |  | |   ___/  \n" +
                        "|  |      |  |____ |  |\\   | |  | |  |\\   |     |  |     __ \\    /    |  | |  |      \n" +
                        "| _|      |_______||__| \\__| |__| |__| \\__|     |__|    (__) \\__/     |__| | _|      \n" +
                        "                                                                                     "
        );
    }

}
