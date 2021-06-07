package Robot;

import java.util.Map;

import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import pack.Main;
import pack.SpustacVyjazdov;

public class Robot {
	private final EV3MediumRegulatedMotor predny;
	private final EV3MediumRegulatedMotor zadny;
	public Farebnik lavyFarebnik;
	public Farebnik pravyFarebnik;
	private Chassis chassis;
	public Gyro gyroSensor;
	private Map<String, PIDController> pidy;
	
	
	private PIDController PIDdopreduLavy = new PIDController(3.2, 2, 0);//5.4;
	private PIDController PIDdopreduPravy = new PIDController(-3.2, -2, 0);//5.4;
	private PIDController PIDdozaduLavy = new PIDController(3.2, 2, 0);//5.4;
	private PIDController PIDdozaduPravy = new PIDController(-3.2, -2, 0);//5.4;
	private PIDController PIDdopreduPomer = new PIDController(0.019,-0.0095,0);//0.0165,-0.0095,0.02
	
	public void NastavPIDy(Map<String, PIDController> pidy) {
		this.pidy = pidy;
		if(pidy.containsKey("dopreduLavy")) PIDdopreduLavy = pidy.get("dopreduLavy");
		if(pidy.containsKey("dopreduPravy")) PIDdopreduPravy = pidy.get("dopreduPravy");
		if(pidy.containsKey("dozaduLavy")) PIDdozaduLavy = pidy.get("dozaduLavy");
		if(pidy.containsKey("dozaduPravy")) PIDdozaduPravy = pidy.get("dozaduPravy");
		if(pidy.containsKey("dopreduPomer")) PIDdopreduPomer = pidy.get("dopreduPomer");
	}
	
	public Robot() {
		predny = new EV3MediumRegulatedMotor(MotorPort.D);
		zadny = new EV3MediumRegulatedMotor(MotorPort.A);
		lavyFarebnik = new Farebnik(SensorPort.S1);
		pravyFarebnik = new Farebnik(SensorPort.S2);
		gyroSensor = new Gyro(SensorPort.S4);
		chassis = new Chassis(MotorPort.C,MotorPort.B);
		maxSpeed = chassis.getMaxSpeed();
	}
	
	public void OtocPredny(int stupne, boolean cakaj) {
		OtocPredny(stupne, (int)predny.getMaxSpeed(), cakaj);
	}
	
	public void OtocPredny(int stupne, int speed, boolean wait) {
		predny.setSpeed(speed);	
		predny.rotate(stupne, !wait);
	}
	
	public void OtocZadny(int stupne) {
		OtocZadny(stupne,true);
	}
	
	public void OtocZadny(int stupne,boolean cakaj) {
		nastavitZasekavanie();
		zadny.setSpeed(zadny.getMaxSpeed());
		zadny.rotate(stupne, !cakaj);
	}
	
	public void OtocPoZaseknutie() {
		//zadny.
	}
	
	public void nastavitZasekavanie() {
		predny.setStallThreshold(10000, 10000);
		zadny.setStallThreshold(10000, 10000);
	}
	
	public void Stop() {
		chassis.stopMovement();
	}
	
	public void Flt() {
		chassis.flt();
		predny.flt();
		zadny.flt();
	}
	
	final float sensitivity = 1.0f;
	final int zrychlenie = 1400;//500;
	final int default_spomalenie = 860;//450, 550
	final int zrychlenie_cuvanie = 700;//1200;
	final int spomalenie_cuvanie = 400;
	final float maxSpeed;
	
	public void dopredu(double vzd) throws InterruptedException {
		dopredu(vzd, maxSpeed);
	}
	
	public void dozadu(double vzd, float rychlost) throws InterruptedException {
		dopredu(-vzd, rychlost);
	}
	
	public void dozadu(double vzd) throws InterruptedException {
		dopredu(-vzd);
	}
	
	public void dozadu(double vzd, float rychlost, float targetUhol) throws InterruptedException {
		dopredu(-vzd,rychlost,targetUhol);
	}
	
	public void dopredu(double vzd, float rychlost) throws InterruptedException {
		if(vzd > 0)PIDdopredu2(vzd, rychlost, default_spomalenie);
		else PIDdopredu2(vzd, rychlost, spomalenie_cuvanie);
	}
	
	public void dopredu(double vzd, float rychlost, int spomalenie) throws InterruptedException {
		PIDdopredu2(vzd, rychlost, spomalenie);
	}

	public void dopredu(double vzd, float rychlost, float targetUhol) throws InterruptedException {
		gyroSensor.NastavTargetUhol((int)targetUhol);
		PIDdopredu2(vzd, rychlost, default_spomalenie);
		gyroSensor.NastavTargetUhol(0);
	}
	
	public void dopredubezgyra(double vzd, float rychlost) {
		dopredubezgyra(vzd,rychlost,1);
	}
	
	public void dopredubezgyra(double vzd, float rychlost,float zabacanie) {
		float rozdiel = rychlost*zabacanie-rychlost;
		chassis.initSynchronizedMovement(rychlost+rozdiel,rychlost-rozdiel, zrychlenie);
		chassis.beginForwardMovement(chassis.lengthToAngle(vzd));
		chassis.adjustSpeed(rychlost+rozdiel, rychlost-rozdiel);
		while(chassis.isMovingbezgyra()) {}
		Stop();
	}
	
	public void dopreduBezGyra2(double vzd, float rychlost, int zrychlenie, int spomalenie) throws InterruptedException {
		PIDController PIDnula = new PIDController(0,0,0);
		PIDControlledMovement pid = new PIDControlledMovement(chassis, gyroSensor, PIDnula, PIDnula, rychlost);		
		pid.execute(zrychlenie, chassis.lengthToAngle(vzd),spomalenie);
	}

	public void lineFollowerPravy(float speed, double vzd, int zrychlenie, int spomalenie) throws InterruptedException {
		//PIDController lavy = new PIDController(0.2, 0, 0.3);//0.2,0,0.1  -0.2/6,0,-0.1/6		200,10            pre rychlost 100 optimalne 0.75,0,0 a -0.125,0,0
		//PIDController pravy = new PIDController(-0.2/6, 0, -0.3/6);//140,0,-10
		//PIDController lavy = new PIDController(100, 0, 100.0);//                               0.2,0,0.1  -0.2/6,0,-0.1/6		200,10            pre rychlost 100 optimalne 0.75,0,0 a -0.125,0,0
		//PIDController pravy = new PIDController(-100/8, 0, -100.0/8);
		PIDController lavy = new PIDController(0.075*17, 0, 0.02*17);//d-0.0075                               0.2,0,0.1  -0.2/6,0,-0.1/6		200,10            pre rychlost 100 optimalne 0.75,0,0 a -0.125,0,0
		PIDController pravy = new PIDController(-0.075*9.5, 0, -0.02*9.5);
		PIDControlledMovement pid = new PIDControlledMovement(chassis, pravyFarebnik, pravy, lavy, speed);//17,9.5
		pid.execute(zrychlenie, chassis.lengthToAngle(vzd),spomalenie);
	}
	
	public void lineFollowerLavy(float speed, double vzd) throws InterruptedException {
		PIDController lavy;
		PIDController pravy;
		if(vzd < 0) {
			lavy = new PIDController(-1, 0, 0);//100 0 -10
			pravy = new PIDController(-5, 0, 0);//200 0 10
		}else {
			lavy = new PIDController(130, 0, -10);//100 0 -10
			pravy = new PIDController(220, 0, 10);//200 0 10
		}
		PIDControlledMovement pid = new PIDControlledMovement(chassis, lavyFarebnik, pravy, lavy, speed);
		pid.execute(zrychlenie, chassis.lengthToAngle(vzd));
		
	}
	
	public void PIDdopredu2(double vzd, float speed, int spomalenie) throws InterruptedException {
		gyroSensor.nastavMod(1);
		//PIDdopreduLavy = new PIDController(3.2,2,0);
		//PIDdopreduPravy = new PIDController(-3.2,-2,0);
		int zrychlenieTeraz = zrychlenie;
		PIDController lavypid,pravypid;
		if(vzd > 0) {
			lavypid = PIDdopreduLavy;
			pravypid = PIDdopreduPravy;
		}else {
			zrychlenieTeraz = zrychlenie_cuvanie;
			lavypid = PIDdozaduLavy;
			pravypid = PIDdozaduPravy;
		}
		PIDControlledMovement pid = new PIDControlledMovement(chassis, gyroSensor, pravypid, lavypid, speed);
		
		pid.execute(zrychlenieTeraz, chassis.lengthToAngle(vzd),spomalenie);
	}
	
	public void dopreduGyroUhol(double vzd, float rychlost, float zabacanie) throws InterruptedException {
		gyroSensor.nastavMod(1);
		PIDController lavypid = new PIDController(0.1,3,0);
		PIDController pravypid = new PIDController(-0.1,-3,0);
		PIDControlledMovement pid = new PIDControlledMovement(chassis, gyroSensor, pravypid, lavypid, rychlost);
		
		pid.execute(zrychlenie, chassis.lengthToAngle(vzd),default_spomalenie,zabacanie);
	}
	
	public void zarovnatNaUhol(float uhol) throws InterruptedException {
		gyroSensor.nastavMod(0);
		float uholTeraz = gyroSensor.getValue();
		if(uhol > uholTeraz) {
			otocitPoUhol(-40,40,uhol);
		}else {
			otocitPoUhol(40,-40,uhol);
		}
	}
	
	public void otocitPoUhol(float rychlost1, float rychlost2, float chcenyuhol) throws InterruptedException {
		gyroSensor.nastavMod(0);
		OtocitPoUhol volaco = new OtocitPoUhol(rychlost1,rychlost2,chcenyuhol,chassis,gyroSensor);
		volaco.execute(0,0);
		gyroSensor.nastavMod(1);
	}
	
	public void PIDpoCiaru(float rychlost, double vzd, Farebnik farebnik, boolean cierna) throws InterruptedException {
		int zrychlenieTeraz = zrychlenie;
		PIDController lavypid,pravypid;
		if(vzd > 0) {
			lavypid = PIDdopreduLavy;
			pravypid = PIDdopreduPravy;
		}else {
			zrychlenieTeraz = zrychlenie_cuvanie;
			lavypid = PIDdozaduLavy;
			pravypid = PIDdozaduPravy;
		}
		PIDPoCiaru pid = new PIDPoCiaru(chassis,rychlost,farebnik,pravypid,lavypid,cierna);
		pid.execute(zrychlenieTeraz, chassis.lengthToAngle(vzd),default_spomalenie);
	}
	
	public void otocitPoCiaru(float rychlost1, float rychlost2, Farebnik farebnik) throws InterruptedException {
		OtocitPoCiaru otocit = new OtocitPoCiaru(chassis, rychlost1, rychlost2, farebnik);
		otocit.execute(0,0);
	}
	
	public void dopreduZarovnatNaCiaru(int strana, int casKonca) throws InterruptedException {
		//this.PIDpoCiaru(300, vzd, this.lavyFarebnik);
		PIDController pidP = new PIDController(10,0,3);
		PIDController pidL = new PIDController(10,0,3);
		float stred = 0.45f;
		//float bocivost = 10*strana;//40*
		int limit = 2;
		chassis.initSynchronizedMovementBezSynchronizovania(10, 1000000);
		// chassis.beginForwardMovement();
		chassis.adjustSpeedCiara(70, 70);
		long cas = System.currentTimeMillis();
		while(Main.beziVyjazd()) {
			long bezi = System.currentTimeMillis() - cas;
			if (bezi > casKonca) {
				break;
			}
			
			float hodnotaP = this.pravyFarebnik.getValue();
			float hodnotaL = this.lavyFarebnik.getValue();
			float leftSpeed, rightSpeed;
			double vysledokPiduPravy = pidP.getOutput(stred-hodnotaP);
			double vysledokPiduLavy = pidL.getOutput(stred-hodnotaL);
			
			
			rightSpeed = (float)vysledokPiduPravy*strana;//bocivost*(stred-hodnotaP);
			leftSpeed = (float)vysledokPiduLavy*strana;//bocivost*(stred-hodnotaL);
			// chassis.adjustSpeedCiara(leftSpeed, rightSpeed);
			int pravy = Math.max(-limit, Math.min(limit, (int)rightSpeed));
			int lavy = Math.max(-limit, Math.min(limit, (int)leftSpeed));
			if(lavy == 0 && pravy == 0) break;
			chassis.otocObaMotory(
				lavy,
				pravy
			);
			System.out.println(""+bezi+", "+hodnotaL+", "+leftSpeed+"     "+hodnotaP+", "+rightSpeed);
		}
		chassis.stopMovement();
		if(!Main.beziVyjazd()) {
			throw new InterruptedException();
		}
	}
}
