package Robot;

import lejos.hardware.port.Port;
import lejos.hardware.sensor.EV3ColorSensor;

public class Farebnik implements Sensor {

	private EV3ColorSensor sensor;
	private float chcenaHodnota = 0.50f;
	private Thread listenThread;
	private volatile boolean listenThreadActive;
	final float CIERNA = 0.09f;
	final float BIELA = 0.82f;
	
	public Farebnik(Port port) {
		sensor = new EV3ColorSensor(port);
		sensor.setCurrentMode(1);
	}
	
	public void SetHandlers(final FarebnikEvents handlers) {
		final Farebnik farebnik = this;
		listenThread = new Thread(new Runnable() {
			@Override
			public void run() {
				farebnik.listenThreadActive = true;
				float hodnota = farebnik.getValue();
				float predosla = hodnota;
				while(farebnik.listenThreadActive) {
					hodnota = farebnik.getValue();
					System.out.println("h "+hodnota);
					if(predosla < BIELA && hodnota > BIELA) {
						handlers.PridenieNaBielu();
					}
					if(predosla > CIERNA && hodnota < CIERNA) {
						handlers.PridenieNaCiernu();
					}
					
					predosla = hodnota;
				}
			}
		});
	}
	
	public void RemoveHandlers() {
		this.listenThreadActive = false;
		this.listenThread = null;
	}
	
	@Override
	public float getValue() {
		float[] sample = new float[sensor.sampleSize()];
		sensor.fetchSample(sample, 0);
		return sample[0];
	}

	@Override
	public float getError(float hodnota) {
		return 100*(hodnota-chcenaHodnota);
	}
	
	public void reset() {
		
	}

	public void nastavMod(int i) {
		
	}
}
