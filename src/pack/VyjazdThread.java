package pack;

public class VyjazdThread extends Thread {
	private volatile boolean active;
	private Vyjazd vyjazd;
	
	
	public VyjazdThread(Vyjazd vyjazd) {
		this.vyjazd = vyjazd;
	}
	
	@Override
	public void run() {
		active = true;
		vyjazd.Spusti();
		active = false;
	}
	
	public void deactivate() {
		active = false;
	}
	
	public boolean isActive() {
		return active;
	}
}
