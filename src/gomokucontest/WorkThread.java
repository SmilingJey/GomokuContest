package gomokucontest;

public class WorkThread extends Thread {

	public WorkThread(){
		super();
	}

	public void run(){
		for(int i=1;i<=GomokuContest.instance.xoxopanel.games_count;i++)	{
			GomokuContest.instance.xoxopanel.new_game();
			GomokuContest.instance.get_jProgressBar1().setValue(i);
			GomokuContest.instance.xoxopanel.pole.pole[(byte)(1+(GomokuContest.instance.xoxopanel.pole.n-3)*Math.random())]
                    [(byte)(1+(GomokuContest.instance.xoxopanel.pole.n-3)*Math.random())]=GomokuContest.instance.xoxopanel.cpu1_xo;
			cpu_cpu();
			GomokuContest.instance.xoxopanel.repaint();
		}
	}

	public void cpu_cpu(){
		while(!GomokuContest.instance.xoxopanel.pole.game_end){
			cpu_hod("cpu2",GomokuContest.instance.xoxopanel.cpu2_xo);
			GomokuContest.instance.xoxopanel.pole.game_end(GomokuContest.instance.xoxopanel.cpu2_xo);
			if (GomokuContest.instance.xoxopanel.pole.game_end) break;
			cpu_hod("cpu1",GomokuContest.instance.xoxopanel.cpu1_xo);
			GomokuContest.instance.xoxopanel.pole.game_end(GomokuContest.instance.xoxopanel.cpu1_xo);
		}
		if (GomokuContest.instance.xoxopanel.pole.win.equals("x")) GomokuContest.instance.xoxopanel.play1_win++;
		if (GomokuContest.instance.xoxopanel.pole.win.equals("o")) GomokuContest.instance.xoxopanel.play2_win++;
		GomokuContest.instance.setshet(GomokuContest.instance.xoxopanel.play1_win+":"+GomokuContest.instance.xoxopanel.play2_win);
		if (GomokuContest.instance.xoxopanel.play2_win!=0){
			//System.out.println((double)mf.play1_win/mf.play2_win);
		}
	}
	public void cpu_hod(String cpu,byte xo){
		String alg="";
		if (cpu.equals("cpu1")){
			alg=GomokuContest.instance.xoxopanel.cpu1_alg;
		} else{
			alg=GomokuContest.instance.xoxopanel.cpu2_alg;
		}

		if (alg.equals("xoxo")) GomokuContest.instance.xoxopanel.xoxo1.doMove(xo);
		if (alg.equals("xoxo2")) GomokuContest.instance.xoxopanel.xoxo2.doMove(xo);
	}
}
