package gomokucontest;

public class WorkThread extends Thread {

    private final int gameCount;
    private final GobanPanel gobanPanel;

    public WorkThread(GobanPanel gobanPanel,int gameCount){
        super();
        this.gobanPanel = gobanPanel;
        this.gameCount = gameCount;
    }

    public void run(){
        for(int i=1;i<=gameCount;i++){
            gobanPanel.playerBLACK.gameStart();
            gobanPanel.playerWHITE.gameStart();
            GomokuContest.getInstance().jProgressBar.setValue(i);
            gobanPanel.clearGoban();
            gobanPanel.placeStone((int)(3+(gobanPanel.getGobanSize()-6)*Math.random()), (int)(3+(gobanPanel.getGobanSize()-6)*Math.random()));
            int gameEnd = 0;
            while(gameEnd == 0){
                gobanPanel.playerWHITE.doMove(GobanPanel.WHITE);
                gameEnd = GomokuContest.getInstance().isGameEnd();
                if (gameEnd == 0){
                    gobanPanel.playerBLACK.doMove(GobanPanel.BLACK);
                    gameEnd = GomokuContest.getInstance().isGameEnd();
                }
            }

            if (gameEnd == GobanPanel.BLACK) {
                gobanPanel.playerBLACK.gameEnd(true);
                gobanPanel.playerWHITE.gameEnd(false);
            }
            else if (gameEnd == GobanPanel.WHITE) {
                gobanPanel.playerBLACK.gameEnd(false);
                gobanPanel.playerWHITE.gameEnd(true);
            }
        }
        gobanPanel.repaint();
    } 
}
