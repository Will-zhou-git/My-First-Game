package start;


import game.GamePanel;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        System.out.println("Start!");

        //创建一个新的框架
        JFrame frame = new JFrame("TankNode Battle");
        //创建一个新的面板类
        GamePanel panel = new GamePanel();
        frame.add(panel);
        frame.setSize(1000, 1000);
        //设置用户点击关闭窗口可以正常关闭
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //设置组件可见
        frame.setVisible(true);

        while (true) {
            panel.updateGame();
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
