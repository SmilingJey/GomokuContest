package gomokucontest;

public interface IGomokuEngine {
    /**
     * Place a cross or a toe
     * @param xo - who made a move? 1-x, 2-o
     */

    public void doMove(byte xo);
}
