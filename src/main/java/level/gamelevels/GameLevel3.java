package level.gamelevels;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import node.GameNode;
import node.PlayerNode;
import util.GameUtil;
import util.UIUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

@Data
public class GameLevel3 extends GameLevel {
    //关卡信息
    private int levelTime = 1;
    private int targetScoreToNext = 20;

    //人物信息
    private int playerXInit = 400;
    private int playerYInit = 100;
    private int playerSpeed = 0;

    //节点1信息
    private String imagePath3 = "node/wawa.png"; // 黄金图片
    private int nodeCount = 20;

    // 钩子相关参数
    private double hookAngle = 0;  // 钩子当前角度
    private double hookLength = 50;  // 钩子长度
    private double hookSpeed = 2;  // 钩子摆动速度
    private boolean isHookExtending = false;  // 钩子是否在伸长
    private boolean isHookRetracting = false;  // 钩子是否在收回
    private double hookExtendSpeed = 30;  // 钩子伸长速度
    private GameNode caughtGold = null;  // 抓住的黄金
    private double maxHookLength = 800;  // 增加最大长度
    // 钩子绘制参数
    private int ropeWidth = 3;      // 绳子粗细
    private int hookSize = 20;      // 钩子大小


    @Override
    public int getCurLevel() {
        return 3;
    }

    @Override
    public void generateGameNode() {
        GamePanel gamePanel = GameUtil.gamePanel;
        if (gamePanel.getNodes().size() == 0) {
            GameUtil.generateRandomNodes(nodeCount, imagePath3);
        }
    }

    @Override
    public int[] getNewXAndYAndD(PlayerNode playerNode) {
        return playerNode.getNewXAndYAndD(playerNode, playerNode.getX(), playerNode.getY(), playerNode.getDirection(), playerSpeed);
    }

    @Override
    public int[] getNewXAndYAndD(GameNode gameNode1) {
        return gameNode1.getNewXAndYAndD(gameNode1, gameNode1.getX(), gameNode1.getY(), gameNode1.getDirection(), getNodeSpeed(),
        new int[]{0, 300, GameUtil.gamePanel.getBoundWidth(), GameUtil.gamePanel.getBoundHeight() - 100});
    }

    @Override
    public int[] getRanDomXAndY(int boundX, int boundY) {
        return new int[]{new Random().nextInt(boundX), new Random().nextInt(400) + 300};
    }

    @Override
    public void updateLevel() {
        // 更新钩子摆动
        if (!isHookExtending && !isHookRetracting) {
            hookAngle += hookSpeed;
            if (hookAngle > 70 || hookAngle < -70) {
                hookSpeed = -hookSpeed;
            }
        }

        // 更新钩子伸缩
        if (isHookExtending) {
            hookLength += hookExtendSpeed;
            if (isOutOfBounds() || hookLength > maxHookLength || caughtGold != null) {
                isHookExtending = false;
                isHookRetracting = true;
            }
            checkGoldCollision();
        }

        if (isHookRetracting) {
            hookLength -= hookExtendSpeed;
            if (hookLength <= 50) {
                resetHook();
            }
        }
    }

    // 检查是否超出边界
    private boolean isOutOfBounds() {
        int endX = playerXInit + 50 + (int)(hookLength * Math.sin(Math.toRadians(hookAngle)));
        int endY = playerYInit + 50 + (int)(hookLength * Math.cos(Math.toRadians(hookAngle)));
        return endX < 0 || endX > GameUtil.gamePanel.getBoundWidth() || endY < 0 || endY > GameUtil.gamePanel.getBoundHeight();
    }

    private void resetHook() {
        hookLength = 50;
        isHookRetracting = false;
        if (caughtGold != null) {
            GameUtil.gamePanel.setScoring(GameUtil.gamePanel.getScoring() + getGoldScore(caughtGold));
            GameUtil.gamePanel.getNodes().remove(caughtGold);
            caughtGold = null;
        }
    }

    private int getGoldScore(GameNode gold) {
        // 根据黄金大小返回不同分数
        return 1;
    }

    private void checkGoldCollision() {
        if (caughtGold != null) return;

        int hookX = playerXInit + 50 + (int)(hookLength * Math.sin(Math.toRadians(hookAngle)));
        int hookY = playerYInit + 50 + (int)(hookLength * Math.cos(Math.toRadians(hookAngle)));

        for (GameNode gold : GameUtil.gamePanel.getNodes()) {
            // [新增] 使用Rectangle进行碰撞检测
            Rectangle hookBounds = new Rectangle(
                    hookX - hookSize/2,
                    hookY - hookSize/2,
                    hookSize,
                    hookSize
            );

            Rectangle goldBounds = new Rectangle(
                    gold.getX(),
                    gold.getY(),
                    gold.getWidth(),
                    gold.getHeight()
            );

            if (hookBounds.intersects(goldBounds)) {
                caughtGold = gold;
                break;
            }
        }
    }

    @Override
    public void handleKeyPressed(int keyCode) {
        super.handleKeyPressed(keyCode);
        if (keyCode == KeyEvent.VK_SPACE && !isHookExtending && !isHookRetracting) {
            isHookExtending = true;
        }
    }

    @Override
    public void drawSpecialElements(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int startX = playerXInit + 50;
        int startY = playerYInit + 100;
        int endX = startX + (int)(hookLength * Math.sin(Math.toRadians(hookAngle)));
        int endY = startY + (int)(hookLength * Math.cos(Math.toRadians(hookAngle)));

        // 绘制绳子
        g2d.setColor(new Color(139, 69, 19)); // 深棕色绳子
        g2d.setStroke(new BasicStroke(ropeWidth));
        g2d.drawLine(startX, startY, endX, endY);

        // 绘制钩子头
        UIUtil.drawHook(g2d, endX, endY, hookAngle, hookSize);

        // 如果抓住了黄金，在钩子末端绘制
        if (caughtGold != null) {
            caughtGold.setX(endX - caughtGold.getWidth()/2);
            caughtGold.setY(endY - caughtGold.getHeight()/2);
        }
    }

    @Override
    public void enterNextLevel(){
        GamePanel gamePanel = GameUtil.gamePanel;
        gamePanel.setGameLevel(new GameLevel4());
        super.enterNextLevel();
    }
}
