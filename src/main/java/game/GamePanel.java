package game;


import level.GameLevel;
import level.gamelevels.GameLevel1;
import lombok.Data;
import node.GameNode;
import node.PlayerNode;
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
    //边界范围
    private int boundWidth = 1000;
    private int boundHeight = 1000;

    private GameLevel gameLevel = new GameLevel1();
    private PlayerNode playerNode;
    private List<GameNode> nodes;

    //关于关卡的一些参数
    private int curLevel = 1;
    private long starGameTime = System.currentTimeMillis();

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
        addKeyListener(this);
        //设置组件可以接受键盘输入事件
        setFocusable(true);

        nodes = new ArrayList<>();
    }

    private void checkCollision() {
        Rectangle playerBounds = playerNode.getBounds();

        List<GameNode> nodesToRemove = new ArrayList<>();
        for (GameNode node : nodes) {
            Rectangle nodeBounds = node.getBounds();
            if (playerBounds.intersects(nodeBounds)) {
                nodesToRemove.add(node);
                scoring++;
            }
        }
        nodes.removeAll(nodesToRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        GamePanel gamePanel = GameUtil.gamePanel;
        GameLevel curGameLevel = gamePanel.getCurGameLevel();
        if (curGameLevel == null) {
            return;
        }

        //绘制一个填充矩形
        g.fillRect(0, 0, boundWidth, boundHeight);

        g.drawImage(GameUtil.getNodeImage(curGameLevel.getBackGroundImage()), 0, 0, boundWidth, boundHeight, this);

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

        if (playerNode != null) {
            playerNode.draw(g);
        }

        for (GameNode node : nodes) {
            if (node == null) {
                continue;
            }
            node.draw(g);
        }

        // 绘制当前关卡特殊元素
        curGameLevel.drawSpecialElements(g);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        playerNode.keyPressed(e);

        // 处理当前关卡特殊按键
        GameLevel curGameLevel = getCurGameLevel();
        if (curGameLevel != null) {
            curGameLevel.handleKeyPressed(e.getKeyCode());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        GameLevel curGameLevel = getCurGameLevel();
        if (curGameLevel == null) {
            return;
        }

        if (curGameLevel.openKeyReleased()) {
            playerNode.keyReleased(e);
        }
    }

    public void updateGame() {
        //检测是否进入下一关
        checkEnterNextLevel();

        //检测人物
        checkPlayerNode();

        //检测节点
        checkGameNode();

        //碰撞检测
        checkCollision();

        // 更新当前关卡特殊逻辑
        GameLevel curGameLevel = getCurGameLevel();
        if (curGameLevel != null) {
            curGameLevel.updateLevel();
        }

        //触发组件重新绘制
        repaint();
    }

    public boolean checkEnterNextLevel() {
        GameLevel curGameLevel = getCurGameLevel();
        if (curGameLevel == null) {
            return false;
        }

        int levelTime = curGameLevel.getLevelTime();
        int targetScore = curGameLevel.getTargetScoreToNext();

        countdownSeconds = levelTime - (int) ((System.currentTimeMillis() - starGameTime) / 1000);
        if (System.currentTimeMillis() - starGameTime < levelTime * 1000 && scoring < targetScore) {
            return false;
        }

        curGameLevel.enterNextLevel();
        return true;
    }

    public void checkPlayerNode() {
        if (playerNode == null) {
            playerNode = new PlayerNode();
        }

        //初始化人物 和移动逻辑
        playerNode.move();
    }

    public void checkGameNode() {
        GameLevel curGameLevel = getCurGameLevel();
        if (curGameLevel == null) {
            return;
        }
        //生成节点
        curGameLevel.generateGameNode();

        //检测节点移动
        Iterator<GameNode> iterator = nodes.iterator();
        while (iterator.hasNext()) {
            GameNode node = iterator.next();
            if (node == null) {
                continue;
            }
            int move = node.move();
            if (move == -1) {
                iterator.remove();
            }
        }
    }

    public GameLevel getCurGameLevel() {
        return gameLevel;
    }
}
