package util;

import level.GameLevel;
import node.GameNode;
import node.PlayerNode;

import java.awt.*;
import java.awt.geom.QuadCurve2D;

public class UIUtil {

    // 绘制更精细的钩子头
    public static void drawHook(Graphics2D g2d, int x, int y, double angle, int hookSize) {
        int hookThickness = 4;  // 钩子头部粗细

        g2d.setColor(new Color(169, 169, 169)); // 银灰色钩子
        g2d.setStroke(new BasicStroke(hookThickness, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

        // 计算钩子的弧形路径
        double hookAngleRad = Math.toRadians(angle);
        int[] xPoints = new int[4];
        int[] yPoints = new int[4];

        // 钩子的主体部分
        xPoints[0] = x;
        yPoints[0] = y;

        // 钩子的弯曲部分
        xPoints[1] = x + (int) (hookSize * Math.sin(hookAngleRad + Math.PI / 6));
        yPoints[1] = y + (int) (hookSize * Math.cos(hookAngleRad + Math.PI / 6));

        xPoints[2] = x + (int) (hookSize * 1.2 * Math.sin(hookAngleRad));
        yPoints[2] = y + (int) (hookSize * 1.2 * Math.cos(hookAngleRad));

        xPoints[3] = x + (int) (hookSize * Math.sin(hookAngleRad - Math.PI / 6));
        yPoints[3] = y + (int) (hookSize * Math.cos(hookAngleRad - Math.PI / 6));

        // 绘制钩子
        g2d.drawPolyline(xPoints, yPoints, 4);
    }

    public static void playerAttackUI(Graphics g) {
        PlayerNode player = GameUtil.gamePanel.getPlayerNode();
        if (player == null || g == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int x = player.getX();
        int y = player.getY();
        int width = player.getWidth();
        int height = player.getHeight();

        // 大炮参数
        int cannonLength = 25;
        int cannonWidth = 14;
        int baseSize = 20;

        // 设置渐变色
        GradientPaint metalGradient = new GradientPaint(
                x, y, new Color(100, 100, 100),
                x + cannonLength, y, new Color(180, 180, 180)
        );

        switch (player.getDirection()) {
            case GameUtil.up:
                // 炮座
                g2d.setColor(new Color(80, 80, 80));
                g2d.fillOval(x + width / 2 - baseSize / 2, y + 5, baseSize, baseSize);
                // 炮管
                g2d.setPaint(metalGradient);
                g2d.fillRect(x + width / 2 - cannonWidth / 2, y - cannonLength + 5, cannonWidth, cannonLength);
                // 炮口装饰
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x + width / 2 - cannonWidth / 2 - 2, y - cannonLength + 5, cannonWidth + 4, 3);
                break;

            case GameUtil.down:
                // 炮座
                g2d.setColor(new Color(80, 80, 80));
                g2d.fillOval(x + width / 2 - baseSize / 2, y + height - baseSize - 5, baseSize, baseSize);
                // 炮管
                g2d.setPaint(metalGradient);
                g2d.fillRect(x + width / 2 - cannonWidth / 2, y + height - 5, cannonWidth, cannonLength);
                // 炮口装饰
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x + width / 2 - cannonWidth / 2 - 2, y + height + cannonLength - 8, cannonWidth + 4, 3);
                break;

            case GameUtil.right:
                // 炮座(圆形)
                g2d.setColor(new Color(80, 80, 80));
                g2d.fillOval(x + width - baseSize - 5, y + height / 2 - baseSize / 2, baseSize, baseSize);
                // 炮管
                g2d.setPaint(metalGradient);
                g2d.fillRect(x + width - 5, y + height / 2 - cannonWidth / 2, cannonLength, cannonWidth);
                // 炮口装饰
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x + width + cannonLength - 8, y + height / 2 - cannonWidth / 2 - 2,
                        3, cannonWidth + 4);
                break;

            case GameUtil.left:
                // 炮座
                g2d.setColor(new Color(80, 80, 80));
                g2d.fillOval(x + 5, y + height / 2 - baseSize / 2, baseSize, baseSize);
                // 炮管
                g2d.setPaint(metalGradient);
                g2d.fillRect(x - cannonLength + 5, y + height / 2 - cannonWidth / 2, cannonLength, cannonWidth);
                // 炮口装饰
                g2d.setColor(new Color(60, 60, 60));
                g2d.fillRect(x - cannonLength + 5, y + height / 2 - cannonWidth / 2 - 2,
                        3, cannonWidth + 4);
                break;
        }
    }

    private static void drawBowCurve(Graphics2D g2d, int[] x, int[] y) {
        // 绘制弓身的三段贝塞尔曲线
        for (int i = 0; i < 3; i++) {
            int x1 = x[i];
            int y1 = y[i];
            int x2 = x[i + 1];
            int y2 = y[i + 1];
            int ctrlX = (x1 + x2) / 2;
            int ctrlY = (y1 + y2) / 2;

            QuadCurve2D curve = new QuadCurve2D.Float(x1, y1, ctrlX, ctrlY, x2, y2);
            g2d.draw(curve);
        }
    }

    public static void enemyAttackUI(Graphics g, GameNode enemy) {
        if (enemy == null || g == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
}