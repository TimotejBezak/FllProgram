package Robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.Port;
import lejos.robotics.RegulatedMotor;

public class Chassis {
	public EV3LargeRegulatedMotor left;
	public EV3LargeRegulatedMotor right;
	private final double obvodkolesa = (2*Math.PI*4.4)/100;
	private final float vzdialenostKonstanta = 1.01f;
	
	public Chassis(Port left, Port right) {
		this.left = new EV3LargeRegulatedMotor(left);
		this.right = new EV3LargeRegulatedMotor(right);
		this.left.synchronizeWith(new RegulatedMotor[] {this.right});
	}
	
	public float getMaxSpeed() {
		return (left.getMaxSpeed()+right.getMaxSpeed())/2;
	}
	
	public int aktualnaRychlostPravy() {
		return right.getRotationSpeed();
	}
	
	public int aktualnaRychlostLavy() {
		return left.getRotationSpeed();
	}
	
	public void flt() {
		left.flt(true);
		right.flt(true);
	}
	
	public int lengthToAngle(double length) {
		return (int)(((length / obvodkolesa)*360)*vzdialenostKonstanta);
	}
	
	public boolean isMoving() {
		return left.isMoving() || right.isMoving();
	}
	
	public void initSynchronizedMovement(float speed,int acc) {
		initSynchronizedMovement(speed,speed,acc);
	}
	
	public void initSynchronizedMovement(float speed1, float speed2, int acc) {
		left.startSynchronization();
		left.setAcceleration(acc);
		right.setAcceleration(acc);
		left.endSynchronization();
		left.startSynchronization();
		left.setSpeed(speed1);
		right.setSpeed(speed2);
		left.endSynchronization();
	}
	
	public int otocenieMotorov() {
		return (left.getTachoCount()+right.getTachoCount())/2;
	}
	
	public void beginForwardMovement() {
		left.startSynchronization();
		left.forward();
		right.forward();
		left.endSynchronization();
	}
	
	public void beginForwardMovement(int angle) {
		angle *= -1;
		left.startSynchronization();
		left.rotate(angle, true);
		right.rotate(angle, true);
		left.startSynchronization();
	}
	
	public void beginBackwardMovement() {
		left.startSynchronization();
		left.backward();
		right.backward();
		left.endSynchronization();
	}
	
	public void beginBackwardMovement(int angle) {
		beginForwardMovement(-angle);
	}
	
	public void stopMovement() {
		left.startSynchronization();
		left.setSpeed(0);
		right.setSpeed(0);
		left.endSynchronization();
		
		left.startSynchronization();
		left.setAcceleration(100000);
		right.setAcceleration(100000);
		left.endSynchronization();
		
		left.startSynchronization();
		left.stop();
		right.stop();
		left.endSynchronization();
	}
	
	public void nastavitZrychlenie(int acc) {
		left.startSynchronization();
		left.setAcceleration(acc);
		right.setAcceleration(acc);
		left.endSynchronization();
	}
	
	public void nastavitVelkeZrychlenie() {
		left.startSynchronization();
		left.setAcceleration(100000);
		right.setAcceleration(100000);
		left.endSynchronization();
	}
	
	public void adjustSpeed(float leftSpeed, float rightSpeed) {
		left.startSynchronization();
		left.setSpeed(leftSpeed);
		right.setSpeed(rightSpeed);
		left.endSynchronization();
	}
}
