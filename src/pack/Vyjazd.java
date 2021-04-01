package pack;

import java.util.HashMap;
import java.util.Map;

import Robot.PIDController;
import Robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public abstract class Vyjazd{

	public String meno;
	public int poradie;
	protected static volatile Robot robot;
	protected Stopwatch sw;
	public boolean selected;
	private Map<String, PIDController> pidy;
	
	public Vyjazd(String meno) {
		this(meno, new HashMap<String, PIDController>());
	}
	
	public Vyjazd(String meno, Map<String, PIDController> pidy){
        this.meno = meno;
        sw = new Stopwatch();
        this.pidy = pidy;
    }
	
	public static void init() {
		robot = new Robot();
	}
	
	public void Zacni() throws InterruptedException {
//		System.out.println("Spustam vyjazd " + meno + poradie + Thread.currentThread().isDaemon());
		// TODO Auto-generated method stub
		robot.gyroSensor.hardReset();
		robot.NastavPIDy(pidy);
        Spusti();
	}

	protected abstract void Spusti() throws InterruptedException;
	
	public void Stop(){
        robot.Stop();
        robot.Flt();
    }
	
	public void KresliMenu(){
        String menu;
        if(selected) menu = "> ";
        else menu = "- ";
        menu += meno;
        LCD.drawString(menu, 0, poradie+1);
    }
}
