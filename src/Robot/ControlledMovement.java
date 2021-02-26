package Robot;

public abstract class ControlledMovement {
	protected Chassis chassis;
	protected float speed;
	protected Sensor sensor;
	
	
	public ControlledMovement(Chassis _chassis, float speed, Sensor sensor) {
		chassis = _chassis;
		this.speed = speed;
		this.sensor = sensor;
	}
	
	public void execute(int acc, int angle) {
		initMovement(angle,acc);
		
		float hodnota = sensor.getValue();
		chassis.adjustSpeed(getLeftSpeed(hodnota), getRightSpeed(hodnota));
		while(isActive(hodnota)) {
			hodnota = sensor.getValue();
			chassis.adjustSpeed(getLeftSpeed(hodnota), getRightSpeed(hodnota));
		}
		chassis.stopMovement();
	}
	
	protected boolean isActive(float hodnota) {
		return chassis.isMoving();
	}
	
	protected void initMovement(int angle, int acc) {
		chassis.initSynchronizedMovement(speed, acc);
		chassis.beginForwardMovement(angle);
	}
	
	protected abstract float getLeftSpeed(float hodnota);

	protected abstract float getRightSpeed(float hodnota); 
}
