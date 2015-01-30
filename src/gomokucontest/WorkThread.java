package gomokucontest;

public class WorkThread extends Thread {

	public WorkThread(){
		super();
	}

	public void run(){
		for(int i=1;i<=GomokuContest.instance.gobanPanel.games_count;i++)	{
			GomokuContest.instance.gobanPanel.new_game();
			GomokuContest.instance.get_jProgressBar1().setValue(i);
			GomokuContest.instance.gobanPanel.pole.pole[(byte)(1+(GomokuContest.instance.gobanPanel.pole.n-3)*Math.random())]
                    [(byte)(1+(GomokuContest.instance.gobanPanel.pole.n-3)*Math.random())]=GomokuContest.instance.gobanPanel.cpu1_xo;
			cpu_cpu();
			GomokuContest.instance.gobanPanel.repaint();
		}
	}

	public void cpu_cpu(){
		while(!GomokuContest.instance.gobanPanel.pole.game_end){
			cpu_hod("cpu2",GomokuContest.instance.gobanPanel.cpu2_xo);
			GomokuContest.instance.gobanPanel.pole.game_end(GomokuContest.instance.gobanPanel.cpu2_xo);
			if (GomokuContest.instance.gobanPanel.pole.game_end) break;
			cpu_hod("cpu1",GomokuContest.instance.gobanPanel.cpu1_xo);
			GomokuContest.instance.gobanPanel.pole.game_end(GomokuContest.instance.gobanPanel.cpu1_xo);
		}
		if (GomokuContest.instance.gobanPanel.pole.win.equals("x")) GomokuContest.instance.gobanPanel.play1_win++;
		if (GomokuContest.instance.gobanPanel.pole.win.equals("o")) GomokuContest.instance.gobanPanel.play2_win++;
		GomokuContest.instance.setshet(GomokuContest.instance.gobanPanel.play1_win+":"+GomokuContest.instance.gobanPanel.play2_win);
		if (GomokuContest.instance.gobanPanel.play2_win!=0){
			//System.out.println((double)mf.play1_win/mf.play2_win);
		}
	}
	public void cpu_hod(String cpu,byte xo){
		String alg="";
		if (cpu.equals("cpu1")){
			alg=GomokuContest.instance.gobanPanel.cpu1_alg;
		} else{
			alg=GomokuContest.instance.gobanPanel.cpu2_alg;
		}

		if (alg.equals("xoxo")) GomokuContest.instance.gobanPanel.xoxo1.doMove(xo);
		if (alg.equals("xoxo2")) GomokuContest.instance.gobanPanel.xoxo2.doMove(xo);
	}
}
