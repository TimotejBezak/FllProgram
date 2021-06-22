package Vyjazdy;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import Robot.PIDController;
import pack.Vyjazd;

public class tretiVyjazd extends Vyjazd {

	public tretiVyjazd() {
		super("Treti Vyjazd", new HashMap<String, PIDController>(){
			{
				float nieco = 1.0f;//
				float PD = 1.2f;//0.044f;0.023 a i=1.5      i=5, PD = 0.39            PD 10 bez i
				float bocivost = 1.21f;
				put("dopreduLavy", new PIDController((0.2/400)*PD,(200/400)*nieco,(250/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dopreduPravy", new PIDController((-0.2/400)*PD,(-200/400)*nieco,(-250/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
				put("dozaduLavy", new PIDController((0.3/400)*PD,(200/400)*nieco,(150/400)*PD));// (0.2/400)*PD,(100/400)*nieco,(250/400)*PD)                6.8  110  500               //1,0.6,2.5         // 3.2,2 //1.2,0.7,2.0     //0.75,0.6,0.4 - toto
				put("dozaduPravy", new PIDController((-0.3*bocivost/400)*PD,(-200*bocivost/400)*nieco,(-150*bocivost/400)*PD));//-1,-0.6,-2.5// d samostatne 1.4 //0.1,0,0.65
			}
		});
	}
	
	@Override
	protected void Spusti() throws InterruptedException {		
		robot.otocitPoUhol(100, 0, -3);//robot.dopredu(1.44,700);//idenie popri stene
		
		robot.dopredubezgyra(1.4305, 700);//robot.dopreduBezGyra2(1.430, 700, 1000, 870);//robot.dopredubezgyra(1.438, 700);//1.443
		float uhol1 = 87;//87
		robot.otocitPoUhol(100, 500, uhol1);//100,500
		robot.dopredu(0.20,600,uhol1);//robot.dopredu(0.3075 + 0.10,600,uhol1);//0.2825
		//robot.dopredu(0.2825,600,uhol1);//robot.dopredu(0.3075 + 0.10,600,uhol1);//0.2825
		robot.PIDpoCiaru(60, 0.08, robot.lavyFarebnik, false);
		robot.dopreduZarovnatNaCiaru(1,2300);
//		TimeUnit.SECONDS.sleep(1);
//		robot.otocLavy(-30, 100);//robot.otocitPoUhol(100, -100, 85);
//		robot.otocLavy(30, 100);//robot.otocitPoUhol(-100, 0, uhol1);
//		
		
//		robot.PIDpoCiaru(120, -0.15, robot.lavyFarebnik, false);
//		robot.dozadu(0.008, 200);
//		robot.dopreduZarovnatNaCiaru(-1,2300);
//		
//		robot.dozadu(0.025,100,uhol1);
//		
		robot.OtocZadny(4*360+240, false);//davanie radlice trosku dole
		float uhol2 = 178;
		robot.otocitPoUhol(-600, -79, uhol2);//-1100,-120        -84   strasidelna otocka
		robot.dopredu(0.25,400,uhol2);//prichadzanie k pneumatikam 0.27
		
		
		
		robot.OtocZadny(9*360);//naklapanie radlice na pneumatiky
		robot.dozadu(0.23, 400,uhol2);//preklapanie pneumatik 22
		
		//robot.zarovnatNaUhol(uhol2);
		robot.OtocZadny(-13*360+240, false);										//robot.otocitPoUhol(200, 0, 180-6);
		robot.dopredu(0.16, 600,uhol2);//0.16   dotlacenie pneunmatik do kruhu
		
		robot.OtocPredny(360,true);//zhadzovanie panaka
		robot.OtocPredny(-360, true);
		
		robot.dozadu(0.09+0.06, 700, uhol2);//cuvanie aby sa mohol otocit bez toho aby zavadil o pneumatiky
		
		
		robot.otocitPoUhol(200, -400, 90);
		robot.dozadu(0.55,600);
		robot.dopredu(0.26,600);
		robot.otocitPoUhol(-200, 100, uhol2-5);
		robot.OtocZadny(15*360,false);//tocenie kolesom -10
		robot.dozadu(0.35,600);
		
//		float uhol3 = -60+180;
//		robot.otocitPoUhol(200, -800, uhol3);//100,-400
//		robot.dozadu(0.205,450,uhol3);//0.195   0.215
//		//TimeUnit.SECONDS.sleep(10);
//		robot.otocitPoUhol(-300, 180, uhol2-5);//-300,180
//		//TimeUnit.SECONDS.sleep(10);
//		robot.OtocZadny(-10*360,false);//tocenie kolesom
//		//TimeUnit.SECONDS.sleep(10);
//		robot.dozadu(0.32,600,uhol2-5);//cuvanie po stenu
		
		
		
		
		
		robot.OtocPredny(110,true);//chytenie modreho kolesa
		
		//TimeUnit.SECONDS.sleep(10);
		robot.dopredu(0.13,300);//0.12  posuvanie modreho kolesa
		//TimeUnit.SECONDS.sleep(10);
		robot.OtocZadny(7*360, false);//posunutie radlice dole
		//TimeUnit.SECONDS.sleep(10);
		robot.otocitPoUhol(100, 0, 180-10);// posunutie malej modrej pneumatiky do kruhu
		//TimeUnit.SECONDS.sleep(10);
		
		robot.OtocPredny(-110, true);
		
		float uhol4 = 180+40-10;
		//TimeUnit.SECONDS.sleep(10);
		robot.otocitPoUhol(200, 800, uhol4);													// adam > timo
		//TimeUnit.SECONDS.sleep(10);
		robot.dopredu(0.10,270,uhol4);// idenie k jednotke zdravia
		
		//TimeUnit.SECONDS.sleep(10);
		robot.otocitPoUhol(-80, -500, uhol2);// zobratie jednotky zdravia   -80,-500
		
		robot.otocitPoUhol(0, 150, uhol2+5);//vratenie do basu
		robot.dopredubezgyra(1.7, 700, 0.86f);//1.5
		
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