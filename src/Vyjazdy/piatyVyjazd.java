package Vyjazdy;

import pack.Vyjazd;

public class piatyVyjazd extends Vyjazd {

	public piatyVyjazd() {
		super("Piaty Vyjazd");
	}
	
	@Override
	protected void Spusti() {
		// TODO Auto-generated method stub
		robot.gyro.reset();
		robot.PIDdopredu(0.68,800);
		robot.dopredubezgyra(0.195-0.0125, 30);
		//robot.dopredu(-0.02485);
		robot.otocitPoUhol(-250, 100, 90);
		robot.dopredu(0.50);//47
		robot.OtocZadny(-100);
		robot.dozadu(0.17);//12
		robot.OtocZadny(200);
	}

}
