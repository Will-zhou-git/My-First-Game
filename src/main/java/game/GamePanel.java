package game;


import lombok.Data;
import node.GameNode;
import node.TankNode;
import util.GameUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.Iterator;
import java.util.List;

@Data
public class GamePanel extends JPanel implements KeyListener {
    //随机节点个数
    private int nodeCount = 10;
    //边界范围
    private int boundWidth = 1000;
    private int boundHeight = 1000;
    //背景
    private String backGroundImage = "background/green.png";
    private String backGroundImage2 = "background/green2.png";
    private String imagePath = "node/qingcao.png";
    private String imagePath2 = "node/taiyang.png";

    private TankNode tank;
    private List<GameNode> nodes;

    //关于关卡的一些参数
    private int curLevels = 1;
    private long starGameTime = System.currentTimeMillis();
    private long lastGenerateNode2Time = System.currentTimeMillis();
    private long generateTime = 1; //单位/s

    //关卡时间
    private int leve1Time = 10;
    private int leve2Time = 30;

    //倒计时
    private int countdownSeconds;
    private int clockSize = 30;
    private int clockX = 10;
    private int clockY = 30;

    //计分器
    private int scoring;
    private int scoringSize = 30;
    private int scoringX = 900;
    private int scoringY = 30;

    public GamePanel() {
        tank = new TankNode();
        addKeyListener(this);
        //设置组件可以接受键盘输入事件
        setFocusable(true);

        nodes = new ArrayList<>();

//        GameUtil.generateRandomNodes(nodes, nodeCount);
    }

    private void checkCollision() {
        Rectangle tankBounds = tank.getBounds();
        List<GameNode> nodesToRemove = new ArrayList<>();
        for (GameNode node : nodes) {
            Rectangle nodeBounds = node.getBounds();
            if (tankBounds.intersects(nodeBounds)) {
                nodesToRemove.add(node);
                scoring++;
            }
        }
        nodes.removeAll(nodesToRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //绘制一个填充矩形
        g.fillRect(0, 0, boundWidth, boundHeight);
        if (curLevels == 1) {
            g.drawImage(GameUtil.getNodeImage(backGroundImage), 0, 0, boundWidth, boundHeight, this);
        } else if (curLevels == 2) {
            g.drawImage(GameUtil.getNodeImage(backGroundImage2), 0, 0, boundWidth, boundHeight, this);
        }


        // 绘制倒计时
        g.setColor(Color.RED);
        Font font = new Font("Arial", Font.BOLD, clockSize);
        g.setFont(font);
        g.drawString(String.valueOf(countdownSeconds), clockX, clockY);

        // 绘制计分器
        g.setColor(Color.GREEN);
        Font fontJiFen = new Font("Arial", Font.BOLD, scoringSize);
        g.setFont(fontJiFen);
        g.drawString(String.valueOf(scoring), scoringX, scoringY);

        tank.draw(g);
        for (GameNode node : nodes) {
            node.draw(g);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        tank.keyPressed(e);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (tank.getCurLevels() == 2) {
            tank.keyReleased(e);
        }
    }

    public void updateGame() {
        tank.move(this);

        Iterator<GameNode> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            GameNode node = iterator.next();
            if (node == null) {
                continue;
            }
            int move = node.move(this);
            if (move == -1) {
                iterator.remove();
            }
        }

        checkCollision();

        int levelTime = 0;
        if (curLevels == 1) {
            levelTime = leve1Time;
        } else if (curLevels == 2) {
            levelTime = leve2Time;
        }

        countdownSeconds = levelTime - (int) ((System.currentTimeMillis() - starGameTime) / 1000);
        if (System.currentTimeMillis() - starGameTime >= levelTime * 1000) {
            curLevels = curLevels + 1;
            starGameTime = System.currentTimeMillis();

            if (curLevels == 2) {
                tank.setX(tank.getX2Init());
                tank.setY(tank.getY2Init());
                tank.setCurLevels(2);
                tank.setMoving(false);
                scoring = 0;
            }

            nodes.clear();
        }

        if (nodes.size() == 0) {
            if (curLevels == 1) {
                GameUtil.generateRandomNodes(nodes, nodeCount, imagePath, curLevels);
            }
        }

        if (curLevels == 2) {
            if (System.currentTimeMillis() - lastGenerateNode2Time >= generateTime * 500) {
                lastGenerateNode2Time = System.currentTimeMillis();

                GameUtil.generateRandomNodes(nodes, 5, imagePath2, curLevels);
            }
        }

        //触发组件重新绘制
        repaint();
    }
}
