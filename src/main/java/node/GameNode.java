package node;

import game.GamePanel;
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

    private int speed = 5;
    private int speed2 = 15;
    int boundX = 800;
    int boundY = 800;

    private int count = 0;
    int changeCount = 30;
    int directCount = 4;

    private int direction = new Random().nextInt(directCount); // 0: up, 1: down, 2: left, 3: right
    private boolean isMoving = false;

    private int x = 50;
    private int y = 50;

    private Image image;

    public GameNode(boolean isRandom, String imagePath, int curLevels) {
        if (isRandom) {
            setRanDomXAndY(curLevels);
        }
        image = GameUtil.getNodeImage(imagePath);
    }

    public GameNode(boolean isRandom, Image image, int curLevels) {
        if (isRandom) {
            setRanDomXAndY(curLevels);
        }
        this.image = image;
    }

    public void setRanDomXAndY(int curLevels) {
        this.x = new Random().nextInt(boundX);
        this.y = new Random().nextInt(boundY);
        if (curLevels == 2) {
            this.y = new Random().nextInt(100);
        }
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
            direction = 0;
            isMoving = true;
        } else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
            direction = 1;
            isMoving = true;
        } else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            direction = 2;
            isMoving = true;
        } else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            direction = 3;
            isMoving = true;
        }
    }

    public int move(GamePanel gamePanel) {
        if (gamePanel == null) {
            return 0;
        }
        if (++count >= changeCount) {
            count = 0;
            direction = new Random().nextInt(directCount);
        }
        int[] newXAndYAndD = getNewXAndYAndD(x, y, direction, speed);
        if (gamePanel.getCurLevels() == 2) {
            newXAndYAndD = getNewXAndYAndD(x, y, GameUtil.down, speed2, 1);
        }
        if (newXAndYAndD == null || newXAndYAndD.length < 3) {
            return 0;
        }

        x = newXAndYAndD[0];
        y = newXAndYAndD[1];
        direction = newXAndYAndD[2];

        if (gamePanel.getCurLevels() == 2 && direction == GameUtil.none) {
            return -1;
        }

        return 1;
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed) {
        return getNewXAndYAndD(x, y, direction, speed, new int[]{boundX, boundY}, 0);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int disappear) {
        return getNewXAndYAndD(x, y, direction, speed, new int[]{boundX, boundY}, disappear);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int[] limit) {
        return getNewXAndYAndD(x, y, direction, speed, limit, 0);
    }

    public int[] getNewXAndYAndD(int x, int y, int direction, int speed, int[] limit, int disappear) {
        if (limit == null || limit.length < 2) {
            return null;
        }
        boolean changeDirection = false;
        switch (direction) {
            case 0:
                int nextYl = y - speed;
                if (nextYl >= 0 && nextYl <= limit[1]) {
                    y = nextYl;
                } else {
                    changeDirection = true;
                }
                break;
            case 1:
                int nextYr = y + speed;
                if (nextYr >= 0 && nextYr <= limit[1]) {
                    y = nextYr;
                } else {
                    changeDirection = true;
                }
                break;
            case 2:
                int nextXl = x - speed;
                if (nextXl >= 0 && nextXl <= limit[0]) {
                    x = nextXl;
                } else {
                    changeDirection = true;
                }
                break;
            case 3:
                int nextXr = x + speed;
                if (nextXr >= 0 && nextXr <= limit[0]) {
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
