package KNN;

import java.awt.image.BufferedImage;
import java.io.*;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import org.json.JSONObject;

import javax.imageio.ImageIO;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.features2d.*;

public class Kmean {
	BufferedWriter output = null;
	BufferedReader input = null;
	
	public Kmean() {
		try {
			input = new BufferedReader(new FileReader("dictionary.dic"));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private Vector<String[]> dictionary = null;
	private Vector<Vector<Double>> centers = new Vector<Vector<Double>>();

	private Vector<Vector<KeyPoint>> clusters = new Vector<Vector<KeyPoint>>();
	private Vector<Vector<Double>> distances = new Vector<Vector<Double>>();

	private Vector<Vector<KeyPoint>> clustersKP = new Vector<Vector<KeyPoint>>();
	private Vector<Vector<Double>> arraysVector = new Vector<Vector<Double>>();
	private Vector<Vector<Vector<Double>>> clusterVectors = new Vector<Vector<Vector<Double>>>();

	private double computeDistance(Vector<Double> p1, Vector<Double> p2) {
		// System.out.println("computeDistance");
		double result = 0;
		for (int i = 0; i < p1.size(); i++) {
			result += Math.pow((p1.get(i) - p2.get(i)), 2);
		}
		return Math.sqrt(result);
	}

	private int minDistance(double[] dis) {
		double min = Double.MAX_VALUE;
		int index = 0;
		for (int i = 0; i < dis.length; i++) {
			if (min > dis[i]) {
				min = dis[i];
				index = i;
			}
		}
		return index;
	}

	private void computeCluster(Vector<Vector<Double>> arrVts, KeyPoint[] kps, int k) {
		// System.err.println("computeCluster");
		int index = 0;
		double dis[] = new double[k];
		// reset tiep tuc vong lap
		for (int i = 0; i < k; i++) {
			distances.get(i).removeAllElements();
			clustersKP.get(i).removeAllElements();
			clusterVectors.get(i).removeAllElements();
		}
		// Tinh khoang cach
		for (Vector<Double> cen : centers) {
			for (Vector<Double> kp : arraysVector) {
				distances.get(index).add(new Double(computeDistance(cen, kp)));
			}
			index++;
		}

		// Tinh toan cluster
		index = 0;
		for (int i = 0; i < kps.length; i++) {
			// tao mang de so sanh khoang cach
			for (int j = 0; j < k; j++) {
				dis[j] = distances.get(j).get(i);
			}
			int indexCluster = minDistance(dis);
			clustersKP.get(indexCluster).add(kps[i]);
			clusterVectors.get(indexCluster).add(arraysVector.get(i));
		}
		// Tinh toan lai tam cum
		// for (int i = 0; i < k; i++) {
		// Vector<KeyPoint> cluster = clusters.get(i);
		// if (cluster.size() != 0) {
		// double x = 0;
		// double y = 0;
		// for (int j = 0; j < cluster.size(); j++) {
		// x += cluster.get(j).pt.x;
		// y += cluster.get(j).pt.y;
		// }
		// KeyPoint c = new KeyPoint();
		// c.pt.x = x / cluster.size();
		// c.pt.y = y / cluster.size();
		// centers.set(i, c);
		// }
		// }

		// Tinh lai tam cum
//		for (int i = 0; i < k; i++) {
//			Vector<Vector<Double>> cluster = clusterVectors.get(i);
//			if (cluster.size() != 0) {
//				Vector<Double> t = null;
//				Vector<Double> c = null;
//				double totalDis = Double.MAX_VALUE;
//				double dist = 0;
//				for (int j = 0; j < cluster.size(); j++) {
//					c = cluster.get(j);
//					for (int m = 0; m < cluster.size(); m++) {
//						dist += computeDistance(c, cluster.get(m));
//					}
//					if (totalDis > dist) {
//						totalDis = dist;
//						t = c;
//					}
//				}
//				centers.set(i, t);
//			}
//		}
		for (int i = 0; i < k; i++) {
			Vector<Vector<Double>> cluster = clusterVectors.get(i);
			if (cluster.size() != 0) {
				Vector<Double> t = null;
				Vector<Double> c = null;
				double totalDis = Double.MAX_VALUE;
				double dist = 0;
				Vector<Double> cen = new Vector<Double>();
				double temp = 0;
				for(int j = 0;j<128;j++) {
					temp  = 0;
					for(int l = 0;l<cluster.size();l++) {
						temp += cluster.get(l).get(j);
					}
					cen.add(temp/cluster.size());
				}
//				for (int j = 0; j < cluster.size(); j++) {
//					c = cluster.get(j);
//					for (int m = 0; m < cluster.size(); m++) {
//						dist += computeDistance(c, cluster.get(m));
//					}
//					if (totalDis > dist) {
//						totalDis = dist;
//						t = c;
//					}
//				}
				for(int j = 0;j<cluster.size();j++) {
					c =cluster.get(j);
					dist= computeDistance(c, cen);
					if(totalDis > dist) {
						t = c;
						totalDis = dist;
					}
				}
				centers.set(i, t);
			}
		}
	}

	private void showResult(Mat objectImage) {
		// System.out.println("show result");
		// Scalar newKeypointColor = new Scalar(255, 0, 0);
		//
		// for (int i = 0; i < clustersKP.size(); i++) {
		// System.out.println("Drawing key points on object image...");
		// MatOfKeyPoint mkp = new MatOfKeyPoint();
		// mkp.fromList(clustersKP.get(i));
		// Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(),
		// Highgui.CV_LOAD_IMAGE_COLOR);
		// Features2d.drawKeypoints(objectImage, mkp, outputImage, newKeypointColor, 0);
		// Highgui.imwrite("outputImage" + i + ".jpg", outputImage);
		// System.out.println(centers.get(i).toString());
		// }

		// in tâm cụm

		try {
			String text = "";
			for (int i = 0; i < centers.size(); i++) {
				text = centers.get(i).toString();
				output.write(text);
				output.newLine();
				output.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private Vector<Integer> createTF(String pathImage) {
		//System.out.println("createTf");
		if(dictionary == null) {
			loadDictionary();
		}
		int size = dictionary.size();
		Vector<Integer> tf = new Vector<Integer>();
		double cosi = 0;
		try {
			for(int i = 0;i<size;i++) {
				String itemDic []= dictionary.get(i);
				int count = 1;
				for(int j = 0; j<centers.size();j++) {
					cosi = computeDistanceCosi(centers.get(j), itemDic, itemDic.length);
					System.out.println(cosi);
					if(cosi >= 0.5) {
						count ++;
					}
				}
				tf.add(count);
			}
			System.out.println("qua ham: "+tf.size());
			return tf;
//			String text = "{\"pathFile\":\""+pathImage+"\",\"tf\":"+tf.toString()+"}";
//			output.write(text);
//			output.newLine();
//			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
		return null;
	}
	
	public void loadTf() {
		try {
			String line = input.readLine();
			JSONObject object = new JSONObject(line);
			System.out.println("file name: " + object.getString("pathFile"));
			System.out.println("array: "+object.getJSONArray("tf").toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private double computeDistanceCosi(Vector<Double> vec, String[] ar, int size) {
		double cosi = 0;
		double mul = 0;
		double dis1 = 0;
		double dis2 = 0;
		try {
			for (int i = 0; i < size; i++) {
				dis1 += Math.pow(vec.get(i), 2);
				dis2 += Math.pow(Double.parseDouble(ar[i].trim()), 2);
				mul += vec.get(i) * Double.parseDouble(ar[i].trim());
			}
			cosi = mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return Math.round(cosi*100)/100;
	}
	
	public double computeDistanceCosi2(Vector<Integer> vec, String[] ar, int size) {
		double cosi = 0;
		double mul = 0;
		double dis1 = 0;
		double dis2 = 0;
		try {
			for (int i = 0; i < size; i++) {
				dis1 += Math.pow(vec.get(i), 2);
				dis2 += Math.pow(Double.parseDouble(ar[i].trim()), 2);
				mul += vec.get(i) * Double.parseDouble(ar[i].trim());
			}
			cosi = mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return Math.round(cosi*100)/100;
	}
	
	private double computeDistanceCosi(Vector<Double> vec, Vector<Double> ar, int size) {
		double cosi = 0;
		double mul = 0;
		double dis1 = 0;
		double dis2 = 0;
		try {
			for (int i = 0; i < size; i++) {
				dis1 += Math.pow(vec.get(i), 2);
				dis2 += Math.pow(ar.get(i), 2);
				mul += vec.get(i) * ar.get(i);
			}
			cosi = mul / (Math.sqrt(dis1) * Math.sqrt(dis2));
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return Math.round(cosi*100)/100;
	}

	private void loadDictionary() {
		dictionary = new Vector<String []>();
		try {
			String line = input.readLine();
			while(line != null) {
				line = line.substring(0, line.length() - 1).substring(1);
				dictionary.add(line.split(","));
				line = input.readLine();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public Vector<Integer> executeKmean(String pathImage, int k) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		// load anh
		// String bookObject = "E:/laptrinhjava/luyentap java/DemoOpenCV/10.jpg";
		Mat objectImage = Highgui.imread(pathImage, Highgui.CV_LOAD_IMAGE_COLOR);
		// Detect anh
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SIFT);
		MatOfKeyPoint mkp = new MatOfKeyPoint();
		featureDetector.detect(objectImage, mkp);
		// compute anh
		DescriptorExtractor des = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		MatOfKeyPoint descriptors = new MatOfKeyPoint();
		des.compute(objectImage, mkp, descriptors);
		// Lay ra cac vector
		arraysVector.removeAllElements();
		double data[] = new double[(int) (descriptors.channels() * descriptors.total())];
		Mat array = new Mat();
		descriptors.convertTo(array, CvType.CV_64FC3);
		array.get(0, 0, data);
		for (int i = 0; i < array.rows(); i++) {
			Vector<Double> vt = new Vector<Double>();
			for (int j = 0; j < array.cols(); j++) {
				vt.add(data[i * array.cols() + j]);
			}
			arraysVector.add(vt);
		}
		// khoi tao
		centers.removeAllElements();
		distances.removeAllElements();
		clustersKP.removeAllElements();
		clusterVectors.removeAllElements();

		int ar = (arraysVector.size() - 1) / k;
		for (int i = 0; i < k; i++) {
			Vector<Double> d = new Vector<Double>();
			Vector<KeyPoint> kp = new Vector<KeyPoint>();
			Vector<Vector<Double>> arVt = new Vector<Vector<Double>>();
			clusterVectors.add(arVt);
			distances.add(d);
			clustersKP.add(kp);
			centers.add(arraysVector.get(i * ar));
		}
		// kmean
		for (int i = 0; i < 10; i++) {
			computeCluster(arraysVector, mkp.toArray(), k);
		}
		// show result
		System.out.println(pathImage);
		// showResult(objectImage);
		return createTF(new File(pathImage).getName());
	}

	public static void main(String args[]) {
//		Kmean k = new Kmean();
//		File dic = new File("Database");
//		try {
//			k.input = new BufferedReader(new FileReader("dictionary.dic"));
//			k.output = new BufferedWriter(new FileWriter("list_tf.tf"));
//			//k.loadTf();
//			for (File f : dic.listFiles()) {
//				k.executeKmean(f.getPath(), 50);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			try {
//				//k.output.close();
//				k.input.close();
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
//		}

		// try {
		// k.input = new BufferedReader(new FileReader("dictionary.dic"));
		// String text = k.input.readLine();
		// System.out.println(text);
		// String t = text;
		// text = text.substring(0, text.length() - 1).substring(1);
		// System.out.println(text);
		// String ts[] = text.split(",");
		// } catch (Exception e) {
		// // TODO: handle exception
		// }
	}
}
