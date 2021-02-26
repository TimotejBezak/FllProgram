package Robot;

public class PIDController {
	private double P;
	private double I;
	private double D;
	
	private double lastError = 0;
	private double lastErrorSum = 0;
	private long lastCall;
	
	
	public PIDController(final double p, final double i, final double d) {
		this.P = p;
		this.I = i;
		this.D = d;
		lastCall = System.currentTimeMillis();
	}
	
	public double getOutput(double error) {
		double dt = getDt();
		double derivative = (error - lastError) / dt;
		double integral = lastErrorSum + error * dt;
		
		lastError = error;
		lastErrorSum += error * dt; 
		
		return P*error + D*derivative + I*integral;
	}
	
	private double getDt() {
		long now = System.currentTimeMillis();
		long delta = now - lastCall;
		lastCall = now;
		return ((double)delta)/1000;
	}
	
	public void Reset() {
		lastCall = System.currentTimeMillis();
	}
	
	public void Calibrate(final double p, final double i, final double d) {
		this.P = p;
		this.I = i;
		this.D = d;
	}
}
