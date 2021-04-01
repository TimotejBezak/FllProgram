package Vyjazdy;

import java.util.HashMap;

import Robot.PIDController;
import pack.Vyjazd;

public class DruhyVyjazd extends Vyjazd {
	
	public DruhyVyjazd() {
		super("Druhy Vyjazd", new HashMap<String, PIDController>(){
			{
				put("dopreduLavy", new PIDController(3.2, 2, 0));
				put("dopreduPravy", new PIDController(-3.2, -2, 0));
			}
		});
	}
	

	@Override
	protected void Spusti() throws InterruptedException {
		// TODO Auto-generated method stub
		//Delay.msDelay(5000);
		//robot.OtocPredny(720, true);
		
		
		
		
		//for(int i=0;i<10;i++) {
		//	robot.otocitPoUhol(400, 800, 90);
		//	robot.otocitPoUhol(-400, -800, 0);
		//}
		
		
		robot.dopredu(1.94, 800);
	}

}
