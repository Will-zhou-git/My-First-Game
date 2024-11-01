package level;

import game.GamePanel;
import lombok.Data;
import node.GameNode;
import node.PlayerNode;
import util.GameUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;

@Data
public abstract class GameLevel {
    //关卡信息
    private int levelTime = 10;
    private int targetScoreToNext = 10;
    private String backGroundImage = "background/green.png";

    //关卡中的人物信息
    private String curPlayerImage = "miemie.png";
    private int playerXInit = 400;
    private int playerYInit = 400;
    private int playerSpeed = 30;

    //关卡中节点1信息
    private int nodeCount = 10;
    private String imagePath = "node/qingcao.png";
    private int nodeSpeed = 5;

    public abstract int getCurLevel();

    public boolean openKeyReleased() {
        return false;
    }

    public boolean nodeCanExpire() {
        return false;
    }

    public void generateGameNode() {
        if (GameUtil.gamePanel.getNodes().size() == 0) {
            GameUtil.generateRandomNodes(nodeCount, imagePath);
        }
    };

    public void enterNextLevel(){
        GamePanel gamePanel = GameUtil.gamePanel;
        gamePanel.setCurLevel(gamePanel.getCurLevel() + 1);
        gamePanel.setStarGameTime(System.currentTimeMillis());
        gamePanel.setScoring(0);
        gamePanel.getNodes().clear();
        gamePanel.setPlayerNode(new PlayerNode());
    }


    public int[] getNewXAndYAndD(PlayerNode playerNode) {
        if (playerNode == null) {
            return null;
        }
        return playerNode.getNewXAndYAndD(playerNode, playerNode.getX(), playerNode.getY(), playerNode.getDirection(), playerSpeed);
    }

    public int[] getNewXAndYAndD(GameNode gameNode1) {
        if (gameNode1 == null) {
            return null;
        }
        return gameNode1.getNewXAndYAndD(gameNode1, gameNode1.getX(), gameNode1.getY(), gameNode1.getDirection(), nodeSpeed);
    }

    public int[] getRanDomXAndY(int boundX, int boundY) {
        return new int[]{new Random().nextInt(boundX), new Random().nextInt(boundY)};
    }

    public void updateLevel() {
        // 默认空实现，子类可以覆盖
        // 更新当前关卡特殊逻辑
    }

    public void drawSpecialElements(Graphics g) {
        // 默认空实现，子类可以覆盖
        // 绘制当前关卡特殊元素
    }

    public void handleKeyPressed(int keyCode) {
        // 默认空实现，子类可以覆盖
        // 处理当前关卡特殊按键
        if (keyCode == KeyEvent.VK_ESCAPE) {
            // 直接进入下一关
            enterNextLevel();
        }
    }

    public int checkCollisionPlayerAndNode() {
        GamePanel gamePanel = GameUtil.gamePanel;

        Rectangle playerBounds = gamePanel.getPlayerNode().getBounds();

        List<GameNode> nodesToRemove = new ArrayList<>();
        for (GameNode node : gamePanel.getNodes()) {
            Rectangle nodeBounds = node.getBounds();
            if (playerBounds.intersects(nodeBounds)) {
                nodesToRemove.add(node);
                gamePanel.setScoring(gamePanel.getScoring() + 1);
            }
        }
        gamePanel.getNodes().removeAll(nodesToRemove);
        return nodesToRemove.size();
    }

    public void changePlayerAttackUI(Graphics g) {

    }

    public Set<Integer> getValidDirect(GameNode gameNode) {
        return new HashSet<>(Arrays.asList(
                GameUtil.up,
                GameUtil.down,
                GameUtil.left,
                GameUtil.right));
    }
}
