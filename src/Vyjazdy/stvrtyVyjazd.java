package Vyjazdy;

import pack.Vyjazd;

public class stvrtyVyjazd extends Vyjazd {

	public stvrtyVyjazd() {
		super("Stvrty Vyjazd");
	}
	
	@Override
	protected void Spusti() {
		// TODO Auto-generated method stub
		//robot.dopredu(0.49);
		//robot.dopredu(-0.29);
		
		robot.PIDdopredu(1, 800);
	}

}
