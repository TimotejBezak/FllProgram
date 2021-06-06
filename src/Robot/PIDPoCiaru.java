package Robot;

public class PIDPoCiaru extends PIDControlledMovement {
	private volatile boolean bolaCierna = false;
	public PIDPoCiaru(Chassis _chassis, float speed, Sensor sensor, PIDController right, PIDController left) {
		super(_chassis,sensor, right, left, speed);
		assert sensor instanceof Farebnik;
		bolaCierna = false;
		final Farebnik s = (Farebnik)sensor;
		s.SetHandlers(new FarebnikEvents() {
			public void PridenieNaBielu() {
				
			}
			public void PridenieNaCiernu() {
				bolaCierna = true;
				s.RemoveHandlers();
			}
		});
	}
	
	@Override
	protected boolean isActive(float hodnota) {
		return !bolaCierna;
	}
}
