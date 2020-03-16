package main.java.com.ha;

import main.java.com.ha.controller.testdemo;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description:
 * User: Ellison
 * Date: 2019-12-03
 * Time: 18:29
 * Modified:
 */
@Component
@Order(2)
public class InitProject implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("==========init project===========");
        testdemo.timer1();
    }
}
