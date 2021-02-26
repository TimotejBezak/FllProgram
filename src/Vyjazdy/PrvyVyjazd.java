package Vyjazdy;

import pack.Vyjazd;

public class PrvyVyjazd extends Vyjazd {

	public PrvyVyjazd() {
		super("Prvy Vyjazd");
	}
	
	protected void Spusti() {
		robot.gyro.reset();
		robot.dopredu(0.678, 800);
		System.out.println("bezi prvy vyjazd");
		robot.otocitPoUhol(250, 750, 73);//78
		robot.dopredu(0.15, 800);
		// Basketbalovy kos
		robot.OtocPredny((int)-5.5*360, true);
		robot.OtocPredny(90, false);
		robot.dozadu(0.135, 800);
		robot.OtocPredny(-90, false);
		robot.otocitPoUhol(300, 0, 39);
		// pustanie kociek
		robot.dopredu(0.075);
		robot.dozadu(0.36);
		
		robot.otocitPoUhol(400, 200, -20);
		robot.dopredu(0.32);
		robot.OtocZadny(100);
		robot.OtocZadny(-100);
		
		robot.otocitPoUhol(-500, -200, 25);
		robot.otocitPoUhol(-100, -500, -20);
		robot.dopredubezgyra(-0.25, 200);
		robot.dopredubezgyra(-0.60, 800);
		//robot.otocitPoUhol(-500, -800, -90);
		
		//strvrty vyjazd = 47 cm
	}

}
