package TankGame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Scanner;

public class window extends JFrame {
    mypanel mp =null;//定义一个面板s
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        window window1 = new window();//创建一个窗口



    }

    //窗口构造器
    public window() {
        System.out.println("请选择游戏模式：1新游戏 2继续游戏");
        String key = scanner.next();//接收用户输入
        mp = new mypanel(key);//初始化面板
        //启动线程,让面板重绘
        Thread t = new Thread(mp);
        t.start();


        this.add(mp);//把面板（绘图区域）加载
        this.setSize(1300, 1000);//设置窗口的大小
        this.addKeyListener(mp);//给窗口添加键盘监听
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//当点击窗口的小×，程序完全退出.
        this.setVisible(true);//可以显示


        //在JFrame中添加一个相应关闭窗口的处理
        this.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                Recorder.keeprecord();
                System.exit(0);
            }
        });
    }
}