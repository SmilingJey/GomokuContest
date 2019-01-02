package gomokualgorithm;

import gomokucontest.IGomokuEngine;
import gomokucontest.GobanPanel;

public class GomokuAlgorithm1 implements IGomokuEngine{

    private final static int cost_2 = 1;
    private final static int cost_x1 = 3;
    private final static int cost_x0 = 8;
    private final static int cost_xx1 = 20;
    private final static int cost_xx0 = 250;
    private final static int cost_xxx1 = 30;
    private final static int cost_xxxx1 = 500;
    private final static int cost_xxx0 = 6000;
    private final static int cost_xxxx0 = 30000;
    private final static int cost_win = 40000;
    private final static int cost_border = 0;
    private int n;
    private double cost[][];
    private int stoneColor;
    private GobanPanel gobanPanel = null;

    public GomokuAlgorithm1() {
    }
    
    public void init(GobanPanel gobanPanel){
        this.gobanPanel = gobanPanel;
        n = gobanPanel.getGobanSize();
        cost = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                cost[i][j] = 0;
            }
        }
    }

    public void doMove(int stoneColor) {
        if (gobanPanel == null){
            return;
        }
        this.stoneColor = stoneColor;
        getCostArray();
        double max_cost = -10000;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] != 0) {
                    if (cost[i][j] > max_cost) {
                        max_cost = cost[i][j];
                    }
                }
            }
        }
        int cost_max[][] = new int[n * n][2];
        int count_max = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (cost[i][j] == max_cost) {
                    cost_max[count_max][0] = i;
                    cost_max[count_max][1] = j;
                    count_max++;
                }
            }
        }

        int n = (int) Math.round((count_max - 1) * Math.random());
        gobanPanel.placeStone(cost_max[n][0], cost_max[n][1]);
    }

    private void getCostArray() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (gobanPanel.getGobanIJ(i, j) == 0) {
                    if (i == 0 || i == n - 1 || j == 0 || j == n - 1) {
                        cost[i][j] = calcCellCost(i, j) - cost_border;
                    } else {
                        cost[i][j] = calcCellCost(i, j);
                    }
                } else {
                    cost[i][j] = 0;
                }
            }
        }
    }

    private double calcCellCost(int i, int j) {
        double cost = 0;
        double me_cost = calcCost(i, j, stoneColor);
        double em_cost = calcCost(i, j, stoneColor == 2 ? 1 : 2);
        cost = 1 * me_cost + 1 * em_cost;
        return cost;
    }

    private double calcCost(int x, int y, int c) {
        int i = 0, j = 0;
        double cost = 0;
        int len_h = 0, len_v = 0, len_dl = 0, len_dr = 0;
        int zakr_h = 0, zakr_v = 0, zakr_dl = 0, zakr_dr = 0;
        //влево по горизонтали
        i = x;
        j = y;
        do {
            len_h++;
            if (i == 0) {
                break;
            }
            i--;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((x == 0) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_h++;
        }

        //вправо по горизонтали
        i = x;
        j = y;
        do {
            len_h++;
            if (i == n - 1) {
                break;
            }
            i++;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((x == n - 1) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_h++;
        }

        //вверх по вертикали
        i = x;
        j = y;
        do {
            len_v++;
            if (j == 0) {
                break;
            }
            j--;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == 0) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_v++;
        }

        //вниз по вертикали
        i = x;
        j = y;
        do {
            len_v++;
            if (j == n - 1) {
                break;
            }
            j++;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == n - 1) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_v++;
        }

        //влево-вверх
        i = x;
        j = y;
        do {
            len_dl++;
            if (j == 0) {
                break;
            }
            if (i == 0) {
                break;
            }
            j--;
            i--;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == 0) || (x == 0) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_dl++;
        }

        //вправо-вниз
        i = x;
        j = y;
        do {
            len_dl++;
            if (j == n - 1) {
                break;
            }
            if (i == n - 1) {
                break;
            }
            j++;
            i++;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == n - 1) || (x == n - 1) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_dl++;
        }

        //влево-вниз
        i = x;
        j = y;
        do {
            len_dr++;
            if (j == n - 1) {
                break;
            }
            if (i == 0) {
                break;
            }
            j++;
            i--;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == n - 1) || (x == 0) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_dr++;
        }
        //вправо - вверх
        i = x;
        j = y;
        do {
            len_dr++;
            if (j == 0) {
                break;
            }
            if (i == n - 1) {
                break;
            }
            j--;
            i++;
        } while (c == gobanPanel.getGobanIJ(i, j));
        if ((y == 0) || (x == n - 1) || (gobanPanel.getGobanIJ(i, j) == (c == 2 ? 1 : 2))) {
            zakr_dr++;
        }
        cost = getLineCost(len_h - 1, zakr_h) * possibleFive(x, y, c, 0)
                + getLineCost(len_v - 1, zakr_v) * possibleFive(x, y, c, 1)
                + getLineCost(len_dl - 1, zakr_dl) * possibleFive(x, y, c, 2)
                + getLineCost(len_dr - 1, zakr_dr) * possibleFive(x, y, c, 3) + 1;

        /*System.out.println("x="+x+" y="+y+" c="+ c);
         System.out.println("Горизонталь: "+get_cost(len_h-1,zakr_h)+" * "+can_five(x,y,c,0));
         System.out.println("Вертикаль: "+get_cost(len_v-1,zakr_v)+" * "+can_five(x,y,c,1));
         System.out.println("Диагональ \\: "+get_cost(len_dl-1,zakr_dl)+" * "+can_five(x,y,c,2));
         System.out.println("Диагональ /: "+get_cost(len_dr-1,zakr_dr)+" * "+can_five(x,y,c,3));*/
        cost = cost * cost;
        return cost;
    }

    private int possibleFive(int x, int y, int c, int h) {
        int i = 0, j = 0;
        int length = 0;
        int rez = 0;
        int prot_c = (c == 2) ? 1 : 2;

        if (h == 0) {
            i = x;
            j = y;
            do {
                length++;
                if (i == 0) {
                    break;
                }
                i--;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
            i = x;
            j = y;
            do {
                length++;
                if (i == n - 1) {
                    break;
                }
                i++;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
        }

        if (h == 1) {
            i = x;
            j = y;
            do {
                length++;
                if (j == 0) {
                    break;
                }
                j--;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
            i = x;
            j = y;
            do {
                length++;
                if (j == n - 1) {
                    break;
                }
                j++;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
        }

        if (h == 2) {
            i = x;
            j = y;
            do {
                length++;
                if (j == 0) {
                    break;
                }
                if (i == 0) {
                    break;
                }
                j--;
                i--;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
            i = x;
            j = y;
            do {
                length++;
                if (j == n - 1) {
                    break;
                }
                if (i == n - 1) {
                    break;
                }
                j++;
                i++;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
        }

        if (h == 3) {
            i = x;
            j = y;
            do {
                length++;
                if (j == n - 1) {
                    break;
                }
                if (i == 0) {
                    break;
                }
                j++;
                i--;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
            i = x;
            j = y;
            do {
                length++;
                if (j == 0) {
                    break;
                }
                if (i == n - 1) {
                    break;
                }
                j--;
                i++;
            } while (length < 5 && gobanPanel.getGobanIJ(i, j) != prot_c);
        }
        if (length > 4) {
            rez = 1;
        }
        //System.out.println("x="+x+" y="+y+" c="+c+" h="+ h +" len="+length+" rez="+rez);
        return rez;
    }

    private int getLineCost(int lineLength, int closedCellCont) {
        int lineCost = 0;
        switch (lineLength) {
            case 1:
                if (closedCellCont == 0) {
                    lineCost = cost_x0;
                }
                if (closedCellCont == 1) {
                    lineCost = cost_x1;
                }
                if (closedCellCont == 2) {
                    lineCost = cost_2;
                }
                break;
            case 2:
                if (closedCellCont == 0) {
                    lineCost = cost_xx0;
                }
                if (closedCellCont == 1) {
                    lineCost = cost_xx1;
                }
                if (closedCellCont == 2) {
                    lineCost = cost_2;
                }
                break;
            case 3:
                if (closedCellCont == 0) {
                    lineCost = cost_xxx0;
                }
                if (closedCellCont == 1) {
                    lineCost = cost_xxx1;
                }
                if (closedCellCont == 2) {
                    lineCost = cost_2;
                }
                break;
            case 4:
                if (closedCellCont == 0) {
                    lineCost = cost_xxxx0;
                }
                if (closedCellCont == 1) {
                    lineCost = cost_xxxx1;
                }
                if (closedCellCont == 2) {
                    lineCost = cost_2;
                }
                break;
            case 5:
                lineCost = cost_win;
                break;
            default:
                lineCost = 0;
        }
        return lineCost;
    }

    public void gameStart() {
        
    }
    
    public void gameEnd(boolean win) {
        
    }
}
