package ProjectPart2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class KNNModel extends Model {

	private ArrayList<DataPoint> trainSet;
	private ArrayList<DataPoint> testSet;
	private ArrayList<Boolean> category;
	
	public int k;
	
	private int  valueT= 0;
	private int  valueF = 0;

	private double getDistance(DataPoint p1, DataPoint p2) {
		double result = Math.sqrt(Math.pow((p1.getF1()-p2.getF1()), 2)+
				Math.pow((p1.getF2()-p2.getF2()),2)); 
		return result;
	}
	
		
	public KNNModel(int k) {
		this.k = k;
		trainSet = new ArrayList<DataPoint>();
		testSet = new ArrayList<DataPoint>();
		category = new ArrayList<Boolean>();

	}
	
	void train(ArrayList<DataPoint> data) {	
		for(DataPoint passenger : data) {			
			if(passenger.getSurvived()) {
				valueT++;
			}else {
				valueF++;
			}			
		}
		trainSet.addAll(data);
	}
	
	String test(ArrayList<DataPoint> data) {	
		testSet = new ArrayList<DataPoint>();
		category = new ArrayList<Boolean>();
		testSet.addAll(data);

		Double[][] array = new Double[data.size()][2];;

		for(int i =0; i < data.size(); i++) {
			for(int j=0; j < trainSet.size(); j++) {
				double distanceTrainTest = getDistance(data.get(i), trainSet.get(j));
				array[i][0] = distanceTrainTest;			
				array[i][1] = trainSet.get(j).getSurvived() ? 1.0 : 0.0;
			}
		}
		
		Arrays.sort(array, new Comparator<Double[]>() {
			public int compare(Double[] a, Double[] b) {
				return a[0].compareTo(b[0]);
			}
		});
		
		int survived = 0;
		int notSurvived = 0;
		
		for(int i = 0; i < k; i++) {		
			if(array[i][1] == 1) {
				survived++;
			}else {
				notSurvived++;
			}	
			
			if(survived < notSurvived) {
				category.add(true);
			} 		
			category.add(false);
		}	

		System.out.println(category.size() + "  " + testSet.size());

		
		return "";
	}

	Double getAccuracy(ArrayList<DataPoint> data) {		
		double truePositive = 0; 
		double trueNegative = 0;
		double falsePositive = 0;
		double falseNegative = 0;
		
		test(data);
		for(int i = 0; i < category.size(); i++) {
			if(testSet.get(i).getSurvived() == true) {
				if(testSet.get(i).getSurvived() == category.get(i)) {
					truePositive++;
				} else {
					falsePositive++;
				}
			}
			if(testSet.get(i).getSurvived() == false) {
				if(testSet.get(i).getSurvived() == category.get(i)) {
					trueNegative++;
				} else {
					falseNegative++;
				}
			}
		}

		return (truePositive + trueNegative) / (truePositive + trueNegative + falsePositive + falseNegative);
	}

	
	Double getPrecision(ArrayList<DataPoint> data) {
		double truePositive = 0; 
		double trueNegative = 0;
		double falsePositive = 0;
		double falseNegative = 0;
		
		test(data);
		for(int i = 0; i < category.size(); i++) {
			if(testSet.get(i).getSurvived() == true) {
				if(testSet.get(i).getSurvived() == category.get(i)) {
					truePositive++;
				} else {
					falsePositive++;
				}
			}
			if(testSet.get(i).getSurvived() == false) {
				if(testSet.get(i).getSurvived() == category.get(i)) {
					trueNegative++;
				} else {
					falseNegative++;
				}
			}
		}
		
		return (truePositive)/(truePositive + falseNegative);
	}
	
	
	
}
