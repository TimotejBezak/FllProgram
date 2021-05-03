package Vyjazdy;

import java.util.HashMap;

import Robot.PIDController;
import pack.Vyjazd;

public class DruhyVyjazd extends Vyjazd {
	
	public DruhyVyjazd() {
		super("Druhy Vyjazd", new HashMap<String, PIDController>(){
			{
				float nieco = 1.0f;//
				float PD = 1.2f;//0.044f;0.023 a i=1.5      i=5, PD = 0.39            PD 10 bez i
				put("dopreduLavy", new PIDController((0.2/400)*PD,(200/400)*nieco,(250/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.2/400)*PD,(-200/400)*nieco,(-250/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
				put("dozaduLavy", new PIDController((0.1/400)*PD,(100/400)*nieco,(125/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dozaduPravy", new PIDController((-0.1/400)*PD,(-100/400)*nieco,(-125/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}
		});
	}
	

	@Override
	protected void Spusti() throws InterruptedException {
		// TODO Auto-generated method stub
		//Delay.msDelay(5000);
		//robot.OtocPredny(720, true);
		
		robot.lineFollowerPravy(250, 2.00, 300, 300);
		
		
		//for(int i=0;i<10;i++) {
		//	robot.otocitPoUhol(400, 800, 90);
		//	robot.otocitPoUhol(-400, -800, 0);
		//}
		
		
		//robot.dopredu(1.94, 600);
	}

}
