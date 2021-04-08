package Vyjazdy;

import pack.Vyjazd;

public class piatyVyjazd extends Vyjazd {

	public piatyVyjazd() {
		super("Piaty Vyjazd");
	}
	
	@Override
	protected void Spusti() throws InterruptedException {
		// TODO Auto-generated method stub
		robot.dopredu(0.68,500);
		robot.dopredubezgyra(0.1825, 30);
		robot.dopredu(-0.005,600);
		robot.otocitPoUhol(-250, 100, 90);
		robot.dopredu(0.50,600);//47
		robot.OtocZadny(100);
		robot.dozadu(0.17);//12
		robot.OtocZadny(-200);
	}

}
