package Robot;

public class PIDControlledMovementRatio extends PIDControlledMovement {

	public PIDControlledMovementRatio(Chassis _chassis, float speed, Sensor sensor, PIDController controller) {
		super(_chassis,sensor, controller, controller, speed);
	}
	
	private float getRatio(float hodnota) {
		return (float)leftController.getOutput(sensor.getError(hodnota))+1;
	}
	
	@Override
	protected float getLeftSpeed(float hodnota) {
		float ratio = getRatio(hodnota);
		return ratio < 1 ? speed : speed / ratio;
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		float ratio = getRatio(hodnota);
		return ratio > 1 ? speed * ratio : speed;
	}
}
