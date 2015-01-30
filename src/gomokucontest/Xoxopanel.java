package gomokucontest;

import javax.swing.JPanel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Graphics;
import java.awt.Color;

public class Xoxopanel extends JPanel {

	public int n;
	public int x_n=5;
	public int y_n=5;
	public int shag=20;
	public IGomokuEngine xoxo1; 
	public IGomokuEngine xoxo2; 

	public byte humen1_xo=1;
	public byte humen2_xo=2;

	public int games_count=1;
	public byte cpu1_xo=1;
	public String cpu1_alg;
	public boolean cpu1_think=false;
	public byte cpu2_xo=2;
	public String cpu2_alg;
	public boolean cpu2_think=false;

	public String play1;
	public String play2;
	public byte play1_xo=1;
	public byte play2_xo=2;
	public int play1_win=0;
	public int play2_win=0;

	public byte currentxo=1;

	public Pole pole;
	public String game_type="";  // @jve:decl-index=0:



	private static final long serialVersionUID = 1L;
	/**
	 * This is the default constructor
	 */

	public Xoxopanel(int n) {
		super();
		this.n=n;
		pole=new Pole(n);
		initialize();
	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
		//this.setSize(320, 320);
		this.addMouseListener(new MouseAdapter(){
			public void mousePressed(MouseEvent me){
				mouse_click(me);
			}
		});
	}
	public void new_game(){
		pole.new_game();
		GomokuContest.instance.get_jProgressBar1().setValue(0);
		repaint();
	}

	/** Страт игры */
	public void start_game(){
		game_type="";

		if (play1=="Человек") {
			game_type="humen";
			humen1_xo=play1_xo;
		} else{
			cpu1_alg="xoxo";
			game_type="cpu";
			cpu1_xo=play1_xo;
		}
		if (play2=="Человек") {
			game_type=game_type+"humen";
			humen2_xo=play2_xo;
		} else{
			cpu2_alg="xoxo2";
			game_type=game_type+"cpu";
			cpu2_xo=play2_xo;
		}

		if (game_type.equals("humenhumen")){

		}
		if (game_type.equals("humencpu")){

		}

		if (game_type.equals("cpuhumen")){
			pole.pole[(byte)(1+12*Math.random())][(byte)(1+12*Math.random())]=cpu1_xo;
			repaint();
		}
		if (game_type.equals("cpucpu")){
			WorkThread workthread=new WorkThread();
			workthread.start();
		}

	}

	public void mouse_click(MouseEvent me){
		if (!game_type.equals("cpucpu")){
			int x=me.getX();
			int y=me.getY();
			byte x_count=(byte)((x-x_n)/shag);
			byte y_count=(byte)((y-y_n)/shag);
			if ((x_count>=0) && (x_count<=n-1) && (y_count>=0) && (y_count<=n-1) &&
				(pole.pole[x_count][y_count]==0)&& (!pole.game_end) && (!cpu1_think || !cpu2_think)){
					if (game_type.equals("humencpu")){
						pole.pole[x_count][y_count]=humen1_xo;
						pole.game_end(humen1_xo);
					}
					if (game_type.equals("cpuhumen")){
						pole.pole[x_count][y_count]=humen2_xo;
						pole.game_end(humen2_xo);
					}
					if (game_type.equals("humenhumen")){
						pole.pole[x_count][y_count]=currentxo;
						pole.game_end(currentxo);
						currentxo=(byte)(currentxo==1?2:1);
					}
					repaint();
				if ((!pole.game_end) && (game_type.equals("humencpu") || game_type.equals("cpuhumen"))){
					if (game_type.equals("humencpu")){
						cpu_hod("cpu2",cpu2_xo);
						pole.game_end(cpu2_xo);
					}
					if (game_type.equals("cpuhumen")){
						cpu_hod("cpu1",cpu1_xo);
						pole.game_end(cpu1_xo);
					}
					repaint();
				}
			}
		}
	}
	public void cpu_hod(String cpu,byte xo){
		String alg="";
		if (cpu.equals("cpu1")){
			alg=cpu1_alg;
			cpu1_think=true;
		} else{
			alg=cpu2_alg;
			cpu2_think=true;
		}

		if (alg.equals("xoxo")) xoxo1.doMove(xo);
		if (alg.equals("xoxo2")) xoxo2.doMove(xo);

		if (cpu.equals("cpu1")) cpu1_think=false;
		else cpu2_think=false;
	}

	public void draw_clear(Graphics g){
		g.setColor(new Color(255,255,255));
		g.fillRect(0, 0, this.getWidth(),this.getHeight());
	}
	public void draw_grid(Graphics g){
		int x_k=x_n+n*shag;
		int y_k=y_n+n*shag;
		g.setColor(new Color(0));
		for (int i=x_n;i<=x_k;i=i+shag){
			g.drawLine(i, y_n, i, y_k);
		}

		for (int i=y_n;i<=y_k;i=i+shag){
			g.drawLine(x_n, i, x_k, i);
		}
	}
	public void draw_xo(Graphics g){
		for(int i=0;i<n;i++){
			for(int j=0;j<n;j++){
				if (pole.pole[i][j]==2) {
					g.setColor(new Color(0,0,255));
					g.drawOval(x_n+shag*i+3,y_n+shag*j+3 ,shag-6, shag-6);
				}
				if (pole.pole[i][j]==1) {
					g.setColor(new Color(255,0,0));
					g.drawLine(x_n+shag*i+2, y_n+shag*j+2, x_n+shag*(i+1)-2, y_n+shag*(j+1)-2);
					g.drawLine(x_n+shag*(i+1)-2, y_n+shag*j+2, x_n+shag*i+2, y_n+shag*(j+1)-2);
				}
			}
		}
	}
	public void draw_end_line(Graphics g){
		if ((pole.game_end) && (!pole.win.equals("no"))){
			g.setColor(new Color(0,255,0));
			g.drawLine((int)(x_n+pole.i_end_n*shag+shag*0.5),(int)(y_n+pole.j_end_n*shag+shag*0.5),
					   (int)(x_n+pole.i_end_k*shag+shag*0.5),(int)(y_n+pole.j_end_k*shag+shag*0.5));
		}
	}
	public void draw_end_mess(Graphics g){
		if (pole.game_end){
		g.setColor(new Color(0,0,255));
		g.drawString(pole.win + " win!!!",shag*n-140,10);
		}
	}
	public void paint(Graphics g){
		draw_clear(g);
		draw_grid(g);
		draw_xo(g);
		draw_end_line(g);
		draw_end_mess(g);
	}

}  //  @jve:decl-index=0:visual-constraint="105,9"
