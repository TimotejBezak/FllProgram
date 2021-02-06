package pack;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;

public class Robot {
	private final EV3LargeRegulatedMotor right;
	private final EV3LargeRegulatedMotor left;
	public Robot() {
		right = new EV3LargeRegulatedMotor(MotorPort.B);
		left = new EV3LargeRegulatedMotor(MotorPort.C);
		left.synchronizeWith(new RegulatedMotor[] {right});
		
	}
	
	public void Dopredu(int ms) {
		left.startSynchronization();
		left.forward();
		right.forward();
		left.endSynchronization();
		Delay.msDelay(ms);
		left.startSynchronization();
		left.stop();
		right.stop();
		left.endSynchronization();
	}
	
	public void zrychlovanie(double zrychlenie, int vzdialenost) {
		int cas = (int) Math.sqrt((2*vzdialenost)/zrychlenie);
		
		double obvodkolesa = (2*Math.PI*4.4)/100;
		
		int zrychlenieStupne = (int) ((zrychlenie/obvodkolesa)*360);
		System.out.println(zrychlenieStupne+"     "+cas);
		left.startSynchronization();
		left.setAcceleration(zrychlenieStupne);
		right.setAcceleration(zrychlenieStupne);
		left.endSynchronization();
		
		left.startSynchronization();
		left.forward();
		right.forward();
		//right.rotate(angle);
		//left.rotate(angle);
		left.endSynchronization();
		
		Delay.msDelay(cas*1000);
		
		left.startSynchronization();
		left.stop();
		right.stop();
		left.endSynchronization();
	}

	public void Stop() {
		// TODO Auto-generated method stub
		right.stop();
		left.stop();
	}
	
	public void Destroy(){
		left.close();
		right.close();
	}
}
