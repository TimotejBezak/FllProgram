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
			}//0.05,0,1.25
		});
	}
	
	@Override
	protected void Spusti() throws InterruptedException {
		// TODO Auto-generated method stub
		
		//robot.dopredu(2.00);
		
		robot.dopredu(0.485,700);
		robot.dopredu(-0.25,1000);//0.29
		
		
		
		//robot.PIDdopredu2(1.9, 800);
	}

}
