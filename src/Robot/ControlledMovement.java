package Robot;

import java.util.ArrayList;
import java.time.LocalTime;
import pack.Main;

public abstract class ControlledMovement {
	protected Chassis chassis;
	protected float speed;
	protected Sensor sensor;
	private ArrayList<Float> uhly_gyra_do_grafu = new ArrayList<Float>();
	private ArrayList<Float> rychlost_gyra_do_grafu = new ArrayList<Float>();
	private ArrayList<Float> zrychlenie_gyra_do_grafu = new ArrayList<Float>();
	private ArrayList<Double> cas_do_grafu = new ArrayList<Double>();
	private boolean grafovat = false;
	private long cas_na_zaciatku;
	
	
	public ControlledMovement(Chassis _chassis, float speed, Sensor sensor) {
		chassis = _chassis;
		this.speed = speed;
		this.sensor = sensor;
	}
	
	private void grafovanie(float hodnota) {
		this.cas_do_grafu.add((double) System.currentTimeMillis()-cas_na_zaciatku);
		
		this.rychlost_gyra_do_grafu.add(hodnota);			
		this.sensor.nastavMod(0);//odkomentovat ked sa negrafuje
		this.uhly_gyra_do_grafu.add(sensor.getValue());
		this.sensor.nastavMod(1);
		
		try {
			float rozdiel_rychlosti = rychlost_gyra_do_grafu.get(rychlost_gyra_do_grafu.size()-1)-rychlost_gyra_do_grafu.get(rychlost_gyra_do_grafu.size()-2);
			float rozdiel_casu = (float)(cas_do_grafu.get(cas_do_grafu.size()-1) - cas_do_grafu.get(cas_do_grafu.size()-2));
			this.zrychlenie_gyra_do_grafu.add(rozdiel_rychlosti/rozdiel_casu);
		}
		catch(Exception e) {
			this.zrychlenie_gyra_do_grafu.add(0.0f);
		}
	}
	
	public void execute(int acc, int angle) throws InterruptedException {
		execute(acc,angle,0);
	}
	
	public void execute(int acc, int angle, int spomalenie) throws InterruptedException {
		initMovement(angle,acc);
		
		int zaciatocneOtocenie=0,otocenieSpomalovania=0,kedyZacatSpomalovat=0,otocenieZrychlenia=0,kedyPrestatZrychlovat=0;
		
		if(spomalenie != 0) {
			zaciatocneOtocenie = chassis.otocenieMotorov();                          //this.chassis.lengthToAngle(spomalenie);
			otocenieSpomalovania = (int)((0.5*speed*speed)/spomalenie);
			kedyZacatSpomalovat = Math.abs(angle-otocenieSpomalovania);
			
			otocenieZrychlenia = (int)((0.5*speed*speed)/acc);
			kedyPrestatZrychlovat = otocenieZrychlenia;
		}
		
		if(spomalenie != 0)chassis.nastavitVelkeZrychlenie();
		
		cas_na_zaciatku = System.currentTimeMillis();
		
		int otocenie = 0;
		
		float hodnota = sensor.getValue();
		chassis.adjustSpeed(getLeftSpeed(hodnota), getRightSpeed(hodnota));
		if(grafovat)grafovanie(hodnota);
		while(isActive(hodnota) && Main.beziVyjazd()) {
			
			if(spomalenie != 0) {
				otocenie = Math.abs(chassis.otocenieMotorov()-zaciatocneOtocenie);
				if(otocenie == 0) otocenie = 10;
				
				if(otocenie < kedyPrestatZrychlovat) {
					speed = (float) (acc*Math.sqrt(otocenie/(0.5*acc)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
					//System.out.println("zrychlovanie rychlost "+speed);
				}
				
				int otocenieDoKonca = Math.abs(angle-otocenie);
				if(otocenie >= kedyZacatSpomalovat) {
					speed = (float) (spomalenie*Math.sqrt(otocenieDoKonca/(0.5*spomalenie)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
					//System.out.println("spomalovanie rychlost "+speed);
				}
				
			}
			
			hodnota = sensor.getValue();
			System.out.println(hodnota+"hodnota senzoru");
			
			if(grafovat)grafovanie(hodnota);
			
			float pravy = getRightSpeed(hodnota);
			float lavy = getLeftSpeed(hodnota);
			//System.out.println("l "+lavy+" p "+pravy);
			//System.out.println("Pravy: " + pravy + "	Lavy: " + lavy + "gyro: " + hodnota + "chyba " + sensor.getError(hodnota));
			chassis.adjustSpeed(lavy, pravy);
		}
		
		chassis.stopMovement();
		System.out.println("hodnota senzora na konci: " + sensor.getValue());
		if(grafovat) {
			koniec();
			System.out.println("cas = "+this.cas_do_grafu);
			System.out.println("uhol = "+this.uhly_gyra_do_grafu);
			System.out.println("rychlost = "+this.rychlost_gyra_do_grafu);
			System.out.println("zrychlenie = "+this.zrychlenie_gyra_do_grafu);
		}
		if(!Main.beziVyjazd()) throw new InterruptedException();
	}

	protected abstract void koniec();

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
