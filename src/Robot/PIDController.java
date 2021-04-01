package Robot;

import java.util.ArrayList;
import java.util.List;

public class PIDController {
	private double P;
	private double I;
	private double D;
	
	private double lastError = 0;
	private double lastErrorSum = 0;
	private long lastCall;
	private List<Double> hodnoty_do_grafu_p;
	private List<Double> hodnoty_do_grafu_i;
	private List<Double> hodnoty_do_grafu_d;
	
	
	public PIDController(final double p, final double i, final double d) {
		this.P = p;
		this.I = i;
		this.D = d;
		resetovat_grafovanie();
		lastCall = System.currentTimeMillis();
	}
	
	public void resetovat_grafovanie() {
		this.hodnoty_do_grafu_p = new ArrayList<Double>();
		this.hodnoty_do_grafu_i = new ArrayList<Double>();
		this.hodnoty_do_grafu_d = new ArrayList<Double>();
	}
	
	public double getOutput(double error) {
		double dt = getDt();
		double derivative = (error - lastError) / dt;
		double integral = lastErrorSum + error * dt;
		
		lastError = error;
		lastErrorSum += error * dt; 
		
		double p_teraz = P*error;
		double i_teraz = I*integral;
		double d_teraz = D*derivative;
		
		this.hodnoty_do_grafu_p.add(p_teraz);
		this.hodnoty_do_grafu_i.add(i_teraz);
		this.hodnoty_do_grafu_d.add(d_teraz);
		return p_teraz + d_teraz + i_teraz;
	}
	
	public void vypis_hodnoty_do_grafu(char ktory) {
		System.out.println(ktory+"p = "+this.hodnoty_do_grafu_p);
		System.out.println(ktory+"i = "+this.hodnoty_do_grafu_i);
		System.out.println(ktory+"d = "+this.hodnoty_do_grafu_d);
	}
	
	private double getDt() {
		long now = System.currentTimeMillis();
		long delta = now - lastCall;
		lastCall = now;
		return ((double)delta)/1000;
	}
	
	public void Reset() {
		lastCall = System.currentTimeMillis();
		lastErrorSum = 0;
		lastError = 0;
	}
	
	public void Calibrate(final double p, final double i, final double d) {
		this.P = p;
		this.I = i;
		this.D = d;
	}
}
