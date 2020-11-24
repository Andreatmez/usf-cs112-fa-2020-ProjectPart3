package ProjectPart2;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Stroke;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Graph extends JPanel {
	
	private ArrayList<DataPoint> testSet;
	private ArrayList<Boolean> category;

    private static final long serialVersionUID = 1L;
    private int labelPadding = 40;
    private Color lineColor = new Color(255, 255, 254);

    private Color pointColor = new Color(255, 0, 255);
    private Color truePositiveColor = new Color(0, 0, 255);
    private Color trueNegativeColor = new Color(0, 255, 255);
    private Color falsePositiveColor = new Color(255, 255, 0);
    private Color falseNegativeColor = new Color(255, 0, 0);

    
    private Color gridColor = new Color(200, 200, 200, 200);
    private static final Stroke GRAPH_STROKE = new BasicStroke(2f);

    private static int pointWidth = 10;

    private int numXGridLines = 6;
    private int numYGridLines = 6;
    private int padding = 40;

    private List<DataPoint> data;
    
    private KNNModel knnmodel;

    public Graph(List<DataPoint> testData, List<DataPoint> trainData) {
        this.data = testData;
		ArrayList<DataPoint> train = new ArrayList<DataPoint>(); // CREATES TRAIN PASSENGER

        knnmodel = new KNNModel(3);
        knnmodel.train(train);
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        double minF1 = getMinF1Data();
        double maxF1 = getMaxF1Data();
        double minF2 = getMinF2Data();
        double maxF2 = getMaxF2Data();

        g2.setColor(Color.WHITE);
        g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - 
        		labelPadding, getHeight() - 2 * padding - labelPadding);
        g2.setColor(Color.BLUE);

        double yGridRatio = (maxF2 - minF2) / numYGridLines;
        for (int i = 0; i < numYGridLines + 1; i++) {
            int x0 = padding + labelPadding;
            int x1 = pointWidth + padding + labelPadding;
            int y0 = getHeight() - ((i * (getHeight() - padding * 2 -
            		labelPadding)) / numYGridLines + padding + labelPadding);
            int y1 = y0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y1);
                g2.setColor(Color.BLACK);
                String yLabel = String.format("%.2f", (minF2 + (i * yGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(yLabel);
                g2.drawString(yLabel, x0 - labelWidth - 6, y0 + (metrics.getHeight() / 2) - 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        double xGridRatio = (maxF1 - minF1) / numXGridLines;
        for (int i = 0; i < numXGridLines + 1; i++) {
            int y0 = getHeight() - padding - labelPadding;
            int y1 = y0 - pointWidth;
            int x0 = i * (getWidth() - padding * 2 - labelPadding) / (numXGridLines) + padding + labelPadding;
            int x1 = x0;
            if (data.size() > 0) {
                g2.setColor(gridColor);
                g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x1, padding);
                g2.setColor(Color.BLACK);
                String xLabel = String.format("%.2f", (minF1 + (i * xGridRatio)));
                FontMetrics metrics = g2.getFontMetrics();
                int labelWidth = metrics.stringWidth(xLabel);
                g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
            }
            g2.drawLine(x0, y0, x1, y1);
        }

        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, padding + labelPadding, padding);
        g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding, getWidth() -
        		padding, getHeight() - padding - labelPadding);

        paintPoints(g2, minF1, maxF1, minF2, maxF2);
    }

    private void paintPoints(Graphics2D g2, double minF1, double maxF1, double minF2, double maxF2) {
        Stroke oldStroke = g2.getStroke();
        g2.setColor(lineColor);
        g2.setStroke(GRAPH_STROKE);
        double xScale = ((double) getWidth() - (3 * padding) - labelPadding) /(maxF1 - minF1);
        double yScale = ((double) getHeight() - 2 * padding - labelPadding) / (maxF2 - minF2);
        g2.setStroke(oldStroke);
        
		testSet = new ArrayList<DataPoint>();
		category = new ArrayList<Boolean>();
		testSet.addAll(data);
		
        for (int i = 0; i < data.size(); i++) {
            int x1 = (int) ((data.get(i).getF1() - minF1) * xScale + padding + labelPadding);
            int y1 = (int) ((maxF2 - data.get(i).getF2()) * yScale + padding);
            int x = x1 - pointWidth / 2;
            int y = y1 - pointWidth / 2;
            int ovalW = pointWidth;
            int ovalH = pointWidth;
            g2.setColor(pointColor);
            g2.fillOval(x, y, ovalW, ovalH);
        }
        
		for(int j = 0; j < category.size(); j++) {
			if(testSet.get(j).getSurvived() == true) {
				if(testSet.get(j).getSurvived() == category.get(j)) {
					g2.setColor(truePositiveColor);
					
				} else {
					g2.setColor(falsePositiveColor);
				}
			}
			if(testSet.get(j).getSurvived() == false) {
				if(testSet.get(j).getSurvived() == category.get(j)) {
					g2.setColor(trueNegativeColor);
				} else {
					g2.setColor(falseNegativeColor);
				}
			}
		}

    }

    private double getMinF1Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF1());
        }
        return minData;
    }

    private double getMinF2Data() {
        double minData = Double.MAX_VALUE;
        for (DataPoint pt : this.data) {
            minData = Math.min(minData, pt.getF2());
        }
        return minData;
    }

    private double getMaxF1Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF1());
        }
        return maxData;
    }

    private double getMaxF2Data() {
        double maxData = Double.MIN_VALUE;
        for (DataPoint pt : this.data) {
            maxData = Math.max(maxData, pt.getF2());
        }
        return maxData;
    }

    public void setData(List<DataPoint> data) {
        this.data = data;
        invalidate();
        this.repaint();
    }

    public List<DataPoint> getData() {
        return data;
    }

    private static void createAndShowGui(List<DataPoint> testData, List<DataPoint> trainData) {

        Graph mainPanel = new Graph(testData, trainData);

        mainPanel.setPreferredSize(new Dimension(700, 600));

        JFrame frame = new JFrame("CS 112 Lab Part 3");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
      
    public static void main(String[] args) {
      SwingUtilities.invokeLater(new Runnable() {
         public void run() {      	 
     		List<DataPoint> passengers = readPassengersFromCSV("titanic.csv");
    		ArrayList<DataPoint> train = new ArrayList<DataPoint>(); 
    		ArrayList<DataPoint> test = new ArrayList<DataPoint>(); 
    		Random random = new Random(); 
    		for(DataPoint p : passengers) {
    			double randomNumber = random.nextDouble(); 
    			if(randomNumber < .9) { 
    				train.add(p); 
    			} else { 
    				test.add(p); 
    			}
    		}
    		KNNModel knnmodel = new KNNModel(7);
    		
    		knnmodel.train(train);
    		knnmodel.test(test);
    		
    		double AccuracyData = knnmodel.getAccuracy(test); 
    		double PrecisionData = knnmodel.getPrecision(test);
    		
    		String Accuracy = String.format("%8s : %2.0f", "Accuracy % ", AccuracyData * 100); 
    		String Precision = String.format("%9s : %2.0f", "Precision % ", PrecisionData * 100);

    		JFrame MyFrame = new JFrame("Accuracy & Precision");  
    		MyFrame.setVisible(true); 

    		JPanel panel = new JPanel();  
    		MyFrame.add(panel);
    		MyFrame.setSize(200, 200);

    		JLabel label1 = new JLabel(Accuracy); 
    		panel.add(label1);   
    		JLabel label2 = new JLabel(Precision);
    		panel.add(label2); 

            createAndShowGui(passengers, null);
         }
      });
    }
    
	private static List<DataPoint> readPassengersFromCSV(String fileName){
		List<DataPoint> passengers = new ArrayList<>();
		Path pathToFile = Paths.get(fileName);
		try {
			BufferedReader br = new BufferedReader(new FileReader(fileName)); 
			String line = br.readLine();
			line = br.readLine();
			while(line != null) {
				String[] attributes = line.split(",");
				DataPoint passenger = createPassenger(attributes);
				passengers.add(passenger);
				line = br.readLine();
			}
		}catch(FileNotFoundException e) {
			System.out.println("File not found");
		}
		catch(IOException ioe) {
			System.out.println("Unable to read file");
			ioe.printStackTrace();
		}
		return passengers;
	}

	private static DataPoint createPassenger(String[] data) {
		boolean survived = data[1].equals("1");
		double age;
		if(data.length>5 && data[5].length()>0) {
			age = Double.parseDouble(data[5]);
		} else { age = 0.0; }
		double fare;
		if(data.length>6) {
			fare = Double.parseDouble(data[6]);
		} else { fare = 0.0; }
		return new DataPoint(survived, age, fare);
	}	

}