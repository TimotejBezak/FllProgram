package Robot;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
//import lejos.hardware.sensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Robot {
	private EV3LargeRegulatedMotor right;
	private EV3LargeRegulatedMotor left;
	private final EV3MediumRegulatedMotor predny;
	private final EV3MediumRegulatedMotor zadny;
	public EV3GyroSensor gyro;
	public EV3ColorSensor lavyFarebnik;
	public EV3ColorSensor pravyFarebnik;
	private Chassis chassis;
	private Gyro gyroSensor;
	public final double obvodkolesa = (2*Math.PI*4.4)/100;
	public Robot() {
//		right = new EV3LargeRegulatedMotor(MotorPort.B);
//		left = new EV3LargeRegulatedMotor(MotorPort.C);
		predny = new EV3MediumRegulatedMotor(MotorPort.D);
		zadny = new EV3MediumRegulatedMotor(MotorPort.A);
//		left.synchronizeWith(new RegulatedMotor[] {right});
		//gyro = new EV3GyroSensor(SensorPort.S4);
		lavyFarebnik = new EV3ColorSensor(SensorPort.S1);
		pravyFarebnik = new EV3ColorSensor(SensorPort.S2);
		lavyFarebnik.setCurrentMode(1);
		//gyro.reset();
		//gyro.setCurrentMode(2);
		gyroSensor = new Gyro(SensorPort.S4);
		chassis = new Chassis(MotorPort.C,MotorPort.B);
	}
	
	public void OtocPredny(int stupne, boolean cakaj) {
		OtocPredny(stupne, (int)predny.getMaxSpeed(), cakaj);
	}
	
	public void OtocPredny(int stupne, int speed, boolean wait) {
		predny.setSpeed(speed);
		
		predny.rotate(stupne, !wait);
	}
	
	public void OtocZadny(int stupne) {
		zadny.rotate(stupne);
	}
	
	public int getUhol(){
		float[] sample = new float[gyro.sampleSize()];
		gyro.fetchSample(sample, 0);
		return (int)sample[0];
	}
	
	public double getLavyFarba() {
		float[] sample = new float[lavyFarebnik.sampleSize()];
		lavyFarebnik.fetchSample(sample, 0);
		return sample[0];
	}

	public void Stop() {
		stopMovement();
	}
	
	public void Destroy(){
		left.close();
		right.close();
	}
	
	int uholPredResetom = 0;
	public void resetnigyro() {
		uholPredResetom = getUhol();
		gyro.reset();
	}
	
	final float sensitivity = 1.0f;
	final int zrychlenie = 300;
	final int maxSpeed = 800;
	final float vzdialenostKonstanta = 1.01f;
	
	public void dopredu(double vzd) {
		dopredu(vzd, left.getMaxSpeed());
	}
	
	public void dozadu(double vzd, float rychlost) {
		dopredu(-vzd, rychlost);
	}
	
	public void dozadu(double vzd) {
		dopredu(-vzd);
	}
	
	public void dopredu(double vzd, float rychlost) {
		vzd *= -1*vzdialenostKonstanta;
		Stopwatch sw = new Stopwatch();
		sw.reset();
		int otocenie = (int)((vzd / obvodkolesa)*360);
		
		float uhol = getUhol() + uholPredResetom;

		left.startSynchronization();
		left.setAcceleration(zrychlenie);
		right.setAcceleration(zrychlenie);
		left.endSynchronization();
		
		left.startSynchronization();
		left.setSpeed(rychlost);
		right.setSpeed(rychlost);
		left.endSynchronization();
		
		left.startSynchronization();
		right.rotate(otocenie, true);
		left.rotate(otocenie, true);
		left.endSynchronization();
		
		float rightSpeed = right.getSpeed();
		float leftSpeed = left.getSpeed();
		SampleProvider provider = gyro.getAngleAndRateMode();
		
		System.out.println("Uhol " + getUhol());
		while(left.isMoving()) {
			int uholTeraz = getUhol()  + uholPredResetom;
			System.out.println("uhol je "+uhol);
			
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
		//resetnigyro();
	}
	
	double biela = 0.9;
	double cierna = 0.08;
	double chcenaFarba = 0.50;
	public void linefollower(double vzd, float rychlost) {
		vzd *= -1*vzdialenostKonstanta;
		int otocenie = (int)((vzd / obvodkolesa)*360);
		
		left.startSynchronization();
		left.setAcceleration(zrychlenie);
		right.setAcceleration(zrychlenie);
		left.endSynchronization();
		
		left.startSynchronization();
		left.setSpeed(rychlost);
		right.setSpeed(rychlost);
		left.endSynchronization();
		
		left.startSynchronization();
		right.rotate(otocenie, true);
		left.rotate(otocenie, true);
		left.endSynchronization();
		
		PIDController lavyPID = new PIDController(300,0,0);
		PIDController pravyPID = new PIDController(700,0,0);
		
		double lavaRychlost;
		double pravaRychlost;
		
		double farba;
		while(left.isMoving()) {
			farba = getLavyFarba() - chcenaFarba;
			lavaRychlost = lavyPID.getOutput(farba);
			pravaRychlost = pravyPID.getOutput(farba);
			
			System.out.println("lava: "+lavaRychlost+"   prava: "+pravaRychlost);
			
			
			left.startSynchronization();
			left.setSpeed((int)(rychlost-lavaRychlost));
			right.setSpeed((int)(rychlost-pravaRychlost));
			left.endSynchronization();
			
		}
		
		stopMovement();
	}
	
	public void PIDdopredu(double vzd, float rychlost) {
		vzd *= -1*vzdialenostKonstanta;
		Stopwatch sw = new Stopwatch();
		sw.reset();
		int otocenie = (int)((vzd / obvodkolesa)*360);
		
		float uhol = gyroSensor.getValue();//getUhol() + uholPredResetom;

		left.startSynchronization();
		left.setAcceleration(zrychlenie);
		right.setAcceleration(zrychlenie);
		left.endSynchronization();
		
		left.startSynchronization();
		left.setSpeed(rychlost);
		right.setSpeed(rychlost);
		left.endSynchronization();
		
		left.startSynchronization();
		right.rotate(otocenie, true);
		left.rotate(otocenie, true);
		left.endSynchronization();
		
		double rightSpeed = right.getSpeed();
		double leftSpeed = left.getSpeed();
		SampleProvider provider = gyro.getAngleAndRateMode();
		
		double pomerrychlosti;
		
		PIDController PIDrychlosti = new PIDController(0.0165,-0.0095,0.02);
		//PIDController lavyPID = new PIDController(50,100,40);
		//PIDController pravyPID = new PIDController(50,100,40);
		
		System.out.println("Uhol " + getUhol());
		while(left.isMoving()) {
			int uholTeraz = (int)gyroSensor.getValue();//getUhol()  + uholPredResetom;
			System.out.println("uhol je "+uholTeraz);
			
			//leftSpeed = lavyPID.getOutput((double)uholTeraz);
			//rightSpeed = pravyPID.getOutput((double)uholTeraz);
			pomerrychlosti = PIDrychlosti.getOutput((double)uholTeraz)+1;
			
			System.out.println("output je "+pomerrychlosti);
							
			if(pomerrychlosti < 1) {
				leftSpeed = 800;
				rightSpeed = 800 * pomerrychlosti;
			}else {
				rightSpeed = 800;
				leftSpeed = 800 / pomerrychlosti;
			}
			
			left.startSynchronization();
			//left.setSpeed(800-(float)leftSpeed);
			//right.setSpeed(800-(float)rightSpeed);
			left.setSpeed((int)rightSpeed);
			right.setSpeed((int)leftSpeed);
			left.endSynchronization();

		}
		
		
		stopMovement();
		resetnigyro();
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
		//gyro.reset();
		gyro.getAngleMode();
		float[] sample = new float[gyro.sampleSize()];
		gyro.getAngleAndRateMode().fetchSample(sample, 0);
		float uhol = sample[0] + uholPredResetom;
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
			uholTeraz = (int)sample[0] + uholPredResetom;
			System.out.println("uholTeraz: "+uholTeraz);
			if(rozdieluhlov2 > 0) {
				if(uholTeraz >= chcenyuhol) break;
			} else {
				if(uholTeraz <= chcenyuhol) break;
			}
			
			float percenta = Math.abs(uholTeraz-uhol)/rozdieluhlov;
			if(percenta == 1.0) break;
			rychlost = rychlostpodlauhlu(percenta);
			System.out.println(rychlost+"   "+percenta+"   "+uholTeraz);
			left.startSynchronization();
			left.setSpeed(rychlost*rychlost1);
			right.setSpeed(rychlost*rychlost2);
			left.endSynchronization();
		}
		
		stopMovement();
		
		
		
		//resetnigyro();
	}
	
	
	public void dopredubezgyra(double vzd, float rychlost) {
		vzd*=-1;
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
	
	public void PIDdopreduNove(double vzd, float rychlost) {
		int otocenie = (int)((vzd / obvodkolesa)*360);
		PIDController pid = new PIDController(0.0165,-0.0095,0.02);
		PIDControlledMovementRatio PIDdopredu = new PIDControlledMovementRatio(chassis,rychlost,gyroSensor,pid);
		PIDdopredu.execute(zrychlenie, otocenie);
	}
	
	
	public void otocitPoUholNove(float rychlost1, float rychlost2, float chcenyuhol){
		OtocitPoUhol volaco = new OtocitPoUhol(rychlost1,rychlost2,chcenyuhol,chassis,gyroSensor);
		volaco.execute(0,0);
	}
	
	
	
	
}
