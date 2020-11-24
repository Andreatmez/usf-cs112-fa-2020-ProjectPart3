package ProjectPart2;

public class DataPoint {
	
	private boolean survived;
	private double f1;
	private double f2;
	
	public DataPoint(boolean survived, double f1, double f2) {
		this.survived = survived;
		this.f1 = f1;
		this.f2 = f2;
	}
	
	public DataPoint() {
		this(false, 0, 0);
	}
	
	public boolean getSurvived() {
		return survived;
	}
	
	public void setSurvived(boolean survived) {
		this.survived = survived;
	}
	
	public double getF1() {
		return f1;
	}
	
	public void setF1(double f1) {
		this.f1 = f1;
	}
	
	public double getF2() {
		return f2;
	}
	
	public void setFare(double f2) {
		this.f2 = f2;
	}
	
	public String toString() {
		return "Survived: " + survived + " Age: " + f1 + " Fare: " + f2;
	}
		

}
