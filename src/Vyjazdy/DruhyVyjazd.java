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
				put("dozaduLavy", new PIDController((0.05/400)*PD,(150/400)*nieco,(70/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dozaduPravy", new PIDController((-0.05/400)*PD,(-150/400)*nieco,(-70/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}//d:375
		});
	}
	

	@Override
	protected void Spusti() throws InterruptedException {
		//robot.PIDpoCiaru(300, 0.2, robot.lavyFarebnik);
		//robot.otocitPoCiaru(100, -100, robot.lavyFarebnik);
		
		robot.PIDpoCiaru(230, -0.10, robot.lavyFarebnik, false);
		robot.dopreduZarovnatNaCiaru(-1,2300);
		
		//robot.dozadu(0.5, 600);
		
		
//		int otocenieRadlice = 1000;
//		robot.OtocZadny(otocenieRadlice, false);
//		robot.otocitPoUhol(600, 214, -35);//lavarychlost,pravarychlost,uhol-zaporny vpravo // 700, 250
//		robot.dopredu(0.55, 700); // 0.490
//		robot.otocitPoUhol(600, 300, -87); // 700 400
//		robot.dopredu(0.59); // .58
//		robot.otocitPoUhol(200, 700, -40);//400,700,-54
//		robot.OtocZadny(-otocenieRadlice);
//		robot.OtocZadny(otocenieRadlice, false);
//		robot.otocitPoUhol(-500, 120, 25); // - 0 (druhe) 21
//		robot.dopredu(0.24,600); 																				// som lepsi ako timo
//		robot.OtocZadny(-otocenieRadlice);
//		robot.dopredu(-0.28, 600);
//		robot.otocitPoUhol(200, -200, -95);
//		robot.dopredu(0.10, 600);
//		robot.dozadu(1, 600);
		
		
		
		
		
		
		
		//Delay.msDelay(5000);
		//robot.OtocPredny(720, true);
		
		//robot.lineFollowerPravy(250, 2.00, 300, 300);
		
		
		//for(int i=0;i<10;i++) {
		//	robot.otocitPoUhol(400, 800, 90);
		//	robot.otocitPoUhol(-400, -800, 0);
		//}
		
		
		//robot.dopredu(1.94, 600);
	}

}
