package pack;

import Robot.Robot;
import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public abstract class Vyjazd{

	public String meno;
	public int poradie;
	protected static volatile Robot robot;
	protected Stopwatch sw;
	public boolean selected;
	
	public Vyjazd(String meno){
        this.meno = meno;
        sw = new Stopwatch();
    }
	
	public static void init() {
		robot = new Robot();
	}
	
	public void Zacni() {
		System.out.println("Spustam vyjazd " + meno + poradie + Thread.currentThread().isDaemon());
		// TODO Auto-generated method stub
        Spusti();
        Main.ZastavVyjazd();
        Main.Select(1);
	}

	protected abstract void Spusti();
	
	public void Stop(){
        robot.Stop();
//        System.out.println("Zastavujem vyjazd " + meno + poradie);
    }
	
	public void KresliMenu(){
        String menu;
        if(selected) menu = ">";
        else menu = "•";
        menu += meno;
        LCD.drawString(menu, 0, poradie);
    }
}
