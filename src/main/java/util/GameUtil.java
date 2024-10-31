package util;

import game.GamePanel;
import lombok.Data;
import node.GameNode;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.util.*;
import java.awt.*;
import java.io.File;

@Data
public class GameUtil {

    public static final int none = -1;
    public static final int up = 0;
    public static final int down = 1;
    public static final int left = 2;
    public static final int right = 3;

    public static GamePanel gamePanel = new GamePanel();

    public static Image getNodeImage(String filePath) {
        Image image = null;
        try {
            image = ImageIO.read(new File(GameUtil.getResourcePath(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static Image getNodeImageByWebp(String filePath) {
        Image image = null;
        try {
            File imageFile = new File(GameUtil.getResourcePath(filePath));
            ImageInputStream inputStream = ImageIO.createImageInputStream(imageFile);
            ImageReader reader =  ImageIO.getImageReadersByFormatName("webp").next();;
            reader.setInput(inputStream);
            image = reader.read(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void generateRandomNodes(int nodeCount, String imagePath) {
        Image image = null;
        for (int i = 0; i < nodeCount; i++) {
            GameNode gameNode;
            if (image == null) {
                gameNode = new GameNode(imagePath);
                image = gameNode.getImage();
            } else {
                gameNode = new GameNode(image);
            }
            gamePanel.getNodes().add(gameNode);
        }
    }

    public static String getResourcePath(String name) {
        return Objects.requireNonNull(GameUtil.class.getClassLoader().getResource(name)).getPath();
    }
}
