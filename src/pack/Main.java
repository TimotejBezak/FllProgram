package pack;

import Vyjazdy.DruhyVyjazd;
import Vyjazdy.PrvyVyjazd;
import Vyjazdy.piatyVyjazd;
import Vyjazdy.stvrtyVyjazd;
import Vyjazdy.tretiVyjazd;
import lejos.hardware.Brick;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public class Main {

	static Vyjazd[] vyjazdy;
    static Vyjazd selected;
    static SpustacVyjazdov spustac;
	static boolean beziVyjazd = false;
    
	public static void main(String[] args) {
		Stopwatch sw = new Stopwatch();
        vyjazdy = new Vyjazd[]{new PrvyVyjazd(), new DruhyVyjazd(), new tretiVyjazd(), new stvrtyVyjazd(), new piatyVyjazd()};
        spustac = new SpustacVyjazdov();
//        vyjazdThread = new Thread(spustac);
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
//                    System.out.println("Spustam vyjazd: "+ selected.poradie);
                    if(selected.poradie == 0) sw.reset();
                } else if (stlacene[2] && !predosleStlacene[2]) {
                    Select(1);
                    KresliMenu();
                } else if (stlacene[5] && !predosleStlacene[5]){
                    break;
                }
            }
            else {
               if(stlacene[5] && !predosleStlacene[5]){
                    ZastavVyjazd();
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
		LCD.clear();
        for (int i = 0; i < vyjazdy.length; i++){
            vyjazdy[i].poradie = i;
            vyjazdy[i].KresliMenu();
        }
        if(beziVyjazd){
            LCD.drawString("Bezi vyjazd " + selected.meno, 0, 6);
        }
		
	}

	public static void SpustiVyjazd() {
		beziVyjazd = true;
		spustac.spustiVyjazd(selected);
        KresliMenu();	
	}

	public static void ZastavVyjazd() {
		beziVyjazd = false;
		spustac.zastav();
		KresliMenu();
	}
}

