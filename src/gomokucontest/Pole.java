package gomokucontest;
/**
 * Pole - game board
 */
public class Pole {

    /**
     * instance - To access the board from other classes
     */
    public static Pole instance;

    /**
     * n - board size (15 default)
     */
    public int n;                  //размерность поля

    /**
     * pole[][] - board
     */
    public byte pole[][];   //само поле

    /**
     *
     */
    public boolean game_end = false;
    public int i_end_n = 0;
    public int j_end_n = 0;
    public int i_end_k = 0;
    public int j_end_k = 0;
    public String win = "";

    public Pole(int n) {
        instance=this;
        this.n = n;
        pole = new byte[n][n];
        new_game();
    }

    /**
     * Start new game and clear board
     */
    public void new_game() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                pole[i][j] = 0;
            }
        }
        game_end = false;
        i_end_n = 0;
        j_end_n = 0;
        i_end_k = 0;
        j_end_k = 0;
        win = "";
    }

    /**
     * Checks the end of the game
     * @param c - who made a move? 1-x, 2-o
     * @return true if there is a line of five
     */

    public boolean game_end(byte c) {
        boolean end = false;
        //проверка на заполненность всего поля
        boolean vse_zapolneno = true;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (pole[i][j] == 0) {
                    vse_zapolneno = false;
                    break;
                }
                if (vse_zapolneno == false) {
                    break;
                }
            }
        }
        if (vse_zapolneno) {
            end = true;
            win = "no";
            game_end = true;
            return end;
        }

        int len;
        if (c == 1) {
            win = "x";
        }
        if (c == 2) {
            win = "o";
        }
        //проверка по i (по столбцам)
        for (int i = 0; i < n; i++) {
            len = 0;
            for (int j = 0; j < n; j++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }
        //проверка по j	(по строкам)
        for (int j = 0; j < n; j++) {
            len = 0;
            for (int i = 0; i < n; i++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }
        //прверка по диагонали j++  i++
        for (int k = 0; k < n - 3; k++) {
            len = 0;
            for (int j = k, i = 0; i < n && j < n; j++, i++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }
        for (int k = 0; k < n - 3; k++) {
            len = 0;
            for (int j = 0, i = k; i < n; j++, i++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }
        //проверка по диагонали i++  j--
        for (int k = 4; k < n; k++) {
            len = 0;
            for (int i = k, j = 0; i >= 0; i--, j++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }
        for (int k = 1; k < n - 3; k++) {
            len = 0;
            for (int i = k, j = n - 1; i < n; j--, i++) {
                if (pole[i][j] == c) {
                    if (len == 0) {
                        i_end_n = i;
                        j_end_n = j;
                    }
                    len++;
                } else {
                    len = 0;
                }
                if (len >= 5) {
                    i_end_k = i;
                    j_end_k = j;
                    game_end = true;
                    end = true;
                    return end;
                }
            }
        }

        return end;
    }
}
