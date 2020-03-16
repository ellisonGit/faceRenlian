package main.java.com.ha.controller;

import main.java.com.ha.util.Face;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description:
 * User: Ellison
 * Date: 2019-11-12
 * Time: 15:41
 * Modified:
 */
public class testdemo {
    static byte[] bytes;

    public static void main(String[] args) throws Exception {




    }




    // 第一种方法：设定指定任务task在指定时间time执行 schedule(TimerTask task, Date time)
    public static void timer1() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                System.out.println("-------设定要指定任务启动准备就绪--------");
                try {
                    Face.testListFace(10001);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);// 设定指定的时间time,此处为2000毫秒，只执行一次
    }//timer1



}





