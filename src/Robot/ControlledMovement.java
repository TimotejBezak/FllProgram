package Robot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import lejos.utility.Delay;
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
	
	private float a1,a2,b1,b2;
	private float zrychlovacieOtocenie,spomalovacieOtocenie;
	private void zistiFunkcie(int zrychlenie, int spomalenie, int celkovaVzdialenost) {
		zrychlovacieOtocenie = (float)((0.5*speed*speed)/zrychlenie);
		spomalovacieOtocenie = (float)((0.5*speed*speed)/spomalenie);
		if(spomalovacieOtocenie < 0) spomalovacieOtocenie = 0.5f;
		
		System.out.println("zs "+zrychlovacieOtocenie+"  "+spomalovacieOtocenie+"  "+celkovaVzdialenost);
		
//		if(zrychlovacieOtocenie > celkovaVzdialenost/2) {
//			zrychlovacieOtocenie = 0.5f;
//			spomalovacieOtocenie = 0.5f;
//		}
		
		zrychlovacieOtocenie = zrychlovacieOtocenie/celkovaVzdialenost;
		spomalovacieOtocenie = spomalovacieOtocenie/celkovaVzdialenost;
		
		//body su [0,0] [zrychlovacieOtocenie,1]   
		//0 = a0 + b       1 = a*zrychlovacieOtocenie + b
		
		a1 = 1/zrychlovacieOtocenie;//  /2
		b1 = 0.0f;
		a2 = -1/spomalovacieOtocenie;//a2 = -a1;
		b2 = -a2;
		
		spomalovacieOtocenie = 1-spomalovacieOtocenie;
		
		/*if(zrychlovacieOtocenie > 0.5f) {
			zrychlovacieOtocenie = 0.5f;
			spomalovacieOtocenie = 0.5f;
		}*/
		
		if(spomalovacieOtocenie < zrychlovacieOtocenie) {
			//a1*x+b1=a2*x+b2
			//a1*x+b1-b2=a2*x
			//b1-b2=a2*x-a1*x
			//b1-b2=x*(a2-a1)
			//(b1-b2)/(a2-a1)=x
			float x = (b1-b2)/(a2-a1);
			spomalovacieOtocenie = x;
			zrychlovacieOtocenie = x;
		}
		
		System.out.println("a1: "+a1+", b1: "+b1+", a2: "+a2+", b2: "+b2+"   "+zrychlovacieOtocenie+"  "+spomalovacieOtocenie);
	}
	
	protected float p = 4.0f;
	protected float minimalnaRychlost = 0.1f;
	protected float rychlostPodlaVzdialenosti(float x) {//0.113   0.145
		//if(x == 0) return 0.2f;
		if(x>1 || x<0) return 0;
		//return 0.05f-3.8f*x*(x-1);
		float vysledok=1;
		if(x <= zrychlovacieOtocenie) vysledok = a1*x+b1;
		if(x >= spomalovacieOtocenie) vysledok = a2*x+b2;
		if(vysledok < minimalnaRychlost) return minimalnaRychlost;
		return vysledok;
	}
	
	class pravidelneVolatFunkciu extends TimerTask{
		private int spomalenie;
		private int otocenie;
		private int angle;
		private int otocenieSpomalovania;
		private int otocenieZrychlenia;
		private int zaciatocneOtocenie;
		private float zabacanie;
		public float hodnota;
		public boolean hasStarted = false;
		public pravidelneVolatFunkciu(int acc, int angle, int spomalenie, float zabacanie) {
			initMovement(angle,100000);
			this.spomalenie = spomalenie;
			this.angle = angle;
			this.zabacanie = zabacanie;
			int zaciatocneOtocenie=0,otocenieSpomalovania=0,kedyZacatSpomalovat=0,otocenieZrychlenia=0,kedyPrestatZrychlovat=0;
			
			this.angle = Math.abs(angle);
			
			if(spomalenie != 0)zistiFunkcie(acc,spomalenie,this.angle);
			
			if(spomalenie != 0) {
				zaciatocneOtocenie = chassis.otocenieMotorov();                          //this.chassis.lengthToAngle(spomalenie);
				otocenieSpomalovania = (int)((0.5*speed*speed)/spomalenie);
				kedyZacatSpomalovat = Math.abs(angle-otocenieSpomalovania);
				
				otocenieZrychlenia = (int)((0.5*speed*speed)/acc);
				kedyPrestatZrychlovat = otocenieZrychlenia;
			}
			
			this.otocenieSpomalovania = otocenieSpomalovania;
			this.otocenieZrychlenia = otocenieZrychlenia;
			this.zaciatocneOtocenie = zaciatocneOtocenie;
			
			if(spomalenie != 0)chassis.nastavitVelkeZrychlenie();
			
			cas_na_zaciatku = System.currentTimeMillis();
			
			this.otocenie = 0;
			
			this.hodnota = sensor.getValue();
			if(grafovat)grafovanie(hodnota);
			hasStarted = true;
		}
		@Override
		public void run() {
			//System.out.println("cas je: "+System.currentTimeMillis()+"  ");
			float speedMultiplier = 1;  
			if(spomalenie != 0) {
				otocenie = Math.abs(chassis.otocenieMotorov()-zaciatocneOtocenie);
				float percenta = (float)otocenie / (float)angle;
//				if(otocenie < kedyPrestatZrychlovat) {
//					speed = (float) (acc*Math.sqrt(otocenie/(0.5*acc)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
//					//System.out.println("zrychlovanie rychlost "+speed);
//				}
//				
//				int otocenieDoKonca = Math.abs(angle-otocenie);
//				if(otocenie >= kedyZacatSpomalovat) {
//					speed = (float) (spomalenie*Math.sqrt(otocenieDoKonca/(0.5*spomalenie)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
//					//System.out.println("spomalovanie rychlost "+speed);
//				}
				speedMultiplier = rychlostPodlaVzdialenosti(percenta);
				//System.out.println(percenta+" %: " + speedMultiplier);
			}
			this.hodnota = sensor.getValue();
			
			if(grafovat)grafovanie(hodnota);
			
			float pravy = getRightSpeed(hodnota) * speedMultiplier + zabacanie;
			float lavy = getLeftSpeed(hodnota) * speedMultiplier - zabacanie;
			
			//assert lavy < speed;
			//System.out.println("l "+lavy+" p "+pravy);
			//System.out.println("Pravy: " + pravy + "	Lavy: " + lavy + "gyro: " + hodnota + "chyba " + sensor.getError(hodnota));
			chassis.adjustSpeed(lavy, pravy);
		}
	}
	
	public void execute(int acc, int angle, int spomalenie) throws InterruptedException {
		execute(acc,angle,spomalenie,1);
	}
	
	public void execute(int acc, int angle, int spomalenie,float zabacanie) throws InterruptedException {
//		initMovement(angle,100000);
//		
//		int zaciatocneOtocenie=0,otocenieSpomalovania=0,kedyZacatSpomalovat=0,otocenieZrychlenia=0,kedyPrestatZrychlovat=0;
//		
//		angle = Math.abs(angle);
//		
//		if(spomalenie != 0)zistiFunkcie(acc,angle);
//		
//		if(spomalenie != 0) {
//			zaciatocneOtocenie = chassis.otocenieMotorov();                          //this.chassis.lengthToAngle(spomalenie);
//			otocenieSpomalovania = (int)((0.5*speed*speed)/spomalenie);
//			kedyZacatSpomalovat = Math.abs(angle-otocenieSpomalovania);
//			
//			otocenieZrychlenia = (int)((0.5*speed*speed)/acc);
//			kedyPrestatZrychlovat = otocenieZrychlenia;
//		}
//		
//		if(spomalenie != 0)chassis.nastavitVelkeZrychlenie();
//		
//		cas_na_zaciatku = System.currentTimeMillis();
//		
//		int otocenie = 0;
//		
//		float hodnota = sensor.getValue();
//		if(grafovat)grafovanie(hodnota);
//		
		Timer timer = new Timer();
		pravidelneVolatFunkciu volac = new pravidelneVolatFunkciu(acc,angle,spomalenie,zabacanie);
		timer.schedule(volac, 0, 20);
		Delay.msDelay(50);
		while(isActive(volac.hodnota) && Main.beziVyjazd()) {			
//			float speedMultiplier = 1;  
//			if(spomalenie != 0) {
//				otocenie = Math.abs(chassis.otocenieMotorov()-zaciatocneOtocenie);
//				float percenta = (float)otocenie / (float)angle;
////				if(otocenie < kedyPrestatZrychlovat) {
////					speed = (float) (acc*Math.sqrt(otocenie/(0.5*acc)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
////					//System.out.println("zrychlovanie rychlost "+speed);
////				}
////				
////				int otocenieDoKonca = Math.abs(angle-otocenie);
////				if(otocenie >= kedyZacatSpomalovat) {
////					speed = (float) (spomalenie*Math.sqrt(otocenieDoKonca/(0.5*spomalenie)));//                                              (float) Math.sqrt(0.5*spomalenie*otocenie);
////					//System.out.println("spomalovanie rychlost "+speed);
////				}
//				speedMultiplier = rychlostPodlaVzdialenosti(percenta);
//				System.out.println(percenta+" %: " + speedMultiplier);
//			}
//			hodnota = sensor.getValue();
//			
//			if(grafovat)grafovanie(hodnota);
//			
//			float pravy = getRightSpeed(hodnota) * speedMultiplier;
//			float lavy = getLeftSpeed(hodnota) * speedMultiplier;
//			
//			//assert lavy < speed;
//			//System.out.println("l "+lavy+" p "+pravy);
//			System.out.println("Pravy: " + pravy + "	Lavy: " + lavy + "gyro: " + hodnota + "chyba " + sensor.getError(hodnota));
//			chassis.adjustSpeed(lavy, pravy);
		}
		timer.cancel();
		
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
		chassis.initSynchronizedMovement(10, acc);
		chassis.beginForwardMovement(angle);
	}
	
	protected abstract float getLeftSpeed(float hodnota);

	protected abstract float getRightSpeed(float hodnota); 
}
