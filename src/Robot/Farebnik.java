package Robot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class Farebnik implements Sensor {

	private EV3ColorSensor sensor;
	private float chcenaHodnota = 0.50f;
	
	public Farebnik(Port port) {
		sensor = new EV3ColorSensor(port);
	}
	
	@Override
	public float getValue() {
		float[] sample = new float[sensor.sampleSize()];
		sensor.fetchSample(sample, 0);
		return sample[0];
	}

	@Override
	public float getError(float hodnota) {
		return hodnota-chcenaHodnota;
	}
	
	public void reset() {
		
	}

}
