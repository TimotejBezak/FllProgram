package Robot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;

public class Gyro implements Sensor {
	private EV3GyroSensor gyro;
	private float uholPredResetom;
	private int mod;
	
	public void reset() {
		int predoslyMod = this.mod;
		nastavMod(0);
		uholPredResetom = getValue();
		nastavMod(predoslyMod);
//		System.out.println("Resetujem gyro, uhol je: "+ uholPredResetom);
		gyro.reset();
	}
	
	public void hardReset() {
		uholPredResetom = 0;
		gyro.reset();
	}
	
	public Gyro(Port port) {
		gyro = new EV3GyroSensor(port);
		gyro.reset();
		gyro.setCurrentMode(2);
	}
	
	public void nastavMod(int mod) {
		this.mod = mod;
		System.out.println("Nastavujem mod na: "+ mod);
	}
	
	public float getValue() {
		float[] sample = new float[gyro.sampleSize()];
		gyro.fetchSample(sample, 0);
		float vysledok;
		if(mod == 0) vysledok = sample[mod] + uholPredResetom;
		else vysledok = sample[mod];
		if(vysledok != Double.NaN && vysledok != Double.POSITIVE_INFINITY && vysledok != Double.NEGATIVE_INFINITY) return vysledok;
		return 0;
	}
	
	public float getError(float hodnota) {
		return hodnota;//-uholPredresetom
	}

}
