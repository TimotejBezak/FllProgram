package pack;

import lejos.utility.Delay;

public class PrvyVyjazd extends Vyjazd {

	public PrvyVyjazd() {
		super("Prvy Vyjazd");
	}
	
	protected void Spusti() {
		robot.zrychlovanie(0.1,1);
//      robot.zrychlovanie(-100,5000);
//		robot.Dopredu(5000);
	}

}
