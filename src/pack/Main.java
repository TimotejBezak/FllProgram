package pack;

import lejos.hardware.Brick;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import lejos.utility.Stopwatch;

public class Main {

	static Vyjazd[] vyjazdy;
    static Vyjazd selected;
    static boolean beziVyjazd;
	
	public static void main(String[] args) {
		Stopwatch sw = new Stopwatch();
        vyjazdy = new Vyjazd[]{new PrvyVyjazd(), new PrvyVyjazd(), new PrvyVyjazd()};
        Vyjazd.init();
        KresliMenu();
        Brick brick = LocalEV3.get();
        selected = vyjazdy[0];
        selected.selected = true;
        KresliMenu();
        boolean[] predosleStlacene = new boolean[]{false, false, false, false, false, false};
        while (true){
            int pressed = brick.getKeys().getButtons();
            boolean[] stlacene = new boolean[6];
            for (int i = 5; i >= 0; i--) {
                stlacene[i] = (pressed & (1 << i)) != 0;
            }
            if(!beziVyjazd) {
                if (stlacene[0] && !predosleStlacene[0]) {
                    Select(-1);
                    KresliMenu();
                } else if (stlacene[1] && !predosleStlacene[1]) {
                    SpustiVyjazd();
                    if(selected.poradie == 0) sw.reset();
                } else if (stlacene[2] && !predosleStlacene[2]) {
                    Select(1);
                    KresliMenu();
                } else if (stlacene[5] && !predosleStlacene[5]){
                    break;
                }
            }
            else if(selected.thread.isAlive()){
               if(stlacene[5] && !predosleStlacene[5]){
//                    ZastavVyjazd();
                }
            }
            predosleStlacene = stlacene;
        }
	}

	public static void Select(int dir) {
		if (selected.poradie + dir < vyjazdy.length && selected.poradie + dir >= 0) {
	            selected.selected = false;
	            selected = vyjazdy[selected.poradie + dir];
	            selected.selected = true;
        }
	}

	private static void KresliMenu() {
		// TODO Auto-generated method stub
		LCD.clear();
        for (int i = 0; i < vyjazdy.length; i++){
            vyjazdy[i].poradie = i;
            vyjazdy[i].KresliMenu();
        }
//        int millis = sw.elapsed();
//        LCD.drawString((millis /60000) + ":" + (millis/1000),5,5);
        if(beziVyjazd){
            LCD.drawString("Bezi vyjazd " + selected.meno, 0, 6);
        }
		
	}

	public static void SpustiVyjazd() {
		selected.thread = new Thread(selected, selected.meno);
        selected.thread.start();
        beziVyjazd = true;
        KresliMenu();
		
	}

	public static void ZastavVyjazd() {
		beziVyjazd = false;
        selected.thread.interrupt();
        selected.Stop();
        KresliMenu();
	}
}
