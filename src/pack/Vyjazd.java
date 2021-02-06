package pack;

import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public abstract class Vyjazd implements Runnable {

	public Thread thread;
	public String meno;
	public int poradie;
	protected static Robot robot;
	protected Stopwatch sw;
	public boolean selected;
	
	public Vyjazd(String meno){
        this.meno = meno;
        sw = new Stopwatch();
    }
	
	public static void init() {
		robot = new Robot();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		System.out.println("Spustam vyjazd " + meno + poradie);
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
