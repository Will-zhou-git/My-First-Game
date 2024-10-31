package level.gamelevels;

import game.GamePanel;
import level.GameLevel;
import lombok.Data;
import util.GameUtil;

@Data
public class GameLevel1 extends GameLevel {
    //关卡信息
    private int levelTime = 1;

    @Override
    public int getCurLevel() {
        return 1;
    }

    @Override
    public void enterNextLevel(){
        GamePanel gamePanel = GameUtil.gamePanel;
        gamePanel.setGameLevel(new GameLevel2());

        super.enterNextLevel();
    }
}
