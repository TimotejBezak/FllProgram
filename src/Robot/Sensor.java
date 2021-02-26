package Robot;

public interface Sensor {
	public float getValue();
	public float getError(float hodnota);
	public void reset();
}
