package Robot;

public class OtocitPoCiaru extends OtacavyPohyb {
	private boolean BolaCiara = false;
	public OtocitPoCiaru(Chassis chassis, float Rychlost1, float Rychlost2, Farebnik sensor) {
		super(chassis, Rychlost1, Rychlost2, sensor);
		sensor.SetHandlers(new FarebnikEvents() {

			@Override
			public void PridenieNaCiernu() {
				// TODO Auto-generated method stub
				BolaCiara = true;
			}

			@Override
			public void PridenieNaBielu() {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	@Override
	protected boolean isActive(float hodnota) {
		// TODO Auto-generated method stub
		return !BolaCiara;
	}
	@Override
	protected void koniec() {
		((Farebnik)sensor).RemoveHandlers();
	}
}
