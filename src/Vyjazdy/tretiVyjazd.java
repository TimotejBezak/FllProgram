package Vyjazdy;

import java.util.HashMap;

import Robot.PIDController;
import pack.Vyjazd;

public class tretiVyjazd extends Vyjazd {

	public tretiVyjazd() {
		super("Treti Vyjazd", new HashMap<String, PIDController>(){
			{
				float nieco = 1.0f;//
				float PD = 1.2f;//0.044f;0.023 a i=1.5      i=5, PD = 0.39            PD 10 bez i
				put("dopreduLavy", new PIDController((0.68/400)*PD*nieco,(11.0/400)*nieco,(50.0/400)*PD*nieco));//1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.68/400)*PD*nieco,(-11.0/400)*nieco,(-50.0/400)*PD*nieco));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}
		});
	}
	
	@Override
	protected void Spusti() throws InterruptedException {
		
		//while(true) {
		//	System.out.println("farba je "+robot.getLavyFarba());
		//}
		
		robot.dopredu(2,600,520);
		
		//robot.otocitPoUhol(400, 800, 90);
		
		
		/*robot.PIDdopredu(0.5, 800);
		robot.otocitPoUhol(400, 800, 90);
		robot.PIDdopredu(0.5, 800);
		robot.otocitPoUhol(400, 800, 180);
		robot.PIDdopredu(0.5, 800);
		robot.otocitPoUhol(400, 800, 270);
		robot.PIDdopredu(0.5, 800);
		robot.otocitPoUhol(400, 800, 0);*/
	}
}