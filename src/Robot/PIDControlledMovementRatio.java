package Robot;

public class PIDControlledMovementRatio extends PIDControlledMovement {

	public PIDControlledMovementRatio(Chassis _chassis, float speed, Sensor sensor, PIDController controller) {
		super(_chassis,sensor, controller, controller, speed);
	}
	
	private float getRatio(float hodnota) {
		//System.out.println(sensor.getError(hodnota));
		//chyba: y
		float vysledok = (float)leftController.getOutput(sensor.getError(hodnota))+1;
		if(vysledok > 2) return 2;
		if (vysledok < 0) return 0;
		return vysledok;
	}
	
	@Override
	protected float getLeftSpeed(float hodnota) {
		float ratio = getRatio(hodnota);
		
		return ratio > 1 ? speed / ratio : speed;
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		float ratio = getRatio(hodnota);
		return ratio < 1 ? speed * ratio : speed;
	}
}
