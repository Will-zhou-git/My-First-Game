package level.gamelevels;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import node.GameNode;
import node.PlayerNode;
import util.GameUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

@Data
public class GameLevel2 extends GameLevel {
    //关卡信息
    private int levelTime = 1;
    private int targetScoreToNext = 20;
    private String backGroundImage = "background/green2.png";

    //人物信息
    private int playerYInit = 800;
    private int playerSpeed = 45;

    //节点1信息
    private int generateTime = 1 * 1000; //自动生成时间 单位ms
    private long lastGenerateNode2Time = 0;
    private int nodeCount = 5;
    private int nodeSpeed = 15;
    private String imagePath2 = "node/taiyang.png";


    @Override
    public int getCurLevel() {
        return 2;
    }

    @Override
    public boolean nodeCanExpire() {
        return true;
    }

    @Override
    public boolean openKeyReleased() {
        return true;
    }

    @Override
    public void enterNextLevel(){
        GamePanel gamePanel = GameUtil.gamePanel;
        gamePanel.setGameLevel(new GameLevel3());

        super.enterNextLevel();
    }

    @Override
    public void generateGameNode() {
        if (System.currentTimeMillis() - getLastGenerateNode2Time() >= generateTime) {
            setLastGenerateNode2Time(System.currentTimeMillis());

            GameUtil.generateRandomNodes(nodeCount, imagePath2);
        }
    }

    @Override
    public int[] getNewXAndYAndD(PlayerNode playerNode) {
        return playerNode.getNewXAndYAndD(
                playerNode, playerNode.getX(),
                playerNode.getY(),
                playerNode.getDirection(),
                playerSpeed,
                new int[]{0, 750, playerNode.getBoundX(), playerNode.getBoundY()});
    }

    @Override
    public int[] getNewXAndYAndD(GameNode gameNode1) {
        if (gameNode1 == null) {
            return null;
        }
        return gameNode1.getNewXAndYAndD(gameNode1, gameNode1.getX(), gameNode1.getY(), GameUtil.down, nodeSpeed, 1);
    }

    @Override
    public String getBackGroundImage() {
        return backGroundImage;
    }

    @Override
    public int[] getRanDomXAndY(int boundX, int boundY) {
        return new int[]{new Random().nextInt(boundX), new Random().nextInt(100)};
    }

    @Override
    public Set<Integer> getValidDirect(GameNode gameNode) {
        if (gameNode instanceof PlayerNode) {
            return new HashSet<>(Arrays.asList(
                    GameUtil.left,
                    GameUtil.right));
        } else {
            return super.getValidDirect(gameNode);
        }

    }
}
