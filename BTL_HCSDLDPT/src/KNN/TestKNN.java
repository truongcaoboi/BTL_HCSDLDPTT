package KNN;
import java.awt.image.BufferedImage;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

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

public class TestKNN {
	private int [] data = {1,20,34,41,52,66,7,84,9,100};
	private int [] center = {1,20,50};
	
	private int [] cluster0=new int[10];
	private int [] cluster1 = new int[10];
	private int [] cluster2 = new int[10];
	
	private int [] vector1 = new int[10];
	private int [] vector2 = new int[10];
	private int [] vector3 = new int[10];
	
	private int computeZero(int [] cluster) {
		int count =0;
		for(int i = 0;i<10;i++) {
			if(cluster[i]!=0) {
				count ++;
			}else {
				break;
			}
		}
		return count;
	}
	
	private void computeDistance(int center,int[] vector) {
		for(int i=0;i<10;i++) {
			vector[i] = Math.abs(data[i] - center);
		}
	}
	
	private void computeCluster() {
		int l=0,m=0,n=0;
		for(int i=0;i<10;i++) {
			if(Math.min(Math.min(vector1[i],vector2[i]), vector3[i])==vector1[i]) {
				cluster0[l]=data[i];
				l++;
			}else if(Math.min(Math.min(vector1[i],vector2[i]), vector3[i])==vector2[i]) {
				cluster1[m] = data[i];
				m++;
			}else {
				cluster2[n]= data[i];
				n++;
			}
		}
		
		if(l<10) {
			for(int i=l;i<10;i++) {
				cluster0[l]=0;
			}
		}
		
		if(m<10) {
			for(int i=m;i<10;i++) {
				cluster1[m]=0;
			}
		}
		
		if(n<10) {
			for(int i=l;i<10;i++) {
				cluster2[n]=0;
			}
		}
	}
	
	private void computeCenter() {
		int center = 0;
		int count = this.computeZero(cluster0);
		if(count!=0) {
			for(int i=0;i<count;i++) {
				center += cluster0[i];
			}
			this.center[0] = center/count;
		}
		count = this.computeZero(cluster1);
		center = 0;
		if(count!=0) {
			for(int i=0;i<10;i++) {
				center += cluster1[i];
			}
			this.center[1] = center/count;
		}
		count = this.computeZero(cluster2);
		center = 0;
		if(count!=0) {
			for(int i=0;i<10;i++) {
				center += cluster2[i];
			}
			this.center[2] = center/count;
		}
	}
	
	private void kmean() {
		for(int i =0;i<10;i++) {
			this.computeDistance(center[0], vector1);
			this.computeDistance(center[1], vector2);
			this.computeDistance(center[2], vector3);
			
			this.computeCluster();
			
			this.computeCenter();
		}
	}
	
	private void exeKnnInt() {
		TestKNN t = new TestKNN();
		System.out.println("processing ...");
		t.kmean();
		System.out.print("Cluster 1: "+t.center[0]+" - {");
		for(int i=0;i<10;i++) {
			System.out.print(t.cluster0[i]+",");
		}
		System.out.println("}\n");
		System.out.print("Cluster 2: "+t.center[1]+" - {");
		for(int i=0;i<10;i++) {
			System.out.print(t.cluster1[i]+",");
		}
		System.out.println("}\n");
		System.out.print("Cluster 3: "+t.center[2]+" - {");
		for(int i=0;i<10;i++) {
			System.out.print(t.cluster2[i]+",");
		}
		System.out.println("}\n");
	}
	
	public static void main(String args  []) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String fileName = "10.jpg";
		Mat matImage = Highgui.imread(fileName);
		FeatureDetector fd = FeatureDetector.create(FeatureDetector.SIFT);
		DescriptorExtractor des = DescriptorExtractor.create(DescriptorExtractor.SIFT);
		MatOfKeyPoint keypoints = new MatOfKeyPoint();
		fd.detect(matImage, keypoints);
		KeyPoint [] ks = keypoints.toArray();
		MatOfKeyPoint descriptors = new MatOfKeyPoint();
		des.compute(matImage, keypoints, descriptors);
		
		KeyPoint kp[] = descriptors.toArray();
		//System.out.println(descriptors.toString());
		Mat vectors = new Mat();
		
		
		descriptors.convertTo(vectors, CvType.CV_64FC3);
		long size = vectors.channels() * vectors.total();
		double [] data = new double[(int)size];
		double [] data1 = new double[128];
		vectors.get(1, 0, data);
		for(int i = 0 ;i<vectors.rows();i++) {
			for(int j =0 ;j<vectors.cols();j++) {
				System.out.print(data[i*vectors.cols()+j]+";");
			}
			System.out.println();
			break;
		}
		System.out.println("------------");
		vectors.row(32).get(0, 0, data1);
		for(int i = 0;i<128;i++) {
			System.out.print(data1[i]+",");
		}
	}
}
