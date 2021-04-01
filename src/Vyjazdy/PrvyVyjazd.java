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
				put("dopreduLavy", new PIDController((0.68/400)*PD,(11.0/400)*nieco,(50.0/400)*PD));//1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.68/400)*PD,(-11.0/400)*nieco,(-50.0/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}
		});
	}
	
	protected void Spusti() throws InterruptedException {
		/*robot.dopredu(0.685, 800);//678
		robot.otocitPoUhol(200, 650, 73);//78
		robot.dopredu(0.15, 800);
		// Basketbalovy kos
		robot.OtocPredny((int)-5.5*360, true);
		robot.OtocPredny(150, false);
		robot.dozadu(0.135, 800);
		robot.OtocPredny(-90, false);
		robot.otocitPoUhol(300, 0, 39-5);
		// pustanie kociek
		robot.dopredu(0.075);
		robot.dozadu(0.36);*/
		
		//robot.dopredu(2.10,400);
		
		//robot.lineFollowerLavy(30, -2);
		robot.lineFollowerPravy(200, 0.50, 60, 40);
		
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
