package pack;

public class VyjazdThread extends Thread {
	private volatile boolean active;
	private Vyjazd vyjazd;
	
	
	public VyjazdThread(Vyjazd vyjazd) {
		this.vyjazd = vyjazd;
	}
	
	@Override
	public void run() {
		try {
			active = true;
			vyjazd.Zacni();
			active = false;
		}
		catch(InterruptedException e) {
			System.out.println("Vyjazd bol zruseny");
		}
		finally {
			deactivate();
			vyjazd.Stop();
		}
	}
	
	public void deactivate() {
		System.out.print("\n\n\n\n\n\n\n\n");
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
}
