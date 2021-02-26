package pack;

public class SpustacVyjazdov {
	private Vyjazd spusteny;
	private VyjazdThread thread;
     
    public SpustacVyjazdov() {
    	
    }
	public void spustiVyjazd(Vyjazd vyjazd) {
//	 	if (thread.isActive()) {
//	    	return;
//	    }
	 	thread = new VyjazdThread(vyjazd);
	 	thread.start();
	    spusteny = vyjazd;
	}
 
    public void zastav() {
    	if(spusteny != null) {
    		thread.deactivate();
    		spusteny = null;
    	}
    }
    
    public boolean BeziVyjazd() {
    	return thread.isActive();
    }
}
