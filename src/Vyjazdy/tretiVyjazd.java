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
				put("dopreduLavy", new PIDController((0.2/400)*PD,(200/400)*nieco,(250/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.2/400)*PD,(-200/400)*nieco,(-250/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
				put("dozaduLavy", new PIDController((0.1/400)*PD,(100/400)*nieco,(125/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dozaduPravy", new PIDController((-0.1/400)*PD,(-100/400)*nieco,(-125/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}
		});
	}
	
	@Override
	protected void Spusti() throws InterruptedException {
		//robot.dopredubezgyra(-1.20,350,0.996f);
		//robot.dopredubezgyra(-1.20, 350, 0.997f);
		
		//robot.dopredubezgyra(1.20, 400, 1.029f);
		
		
		
//		robot.dopredu(1.44,700);//idenie popri stene
//		robot.otocitPoUhol(100, 500, 87);
//		robot.dopredu(0.295,400);
//		robot.otocitPoUhol(-1100, -120, 180);
//		robot.dopredu(0.27,400);//prichadzanie k pneumatikam
		robot.OtocZadny(6*360);
		robot.dozadu(0.22, 400);
		robot.dopredu(0.18, 400);
		robot.dozadu(0.075, 300);
		robot.otocitPoUhol(300, -300, -45);
		
		
		
		
		
		
		
		//robot.dopredu(1.80,600);
		
		//robot.dopredubezgyra(-0.50, 200, 0.82f);
		
		//robot.dopredu(-0.80,600);
		
		//robot.lineFollowerPravy(100, 2.00, 200, 200);
		
		
		
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