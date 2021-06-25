package Vyjazdy;

import java.util.HashMap;

import Robot.PIDController;
import pack.Vyjazd;

public class stvrtyVyjazd extends Vyjazd {

	public stvrtyVyjazd() {
		super("Stvrty Vyjazd", new HashMap<String, PIDController>(){
			{
				put("dopreduLavy", new PIDController(0.05/500, 0.0, 1.25/500));//1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0
				put("dopreduPravy", new PIDController(-0.05/500, -0.0, -1.25/500));//-1,-0.6,-2.5// d samostatne 1.4
//				put("dozaduLavy", new PIDController((0.1/400)*PD,(100/400)*nieco,(125/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
//				put("dozaduPravy", new PIDController((-0.1/400)*PD,(-100/400)*nieco,(-125/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65

			}//0.05,0,1.25
		});
	}
	
	@Override
	protected void Spusti() throws InterruptedException {
		// TODO Auto-generated method stub
		
		//robot.dopredu(2.00);
		
		robot.dopredu(0.485,700);
		robot.dozadu(0.22,800);//0.37.700
		//robot.otocitPoUhol(400,-400,-90);
		
		
		//robot.PIDdopredu2(1.9, 800);
	}

}
