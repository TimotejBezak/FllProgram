package Vyjazdy;

import pack.Vyjazd;

public class tretiVyjazd extends Vyjazd {

	public tretiVyjazd() {
		super("Treti Vyjazd");
	}
	
	@Override
	protected void Spusti() {
		
		//while(true) {
		//	System.out.println("farba je "+robot.getLavyFarba());
		//}
		
		//robot.linefollower(10.00, 120);

		robot.PIDdopreduNove(0.5, 800);
		robot.otocitPoUholNove(400, 800, 90);
		robot.PIDdopreduNove(0.5, 800);
		robot.otocitPoUholNove(400, 800, 180);
		robot.PIDdopreduNove(0.5, 800);
		robot.otocitPoUholNove(400, 800, 270);
		robot.PIDdopreduNove(0.5, 800);
		robot.otocitPoUholNove(400, 800, 0);
	}
}