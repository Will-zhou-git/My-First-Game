package node;

import lombok.Data;
import util.GameUtil;

import java.awt.*;

@Data
public class BulletNode extends GameNode{
    private int x;
    private int y;
    private int direction;
    private int speed;
    private boolean isPlayerBullet;
    private int size = 8;

    public BulletNode(int x, int y, int direction, int speed, boolean isPlayerBullet) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.speed = speed;
        this.isPlayerBullet = isPlayerBullet;
    }

    @Override
    public int move() {
        // [修改] 使用三角函数实现更平滑的移动
        double angle = 0;
        switch (direction) {
            case GameUtil.up:
                angle = -Math.PI/2;
                break;
            case GameUtil.down:
                angle = Math.PI/2;
                break;
            case GameUtil.left:
                angle = Math.PI;
                break;
            case GameUtil.right:
                angle = 0;
                break;
        }

        x += Math.cos(angle) * speed;
        y += Math.sin(angle) * speed;
        return 1;
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x - size/2, y - size/2, size, size);
    }

    @Override
    public void draw(Graphics g) {
        // [修改] 根据方向绘制更细长的子弹
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (direction == GameUtil.up || direction == GameUtil.down) {
            // 垂直方向的子弹
            g2d.fillOval(x - size/4, y - size/2, size/2, size);
        } else {
            // 水平方向的子弹
            g2d.fillOval(x - size/2, y - size/4, size, size/2);
        }
    }
} 