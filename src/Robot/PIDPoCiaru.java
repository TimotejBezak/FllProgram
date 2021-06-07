package Robot;

public class PIDPoCiaru extends PIDControlledMovement {
	private volatile boolean bolaCiara = false;
	public PIDPoCiaru(Chassis _chassis, float speed, Sensor sensor, PIDController right, PIDController left, final boolean idemNaCiernu) {
		super(_chassis,sensor, right, left, speed);
		assert sensor instanceof Farebnik;
		bolaCiara = false;
		final Farebnik s = (Farebnik)sensor;
		s.SetHandlers(new FarebnikEvents() {
			public void PridenieNaBielu() {
				if(!idemNaCiernu) {
					bolaCiara = true;
					s.RemoveHandlers();
				}
			}
			public void PridenieNaCiernu() {
				if(idemNaCiernu) {
					bolaCiara = true;
					s.RemoveHandlers();
				}
			}
		});
	}
	
	@Override
	protected boolean isActive(float hodnota) {
		return !bolaCiara;
	}
}
