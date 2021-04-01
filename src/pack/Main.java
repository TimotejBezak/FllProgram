package pack;

import Vyjazdy.DruhyVyjazd;
import Vyjazdy.PrvyVyjazd;
import Vyjazdy.piatyVyjazd;
import Vyjazdy.stvrtyVyjazd;
import Vyjazdy.tretiVyjazd;
import lejos.hardware.Audio;
import lejos.hardware.Brick;
import lejos.hardware.LED;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.utility.Stopwatch;

public class Main {

	static Vyjazd[] vyjazdy;
    static Vyjazd selected;
    static SpustacVyjazdov spustac;
    static Audio audio;
    static LED led;
    public static boolean beziVyjazd() {return spustac.BeziVyjazd(); }
    
	public static void main(String[] args) {
		Stopwatch sw = new Stopwatch();
		DrawMessage(3, "Nacitavam Vyjazdy");
        vyjazdy = new Vyjazd[]{new PrvyVyjazd(), new DruhyVyjazd(), new tretiVyjazd(), new stvrtyVyjazd(), new piatyVyjazd()};
        spustac = new SpustacVyjazdov();
        DrawMessage(3, "Zistujem motory");
        DrawMessage(4, "a senzory");
        Vyjazd.init();
        DrawMessage(3, "Hotovo");
        KresliMenu();
        Brick brick = LocalEV3.get();
        audio = brick.getAudio();
        led = brick.getLED();
        led.setPattern(0);
        selected = vyjazdy[0];
        selected.selected = true;
        KresliMenu();
        boolean[] predosleStlacene = new boolean[]{false, false, false, false, false, false};
        audio.systemSound(2);
        while (true){
            int pressed = brick.getKeys().getButtons();
            boolean[] stlacene = new boolean[6];
            for (int i = 5; i >= 0; i--) {
                stlacene[i] = (pressed & (1 << i)) != 0;
            }
            if(!spustac.BeziVyjazd()) {
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

	private static void DrawMessage(int row, String Message) {
		LCD.clear(row);
		LCD.drawString(Message, 0, row);
	}
	
	public static void Select(int dir) {
		
		if (selected.poradie + dir < vyjazdy.length && selected.poradie + dir >= 0) {
            selected.selected = false;
            selected = vyjazdy[selected.poradie + dir];
            selected.selected = true;
        }
	}

	private static void KresliMenu() {
		LCD.clearDisplay();
        for (int i = 0; i < vyjazdy.length; i++){
            vyjazdy[i].poradie = i;
            vyjazdy[i].KresliMenu();
        }
        if(spustac.BeziVyjazd()){
            LCD.drawString("Bezi vyjazd " + selected.meno, 0, 6);
        }
		
	}

	public static void SpustiVyjazd() {
		spustac.spustiVyjazd(selected);
        audio.systemSound(0);
        led.setPattern(selected.poradie+1);
		KresliMenu();
        
	}

	public static void ZastavVyjazd() {
		spustac.zastav();
		audio.systemSound(0);
		led.setPattern(0);
		KresliMenu();
	}
}

