package node;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import util.GameUtil;

import java.awt.*;

@Data
public class PlayerNode extends GameNode{
    //人物边界
    private int width = 100;
    private int height = 100;

    public PlayerNode() {
        GameLevel curGameLevel = GameUtil.gamePanel.getCurGameLevel();
        if (curGameLevel == null) {
            return;
        }
        super.setX(curGameLevel.getPlayerXInit());
        super.setY(curGameLevel.getPlayerYInit());
        super.setImage(GameUtil.getNodeImageByWebp(curGameLevel.getCurPlayerImage()));
        super.setCurLevel(curGameLevel.getCurLevel());
        super.setMoving(false);
    }

    @Override
    public void draw(Graphics g) {
       g.drawImage(super.getImage(), super.getX(), super.getY(), width, height,null);
    }

    @Override
    public Rectangle getBounds() {
        return super.getBounds(width, height);
    }

    @Override
    public int move() {
        GamePanel gamePanel = GameUtil.gamePanel;

        GameLevel curGameLevel = gamePanel.getCurGameLevel();
        if (curGameLevel == null) {
            return 0;
        }
        if (super.isMoving()) {
            int[] newXAndYAndD = curGameLevel.getNewXAndYAndD(gamePanel.getPlayerNode());
            if (newXAndYAndD == null || newXAndYAndD.length < 3) {
                return 0;
            }
            super.setX(newXAndYAndD[0]);
            super.setY(newXAndYAndD[1]);
            super.setDirection(newXAndYAndD[2]);
        }
        return 1;
    }

}
