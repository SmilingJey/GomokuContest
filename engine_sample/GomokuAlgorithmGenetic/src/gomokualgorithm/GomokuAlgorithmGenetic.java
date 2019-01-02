package gomokualgorithm;

import gomokucontest.IGomokuEngine;
import gomokucontest.GobanPanel;
import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GomokuAlgorithmGenetic implements IGomokuEngine{

    private double cost_2 = 100;
    private double cost_x1 = 100;
    private double cost_x0 = 100;
    private double cost_xx1 = 100;
    private double cost_xx0 = 100;
    private double cost_xxx1 = 100;
    private double cost_xxxx1 = 100;
    private double cost_xxx0 = 100;
    private double cost_xxxx0 = 100;
    private double cost_win = 100;
    private double cost_border = 100;
    private double cost_default = 100;
    private double balance = 1;
    private int n;
    private double cost[][];
    private int stoneColor;
    private GobanPanel gobanPanel = null;

    public GomokuAlgorithmGenetic() {
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
        double em_cost = calcCost(i, j, stoneColor == GobanPanel.WHITE ? GobanPanel.BLACK : GobanPanel.WHITE);
        cost = (1 + balance) * me_cost + (1 - balance) * em_cost;
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
        //cost = cost * cost;
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

    private double getLineCost(int lineLength, int closedCellCont) {
        double lineCost = 0;
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
                lineCost = cost_default;
        }
        return lineCost;
    }

    public void gameStart() {
        loadconf();
    }

    public void gameEnd(boolean win) {
       if (win) saveconf();
    }
    
    private void loadconf(){
        try {
            InputStream propin = new FileInputStream(new File("GomokuAlgorithmGeneticConf"));
            Properties prop = new Properties();
            prop.load(propin);
            cost_2 = Double.parseDouble(prop.getProperty("cost_2"));
            cost_x1 = Double.parseDouble(prop.getProperty("cost_x1"));
            cost_x0 = Double.parseDouble(prop.getProperty("cost_x0"));
            cost_xx1 = Double.parseDouble(prop.getProperty("cost_xx1"));
            cost_xx0 = Double.parseDouble(prop.getProperty("cost_xx0"));
            cost_xxx1 = Double.parseDouble(prop.getProperty("cost_xxx1"));
            cost_xxxx1 = Double.parseDouble(prop.getProperty("cost_xxxx1"));
            cost_xxx0 = Double.parseDouble(prop.getProperty("cost_xxx0"));
            cost_xxxx0 = Double.parseDouble(prop.getProperty("cost_xxxx0"));
            cost_win = Double.parseDouble(prop.getProperty("cost_win"));
            cost_border = Double.parseDouble(prop.getProperty("cost_border"));
            cost_default = Double.parseDouble(prop.getProperty("cost_default"));  
            balance = Double.parseDouble(prop.getProperty("balance","0"));
            mutateconf(2);
            propin.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void mutateconf(int mutable){
        cost_2 += mutable*Math.random() - mutable/2;
        cost_x1 += mutable*Math.random() - mutable/2;
        cost_x0 += mutable*Math.random() - mutable/2;
        cost_xx1 += mutable*Math.random() - mutable/2;
        cost_xx0 += mutable*Math.random() - mutable/2;
        cost_xxx1 += mutable*Math.random() - mutable/2;
        cost_xxxx1 += mutable*Math.random() - mutable/2;
        cost_xxx0 += mutable*Math.random() - mutable/2;
        cost_xxxx0 += mutable*Math.random() - mutable/2;
        cost_win += mutable*Math.random() - mutable/2;
        cost_border += mutable*Math.random() - mutable/2;
        cost_default += mutable*Math.random() - mutable/2;
        balance += 0.01*Math.random() - 0.005;
    }
    
    private void saveconf(){
        try {
            Properties prop = new Properties();
            OutputStream propOut = new FileOutputStream(new File("GomokuAlgorithmGeneticConf"));
            prop.setProperty("cost_2",Double.toString(cost_2));
            prop.setProperty("cost_x1",Double.toString(cost_x1));
            prop.setProperty("cost_x0",Double.toString(cost_x0));
            prop.setProperty("cost_xx1",Double.toString(cost_xx1));
            prop.setProperty("cost_xx0",Double.toString(cost_xx0));
            prop.setProperty("cost_xxx1",Double.toString(cost_xxx1));
            prop.setProperty("cost_xxxx1",Double.toString(cost_2));
            prop.setProperty("cost_xxx0",Double.toString(cost_xxx0));
            prop.setProperty("cost_xxxx0",Double.toString(cost_xxxx0));
            prop.setProperty("cost_win",Double.toString(cost_win));
            prop.setProperty("cost_border",Double.toString(cost_border));
            prop.setProperty("cost_default",Double.toString(cost_default));
            prop.setProperty("balance",Double.toString(balance));
            prop.store(propOut, "GomokuAlgorithmGeneticConf");
            propOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }        
    }
    
}
