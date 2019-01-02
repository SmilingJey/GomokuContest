package gomokucontest;

public interface IGomokuEngine {

    public void init(GobanPanel gbanPanel);
    public void doMove(int stoneColor);
    public void gameStart();
    public void gameEnd(boolean win);
}
