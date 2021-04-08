package Robot;

public class PIDControlledMovement extends ControlledMovement {

	protected PIDController leftController;
	protected PIDController rightController;
	
	public PIDControlledMovement(Chassis _chassis, Sensor sensor, PIDController _right, PIDController _left, float wantedSpeed) {
		super(_chassis, wantedSpeed, sensor);
		leftController = _left;
		rightController = _right;
		chassis = _chassis;
		leftController.Reset();
		rightController.Reset();
	}
	
	private float zistiRychlostRobota() {
		return (chassis.aktualnaRychlostLavy()+chassis.aktualnaRychlostPravy())/-2;
	}
	
	
	/*if(speed < 70) {
				hodnota = 0.9f;
			}
			
			vysledokzpidu = (float)leftController.getOutput(sensor.getError(hodnota));
			
			float senzitivita;
			if(speed < 160 && predoslaRychlost < speed) senzitivita = 1000;
			else senzitivita = 250;*/

	private float PIDmaxVal=50;
	@Override
	protected float getLeftSpeed(float hodnota) {
		float vysledokzpidu = (float)leftController.getOutput(sensor.getError(hodnota));
		if(vysledokzpidu > PIDmaxVal) vysledokzpidu = PIDmaxVal;
		else if (vysledokzpidu < -PIDmaxVal) vysledokzpidu = -PIDmaxVal;
		if(sensor instanceof Farebnik) {			
			float rychlost = speed + vysledokzpidu;
//			float druhaRychlost = speed + (float)rightController.getOutput(sensor.getError(hodnota));//zistiRychlostRobota()
//			if(druhaRychlost > 800) {
//				rychlost -= druhaRychlost-800;
//			}	
			
			return rychlost;
		}
		
		//System.out.println("lavy "+speed+"  zmenilo sa o "+vysledokzpidu*zistiRychlostRobota());
		if(vysledokzpidu<0)return speed + vysledokzpidu;
		else return speed;
	}

	@Override
	protected float getRightSpeed(float hodnota) {
		float vysledokzpidu = (float)rightController.getOutput(sensor.getError(hodnota));;
		if(vysledokzpidu > PIDmaxVal) vysledokzpidu = PIDmaxVal;
		else if (vysledokzpidu < -PIDmaxVal) vysledokzpidu = -PIDmaxVal;
		if(sensor instanceof Farebnik) {
			//float senzitivita = 250;
			float rychlost = speed + vysledokzpidu;
//			float druhaRychlost = speed + (float)leftController.getOutput(sensor.getError(hodnota));
//			if(druhaRychlost > 800) {
//				rychlost -= druhaRychlost-800;
//			}
			
			return rychlost;
		}
		
		//System.out.println("pravy "+speed+"  zmenilo sa o "+vysledokzpidu*zistiRychlostRobota()+"rychlost robota je "+zistiRychlostRobota());
		if(vysledokzpidu<0)return speed + vysledokzpidu;
		else return speed;
		
	}

	@Override
	protected void initMovement(int angle, int acc) {
		sensor.reset();
		super.initMovement(angle, acc);
		
	}
	
	public void koniec() {
		this.leftController.vypis_hodnoty_do_grafu('l');
		this.rightController.vypis_hodnoty_do_grafu('p');
		this.leftController.resetovat_grafovanie();
		this.rightController.resetovat_grafovanie();
	}

}
