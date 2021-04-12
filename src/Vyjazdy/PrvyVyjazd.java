package Vyjazdy;

import java.util.HashMap;

import Robot.PIDController;
import pack.Vyjazd;

public class PrvyVyjazd extends Vyjazd {

	public PrvyVyjazd() {
		super("Prvy Vyjazd", new HashMap<String, PIDController>(){
			{
				float nieco = 1.0f;//
				float PD = 1.2f;//0.044f;0.023 a i=1.5      i=5, PD = 0.39            PD 10 bez i
				put("dopreduLavy", new PIDController((0.2/400)*PD,(200/400)*nieco,(250/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.2/400)*PD,(-200/400)*nieco,(-250/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
				put("dozaduLavy", new PIDController((0.1/400)*PD,(100/400)*nieco,(125/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dozaduPravy", new PIDController((-0.1/400)*PD,(-100/400)*nieco,(-125/400)*PD));
			}
		});
	}
	
	protected void Spusti() throws InterruptedException {
		robot.dopredu(0.66, 600);//678
		robot.otocitPoUhol(200/3*2, 650/3*2, 73);//78
		robot.dopredu(0.128, 500);
		// Basketbalovy kos
		robot.OtocPredny((int)-6.5*360, true);
		robot.OtocPredny(360, false);
		robot.dozadu(0.13, 600);//0.135
		robot.OtocPredny(-200, false);
		robot.otocitPoUhol(200, 0, 33);
		robot.dopredu(0.08,100);//spustanie modrej kocky
		
		robot.dozadu(0.35,600);
		robot.otocitPoUhol(400, 200, -27);//-28    -24
		robot.dopredu(0.2425,600);
		
		robot.OtocZadny(200);//vyklapanie kociek
		robot.OtocZadny(-220);//odklapanie kociek
		
		robot.otocitPoUhol(-500, -30, 32);//presun k panakom
		robot.otocitPoUhol(-40, -500, -23);//presun k panakom
		
		robot.dopredubezgyra(-0.40, 100,0.96f);//zhadzovanie panakov    -0.25
		robot.dopredubezgyra(-0.56, 600);//navrat do basu
	 
		assert robot != null;
		
		
		/*robot.OtocPredny(-90, false);
		robot.otocitPoUhol(300, 0, 39-5);
		// pustanie kociek
		robot.dopredu(0.075);
		robot.dozadu(0.36);*/
		
		//robot.dopredu(2.10,400);
		
		//robot.lineFollowerLavy(30, -2);
		
		/*robot.otocitPoUhol(400, 200, -20);
		robot.dopredu(0.32);
		robot.OtocZadny(100);
		robot.OtocZadny(-100);
		
		robot.otocitPoUhol(-500, -200, 25);
		robot.otocitPoUhol(-100, -500, -20);
		robot.dopredubezgyra(-0.25, 200);
		robot.dopredubezgyra(-0.60, 800);*/
		//robot.otocitPoUhol(-500, -800, -90);
	}

}
