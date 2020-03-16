package main.java.com.ha;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.support.SpringBootServletInitializer;

import java.io.IOException;

@MapperScan("main.java.com.ha.dao")
//@EnableScheduling//启动定时任务配置
@SpringBootApplication
public class FaceApplication extends SpringBootServletInitializer {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(FaceApplication.class, args);


    }


}
