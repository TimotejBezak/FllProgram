package Vyjazdy;

import pack.Vyjazd;

public class DruhyVyjazd extends Vyjazd {
	
	public DruhyVyjazd() {
		super("Druhy Vyjazd");
	}

	@Override
	protected void Spusti() {
		// TODO Auto-generated method stub
		//Delay.msDelay(5000);
		//robot.OtocPredny(720, true);
		
		
		
		
		//for(int i=0;i<10;i++) {
		//	robot.otocitPoUhol(400, 800, 90);
		//	robot.otocitPoUhol(-400, -800, 0);
		//}
		
		
		robot.PIDdopredu(1.94, 800);
	}

}
