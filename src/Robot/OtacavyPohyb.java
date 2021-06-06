package Robot;

public class OtacavyPohyb extends ControlledMovement {
	protected float rychlost1, rychlost2;
	public OtacavyPohyb(Chassis chassis, float Rychlost1, float Rychlost2, Sensor sensor) {
		super(chassis, 0, sensor);
		this.rychlost1 = Rychlost1;
		this.rychlost2 = Rychlost2;
	}
	
	
	@Override
	protected void koniec() {}

	@Override
	protected float getLeftSpeed(float hodnota) {
		return rychlost1;
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		return rychlost2;
	}

	@Override
	protected void initMovement(int angle, int acc) {
		chassis.adjustSpeed(10,10);
		chassis.left.startSynchronization();
		if(rychlost1 > 0)chassis.left.backward();
		else chassis.left.forward();
		if(rychlost2 > 0)chassis.right.backward();
		else chassis.right.forward();
		chassis.left.endSynchronization();
	}
}
