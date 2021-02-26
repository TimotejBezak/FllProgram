package Robot;

public class OtocitPoUhol extends ControlledMovement {
	private float rychlost1;
	private float rychlost2;
	private float chcenyuhol;
	private float rozdielUhlov;
	private float zaciatocnyUhol;
	
	public OtocitPoUhol(float rychlost1, float rychlost2, float chcenyuhol, Chassis chassis, Gyro gyro) {
		super(chassis,0,gyro);
		this.rychlost1 = rychlost1;
		this.rychlost2 = rychlost2;
		this.chcenyuhol = chcenyuhol;
		this.zaciatocnyUhol = gyro.getValue();
		this.rozdielUhlov = chcenyuhol-this.zaciatocnyUhol;
	}
	
	float p = 4.0f;
	float minimalnaRychlost = 0.2f;
	
	
	float rychlostpodlauhlu(float x) {
		if(x == 0) return 0.2f;
		if(x>1 || x<0) return 0;
		//return 0.05f-3.8f*x*(x-1);
		float vysledok;
		if(x <= 1/p) vysledok = p*x;
		else vysledok = -(p/(p-1))*x+1+1/(p-1);
		if(vysledok <= minimalnaRychlost && x < 0.5) return minimalnaRychlost;
		else return vysledok;
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

	@Override
	protected float getLeftSpeed(float hodnota) {
		// TODO Auto-generated method stub
		float percenta = Math.abs(hodnota-this.zaciatocnyUhol)/this.rozdielUhlov;
		return rychlostpodlauhlu(percenta)*rychlost1;
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		// TODO Auto-generated method stub
		float percenta = Math.abs(hodnota-this.zaciatocnyUhol)/this.rozdielUhlov;
		return rychlostpodlauhlu(percenta)*rychlost2;
	}
	
	protected boolean isActive(float hodnota) {
		if(rozdielUhlov > 0) {
			if(hodnota >= chcenyuhol) return false;
		} else {
			if(hodnota <= chcenyuhol) return false;
		}
		return true;
	}
	

}
