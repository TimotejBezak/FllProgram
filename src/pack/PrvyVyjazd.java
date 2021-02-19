package pack;

import lejos.utility.Delay;

public class PrvyVyjazd extends Vyjazd {

	public PrvyVyjazd() {
		super("Prvy Vyjazd");
	}
	
	protected void Spusti() {
		//robot.dopredu(0.678, 800);
		//robot.otocitPoUhol(250, 750, 79);
		//Delay.msDelay(2000);
		//System.out.println("uhol: " + robot.getUhol());
		//robot.dopredu(0.1, 800);
		
		//robot.otocitPoUhol(400, 800, 45);
		//robot.otocitPoUhol(400, 800, 45);
//		robot.dopredu(1, 800);
//		robot.otocitPoUhol(400, 800, 90);
		
		
		for(int i=1;i<=4;i++) {
			robot.dopredu(0.5, 800);
			robot.otocitPoUhol(200, 800, 90);
		}
//		
		
		//Delay.msDelay(2000);
		
		
		//robot.dopredu(-0.1,800);
		//robot.dopreduZabacavo(200,-200,-90);
		//robot.dopredubezgyra(-1, 800);
//		robot.zrychlovanie(0.1,1);
//      robot.zrychlovanie(-100,5000);
//		robot.Dopredu(5000);
	}

}
