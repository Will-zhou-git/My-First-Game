package node;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import util.GameUtil;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Random;

@NoArgsConstructor
@Data
public class GameNode {
    //节点边界
    private int width = 50;
    private int height = 50;

    int boundX = 800;
    int boundY = 800;

    //改变节点1方向的参数
    private int count = 0;
    int changeCount = 30;

    //节点1方向个数
    int directCount = 4;

    private int direction = new Random().nextInt(directCount); // 0: up, 1: down, 2: left, 3: right
    private boolean isMoving = false;

    private int x = 50;
    private int y = 50;

    private Image image;

    private int curLevel;

    public GameNode(String imagePath) {
        setRanDomXAndY();

        image = GameUtil.getNodeImage(imagePath);
    }

    public GameNode(Image image) {
        setRanDomXAndY();

        this.image = image;
    }

    public void setRanDomXAndY() {
        GamePanel gamePanel = GameUtil.gamePanel;
        GameLevel curGameLevel = gamePanel.getCurGameLevel();
        if (curGameLevel == null) {
            return;
        }

        int[] ranDomXAndY = curGameLevel.getRanDomXAndY(boundX, boundY);
        if (ranDomXAndY == null || ranDomXAndY.length < 2) {
            return;
        }
        this.x = ranDomXAndY[0];
        this.y = ranDomXAndY[1];
    }

    public void draw(Graphics g) {
        g.drawImage(image, x, y, width, height, null);
    }

    public Rectangle getBounds() {
        return getBounds(width, height);
    }

    public Rectangle getBounds(int width, int height) {
        return new Rectangle(x, y, width, height);
    }

    public void keyReleased(KeyEvent e) {
        isMoving = false;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
            direction = GameUtil.up;
            isMoving = true;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            direction = GameUtil.down;
            isMoving = true;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            direction = GameUtil.left;
            isMoving = true;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            direction = GameUtil.right;
            isMoving = true;
        }
    }

    public int move() {
        GamePanel gamePanel = GameUtil.gamePanel;

        GameLevel curGameLevel = gamePanel.getCurGameLevel();
        if (curGameLevel == null) {
            return 0;
        }

        if (++count >= changeCount) {
            count = 0;
            direction = new Random().nextInt(directCount);
        }
        int[] newXAndYAndD = curGameLevel.getNewXAndYAndD(this);
        if (newXAndYAndD == null || newXAndYAndD.length < 3) {
            return 0;
        }

        x = newXAndYAndD[0];
        y = newXAndYAndD[1];
        direction = newXAndYAndD[2];

        if (curGameLevel.nodeCanExpire() && direction == GameUtil.none) {
            return -1;
        }

        return 1;
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed) {
        return getNewXAndYAndD(x, y, direction, speed, new int[]{boundX, boundY}, 0);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int[] limit) {
        return getNewXAndYAndD(x, y, direction, speed, limit, 0);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int disappear) {
        return getNewXAndYAndD(x, y, direction, speed, new int[]{boundX, boundY}, disappear);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int[] limit, int disappear) {
        if (limit == null || limit.length < 2) {
            return null;
        }
        if (limit.length == 2) {
            limit = new int[]{0, 0, boundX, boundY};
        }
        boolean changeDirection = false;
        switch (direction) {
            case 0:
                int nextYl = y - speed;
                if (nextYl >= limit[1] && nextYl <= limit[3]) {
                    y = nextYl;
                } else {
                    changeDirection = true;
                }
                break;
            case 1:
                int nextYr = y + speed;
                if (nextYr >= limit[1] && nextYr <= limit[3]) {
                    y = nextYr;
                } else {
                    changeDirection = true;
                }
                break;
            case 2:
                int nextXl = x - speed;
                if (nextXl >= limit[0] && nextXl <= limit[2]) {
                    x = nextXl;
                } else {
                    changeDirection = true;
                }
                break;
            case 3:
                int nextXr = x + speed;
                if (nextXr >= limit[0] && nextXr <= limit[2]) {
                    x = nextXr;
                } else {
                    changeDirection = true;
                }
                break;
            default:
                break;
        }
        if (changeDirection) {
            if (disappear == 0) {
                int nextDirect;
                do {
                    nextDirect = new Random().nextInt(directCount);
                } while (nextDirect == direction);
                direction = nextDirect;
            } else {
                direction = -1;
            }
        }
        return new int[]{x, y ,direction};
    }
}
