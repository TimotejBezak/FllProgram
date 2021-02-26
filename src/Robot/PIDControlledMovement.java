package Robot;

public class PIDControlledMovement extends ControlledMovement {

	protected PIDController leftController;
	protected PIDController rightController;
	
	public PIDControlledMovement(Chassis _chassis, Sensor sensor, PIDController _right, PIDController _left, float wantedSpeed) {
		super(_chassis, wantedSpeed, sensor);
		leftController = _left;
		rightController = _right;
	}
	
	@Override
	protected float getLeftSpeed(float hodnota) {
		return speed + (float)leftController.getOutput(sensor.getError(hodnota));
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		return speed + (float)rightController.getOutput(sensor.getError(hodnota));
	}

	@Override
	protected void initMovement(int angle, int acc) {
		sensor.reset();
		super.initMovement(angle, acc);
		
	}

}
