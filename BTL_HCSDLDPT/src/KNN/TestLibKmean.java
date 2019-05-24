package KNN;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.TermCriteria;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.highgui.Highgui;

import net.sf.javaml.clustering.KMeans;

public class TestLibKmean {
	public BufferedWriter writer = null;
	public BufferedWriter writerTF = null;
	public Vector<Integer> tf = new Vector<Integer>();
	public Vector<String> dictionary = new Vector<String>();
	public static void main(String args[]) {
//		System.out.println("Start");
//		TestLibKmean kmean = new TestLibKmean();
//		try {
//			//kmean.writer = new BufferedWriter(new FileWriter("mainDictionary.main"));
//			kmean.writerTF = new BufferedWriter(new FileWriter("mainTF.main"));
//			//System.out.println(kmean.createDictionary());
//			kmean.loadDictionary();
//			File dir = new File("Database");
//			for(File file : dir.listFiles()) {
//				kmean.createTF(file.getPath());
//			}
//			//kmean.writer.close();
//			kmean.writerTF.close();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
////		String [] w1 = {"2", "2","3"};
////		double [] w2 = {1.0,2.0,3.0};
////		double cos =kmean.distanceCosi(w1,w2,3);
////		System.out.println("cos: "+cos);
//		System.out.println("end");
	}
	
	public String createDictionary() {
		System.out.println("start");
		File dir = new File("Database");
		for(File file : dir.listFiles()) {
			Mat centers = executeKMean(file.getPath());
			try {
				centers.convertTo(centers, CvType.CV_64FC3);
				for(int i=0;i<centers.rows();i++) {
					double [] d= new double [128];
					centers.get(i, 0,d);
					String text = "";
					for(int j=0;j<128;j++) {
						text += d[j]+",";
					}
					writer.write(text);
					writer.newLine();
					writer.flush();
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return "end";
	}
	
	public Mat executeKMean(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		MatOfKeyPoint detected = new MatOfKeyPoint();
		MatOfKeyPoint computed = new MatOfKeyPoint();
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
		DescriptorExtractor de = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		Mat m = Highgui.imread(path,Highgui.CV_LOAD_IMAGE_COLOR);
		fd.detect(m, detected);
		de.compute(m, detected, computed);
		Mat clusterLabels = new Mat();
		Mat centers = new Mat();
		Mat allDescriptors = new Mat();
		computed.convertTo(allDescriptors, CvType.CV_32F);
		TermCriteria criteria = new TermCriteria(TermCriteria.EPS + TermCriteria.MAX_ITER, 100, 0.1);
		int size = allDescriptors.rows();
		int k = 400;
		if(size > 100) {
			k = 100;
		}else {
			k = size;
		}
		try {
			Core.kmeans(allDescriptors, k, clusterLabels, criteria, 10, Core.KMEANS_PP_CENTERS, centers);
		}catch(Exception e) {
			System.out.println(size);
		}
		return centers;
	}
	public double distanceCosi(String [] word1,double [] word2,int size) {
		double cosi = 0;
		double mul = 0;
		double dis1 = 0;
		double dis2 = 0;
		try {
			for (int i = 0; i < size; i++) {
				dis1 += Math.pow(word2[i], 2);
				dis2 += Math.pow(Double.parseDouble(word1[i].trim()), 2);
				mul += word2[i] * Double.parseDouble(word1[i].trim());
			}
			cosi = mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return Math.round(cosi*100)*1.0/100;
	}
	
	
	public double distanceCosi2(String [] word1,Vector<Integer> word2,int size) {
		double cosi = 0;
		double mul = 0;
		double dis1 = 0;
		double dis2 = 0;
		try {
			for (int i = 0; i < size; i++) {
				dis1 += Math.pow(word2.get(i), 2);
				dis2 += Math.pow(Double.parseDouble(word1[i].trim()), 2);
				mul += word2.get(i) * Double.parseDouble(word1[i].trim());
			}
			cosi = mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return Math.round(cosi*100)*1.0/100;
	}
	
	public Vector<Integer> createTF(String path){
		//System.out.println(path);
		tf.removeAllElements();
		Mat words = executeKMean(path);
		words.convertTo(words, CvType.CV_64FC3);
		int row = words.rows();
		int size = dictionary.size();
		for(int i=0;i<size;i++) {
			String word = dictionary.get(i);
			int count = 0;
			for(int j = 0;j<row;j++) {
				double [] w = new double[128];
				words.row(j).get(0, 0,w);
				double cos = distanceCosi(word.split(","), w,128);
				if(cos >= 0.85) {
					count++;
				}
			}
			tf.add(count);
			//System.out.println(i);
		}
		System.out.println(tf.size());
//		try {
//			writerTF.write(tf.toString());
//			writerTF.newLine();
//			writerTF.flush();
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
		return tf;
	}
	
	public void loadDictionary() {
		//System.out.println("start load");
		dictionary = new Vector<String>();
		BufferedReader input = null;
		int count = 0;
		try {
			input = new BufferedReader(new FileReader("mainDictionary.main"));
			String line = input.readLine();
			while(line != null) {
				line = line.substring(0, line.length() - 1);
				dictionary.add(line);
				line = input.readLine();
			}
			//System.out.println(dictionary.size());
			//System.out.println("end load");
		} catch (Exception e) {
			System.out.println(count);
		}
	}
}
