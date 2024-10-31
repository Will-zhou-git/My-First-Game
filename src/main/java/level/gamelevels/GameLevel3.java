package level.gamelevels;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import node.PlayerNode;
import util.GameUtil;

@Data
public class GameLevel3 extends GameLevel {
    //关卡信息
    private int levelTime = 60;
    private int targetScoreToNext = 20;

    //人物信息
    private int palyerXInit = 500;
    private int palyerYInit = 500;
    private int playerSpeed = 35;

    //节点1信息
    private String imagePath3 = "node/wawa.png"; // 黄金图片
    private int nodeCount = 5;

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
        return playerNode.getNewXAndYAndD(playerNode.getX(), playerNode.getY(), playerNode.getDirection(), playerSpeed);
    }
}
