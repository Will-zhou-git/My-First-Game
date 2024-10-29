package node;

import game.GamePanel;
import lombok.Data;
import util.GameUtil;

import java.awt.*;

@Data
public class TankNode extends GameNode{

    //关卡初始位置
    private int x1Init = 400;
    private int y1Init = 400;

    private int x2Init = 400;
    private int y2Init = 800;


    private int width = 100;
    private int height = 100;
    private int speed = 30;
    private int speed2 = 45;
    private String tankImagePath = "miemie.png";

    private int curLevels = 1;

    public TankNode() {
        super.setX(x1Init);
        super.setY(y1Init);
        super.setImage(GameUtil.getNodeImageByWebp(tankImagePath));
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
    public int move(GamePanel gamePanel) {
        if (gamePanel == null) {
            return 0;
        }
        if (super.isMoving()) {
            int[] newXAndYAndD = null;
            if (gamePanel.getCurLevels() == 1) {
                newXAndYAndD = super.getNewXAndYAndD(super.getX(), super.getY(), super.getDirection(), speed);
            } else if (gamePanel.getCurLevels() == 2) {
                newXAndYAndD = super.getNewXAndYAndD(super.getX(), super.getY(), super.getDirection(), speed2,
                        new int[]{super.getBoundX(), super.getBoundY() - 800});
            }
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
