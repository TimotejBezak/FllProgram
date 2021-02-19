package pack;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Robot {
	private final EV3LargeRegulatedMotor right;
	private final EV3LargeRegulatedMotor left;
	private EV3GyroSensor gyro;
	public final double obvodkolesa = (2*Math.PI*4.4)/100;
	public Robot() {
		right = new EV3LargeRegulatedMotor(MotorPort.B);
		left = new EV3LargeRegulatedMotor(MotorPort.C);
		left.synchronizeWith(new RegulatedMotor[] {right});
		gyro = new EV3GyroSensor(SensorPort.S4);
		gyro.reset();
		gyro.setCurrentMode(2);
	}
	
	public int getUhol(){
		float[] sample = new float[gyro.sampleSize()];
		gyro.fetchSample(sample, 0);
		return (int)sample[0];
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
	
	final float sensitivity = 1.0f;
	final int zrychlenie = 300;
	final int maxSpeed = 800;
	final float vzdialenostKonstanta = 1.01f;
	public void dopredu(double vzd, float rychlost) {
		vzd *= -1*vzdialenostKonstanta;
		Stopwatch sw = new Stopwatch();
		sw.reset();
		int otocenie = (int)((vzd / obvodkolesa)*360);
		float[] sample = new float[gyro.sampleSize()];
		gyro.getAngleAndRateMode().fetchSample(sample, 0);
		float uhol = sample[0];

		left.startSynchronization();
		left.setAcceleration(zrychlenie);
		right.setAcceleration(zrychlenie);
		left.setSpeed(rychlost);
		right.setSpeed(rychlost);
		right.rotate(otocenie, true);
		left.rotate(otocenie, true);
		left.endSynchronization();
		
		float rightSpeed = right.getSpeed();
		float leftSpeed = left.getSpeed();
		SampleProvider provider = gyro.getAngleAndRateMode();
		
		while(left.isMoving()) {
			provider.fetchSample(sample, 0);
			int uholTeraz = (int)sample[0];
			
			if (uholTeraz != uhol) {
				float diff = sensitivity * (uhol-uholTeraz);
				rightSpeed -= diff;
				leftSpeed += diff;
				
				System.out.println("lavarychlost: "+leftSpeed+"   pravarychlost: "+rightSpeed);
				
				float presah = 0.0f;
				if (Math.abs(rightSpeed) > maxSpeed) {
					presah = Math.abs(rightSpeed) - maxSpeed;
				}
				if (Math.abs(leftSpeed) > maxSpeed) {
					presah = Math.abs(leftSpeed) - maxSpeed;
				}
				if (presah > 0.0f) {
					float sign = (rightSpeed >= 0) ? 1 : -1;
					rightSpeed -= sign * presah;
					leftSpeed -= sign * presah;
				}
				
				left.startSynchronization();
				left.setSpeed(leftSpeed);
				right.setSpeed(rightSpeed);
				left.endSynchronization();
			}
		}
//		left.startSynchronization();
//		right.setSpeed(rychlost);
//		left.setSpeed(rychlost);
//		left.endSynchronization();
//		
//		left.startSynchronization();
//		left.setAcceleration(-zrychlenie);
//		right.setAcceleration(-zrychlenie);
//		left.endSynchronization();
//		
//		left.startSynchronization();
//		right.forward();
//		left.forward();
//		left.endSynchronization();
		
//		while(left.getRotationSpeed()>10) {
//			provider.fetchSample(sample, 0);
//			int uholTeraz = (int)sample[0];
//			if (uholTeraz != uhol) {
//				float diff = sensitivity * (uhol-uholTeraz);
//				rightSpeed -= diff;
//				leftSpeed += diff;
//				
//				float presah = 0.0f;
//				if (Math.abs(rightSpeed) > maxSpeed) {
//					presah = Math.abs(rightSpeed) - maxSpeed;
//				}
//				if (Math.abs(leftSpeed) > maxSpeed) {
//					presah = Math.abs(leftSpeed) - maxSpeed;
//				}
//				if (presah > 0.0f) {
//					float sign = (rightSpeed >= 0) ? 1 : -1;
//					rightSpeed -= sign * presah;
//					leftSpeed -= sign * presah;
//				}
//				
//				left.startSynchronization();
//				left.setSpeed(leftSpeed);
//				right.setSpeed(rightSpeed);
//				left.endSynchronization();
//			}
//		}
		stopMovement();
	}

	private void stopMovement() {
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
	
	float p = 4.0f;
	float minimalnaRychlost = 0.2f;
	float rychlostpodlauhlu(float x) {
		if(x == 0) return 0.2f;
		if(x>1 || x<0) return 0;
		//return 0.05f-3.8f*x*(x-1);
		float vysledok;
		if(x <= 1/p) vysledok = p*x;
		else vysledok = -(p/(p-1))*x+1+1/(p-1);
		if(vysledok <= minimalnaRychlost && x < 0.5) return minimalnaRychlost;
		else return vysledok;
	}
	
	public void otocitPoUhol(float rychlost1, float rychlost2, float chcenyuhol) {
		Stopwatch sw = new Stopwatch();
		sw.reset();
		gyro.reset();
		gyro.getAngleMode();
		float[] sample = new float[gyro.sampleSize()];
		gyro.getAngleAndRateMode().fetchSample(sample, 0);
		float uhol = sample[0];
		float rozdieluhlov = Math.abs(chcenyuhol-uhol);
		float rozdieluhlov2 = chcenyuhol-uhol;
		SampleProvider provider = gyro.getAngleAndRateMode();
		
		left.startSynchronization();
		left.setSpeed(10);
		right.setSpeed(10);
		left.endSynchronization();
		
		left.startSynchronization();
		if(rychlost1 > 0)left.backward();
		else left.forward();
		if(rychlost2 > 0)right.backward();
		else right.forward();
		left.endSynchronization();
		
		rychlost1 = Math.abs(rychlost1);
		rychlost2 = Math.abs(rychlost2);
				
		float rychlost;
		int uholTeraz;
		while(true) {
			provider.fetchSample(sample, 0);
			uholTeraz = (int)sample[0];
			if(rozdieluhlov2 > 0) {
				if(uholTeraz >= chcenyuhol) break;
			} else {
				if(uholTeraz <= chcenyuhol) break;
			}
			
			float percenta = Math.abs(uholTeraz-uhol)/rozdieluhlov;
			rychlost = rychlostpodlauhlu(percenta);
			left.startSynchronization();
			left.setSpeed(rychlost*rychlost1);
			right.setSpeed(rychlost*rychlost2);
			left.endSynchronization();
		}
		
		stopMovement();
	}
	
	
	public void dopredubezgyra(double vzd, float rychlost) {
		Stopwatch sw = new Stopwatch();
		sw.reset();
		int otocenie = (int)((vzd / obvodkolesa)*360);
		left.setAcceleration(zrychlenie);
		System.out.println("Cas 1: " + sw.elapsed());
		right.setAcceleration(zrychlenie);
		System.out.println("Cas 2: " + sw.elapsed());
		left.setSpeed(rychlost);
		System.out.println("Cas 3: " + sw.elapsed());
		right.setSpeed(rychlost);
		System.out.println("Cas 4: " + sw.elapsed());
		left.startSynchronization();
		left.rotate(otocenie, true);
		right.rotate(otocenie, true);
		left.endSynchronization();
		System.out.println("Cas po sync: " + sw.elapsed());
		float rightSpeed = right.getSpeed();
		float leftSpeed = left.getSpeed();
		System.out.println("Cas: "+ sw.elapsed() +" left: " + leftSpeed + " right: " + rightSpeed);
		
		while(left.isMoving()) {
			System.out.println("Cas: "+ sw.elapsed());
		}
		left.startSynchronization();
		left.stop(true);
		right.stop(true);
		left.endSynchronization();
	}
}
