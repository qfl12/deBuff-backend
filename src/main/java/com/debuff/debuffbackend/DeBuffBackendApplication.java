package com.debuff.debuffbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.debuff.debuffbackend.mapper")
public class DeBuffBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeBuffBackendApplication.class, args);
    }

}
