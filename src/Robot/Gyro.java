package Robot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3GyroSensor;

public class Gyro implements Sensor {
	private EV3GyroSensor gyro;
	private float uholPredResetom;
	
	public void reset() {
		uholPredResetom = getValue();
		gyro.reset();
	}
	
	public Gyro(Port port) {
		gyro = new EV3GyroSensor(port);
		gyro.reset();
		gyro.setCurrentMode(2);
	}
	
	public float getValue() {
		float[] sample = new float[gyro.sampleSize()];
		gyro.fetchSample(sample, 0);
		return sample[0] + uholPredResetom;
	}
	
	public float getError(float hodnota) {
		return hodnota - uholPredResetom;
	}
}
