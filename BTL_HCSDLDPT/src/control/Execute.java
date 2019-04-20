package control;

import java.awt.image.BufferedImage;
import org.opencv.imgproc.Imgproc;
import org.opencv.highgui.Highgui;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

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
import java.util.Vector;
import model.Image;

public class Execute {
	public Vector<Image> results = null;

	public void showImage(String path) {

	}

	public void detectAndSave(String path) {

	}

	public Mat detectAndCompute(String path) {
		// Đọc file ảnh cần xử lý
		Mat object = Highgui.imread(path);

		// Khởi tạo ma trận tìm kiếm điểm đặc trưng
		MatOfKeyPoint matKeyPoint = new MatOfKeyPoint();

		// Thiết lập máy chạy thuật toán
		FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);

		// Dò tìm các điểm đặc trưng
		featureDetector.detect(object, matKeyPoint);

		// Mảng keypoints
		KeyPoint keypoints[] = matKeyPoint.toArray();

		// Tính toán các description
		MatOfKeyPoint objectDescription = new MatOfKeyPoint();

		// Tạo máy để tính toán
		DescriptorExtractor description = DescriptorExtractor.create(DescriptorExtractor.SURF);

		// Tính toán
		description.compute(object, matKeyPoint, objectDescription);

		// Luu anh keypoint
		// Mat outputImage = new Mat(object.rows(), object.cols(),
		// Highgui.CV_LOAD_IMAGE_COLOR);
		//
		// Scalar newKeypointColor = new Scalar(255, 0, 0);
		//
		//
		//
		// System.out.println("Drawing key points on object image...");
		//
		// Features2d.drawKeypoints(object, matKeyPoint, outputImage, newKeypointColor,
		// 0);
		//
		// Highgui.imwrite("Keypoints\\"+new File(path).getName(), outputImage);
		return objectDescription;
	}

	public void compare(Mat objectDescription, Mat dataDescription, String path,String pathObject) {
		List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();

		DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.BRUTEFORCE_L1);

		// System.out.println("Matching object and scene images...");

		descriptorMatcher.knnMatch(objectDescription, dataDescription, matches, 2);

		// System.out.println("Calculating good match list...");

		LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

		float nndrRatio = 0.7f;

		for (int i = 0; i < matches.size(); i++) {

			MatOfDMatch matofDMatch = matches.get(i);

			DMatch[] dmatcharray = matofDMatch.toArray();

			DMatch m1 = dmatcharray[0];

			DMatch m2 = dmatcharray[1];

			if (m1.distance <= m2.distance * nndrRatio) {

				goodMatchesList.addLast(m1);

			}

		}
		//(int) Math.sqrt(matches.size()) / 2
		if (goodMatchesList.size() >= 50) {
			Image im = new Image();
			im.setPath(path);
			im.setMatches(goodMatchesList.size());
			results.add(im);
			// System.err.println("wow. Congratulate! It compare with "+path+" and
			// "+img.getMatches());

			// Luu anh matching
			
			
			Scalar matchestColor = new Scalar(0, 255, 0);
			
			Scalar newKeypointColor = new Scalar(255, 0, 0);
			
			Mat objectImage = Highgui.imread(pathObject);
			
			Mat sceneImage = Highgui.imread(path);
			
			FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
			MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
			MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
			
			featureDetector.detect(objectImage, objectKeyPoints);
			featureDetector.detect(sceneImage, sceneKeyPoints);
			
			Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);

			List<KeyPoint> objKeypointlist = objectKeyPoints.toList();

			List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

			LinkedList<Point> objectPoints = new LinkedList<>();

			LinkedList<Point> scenePoints = new LinkedList<>();

			for (int i = 0; i < goodMatchesList.size(); i++) {

				objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);

				scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);

			}

			MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();

			objMatOfPoint2f.fromList(objectPoints);

			MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();

			scnMatOfPoint2f.fromList(scenePoints);

			Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

			Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);

			Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

			obj_corners.put(0, 0, new double[] { 0, 0 });

			obj_corners.put(1, 0, new double[] { objectImage.cols(), 0 });

			obj_corners.put(2, 0, new double[] { objectImage.cols(), objectImage.rows() });

			obj_corners.put(3, 0, new double[] { 0, objectImage.rows() });

			System.out.println("Transforming object corners to scene corners...");

			Core.perspectiveTransform(obj_corners, scene_corners, homography);

			Mat img = Highgui.imread(path, Highgui.CV_LOAD_IMAGE_COLOR);

			Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)),
					new Scalar(0, 255, 0), 4);

			Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)),
					new Scalar(0, 255, 0), 4);

			Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)),
					new Scalar(0, 255, 0), 4);

			Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)),
					new Scalar(0, 255, 0), 4);

			System.out.println("Drawing matches image...");

			MatOfDMatch goodMatches = new MatOfDMatch();

			goodMatches.fromList(goodMatchesList);

			Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput,
					matchestColor, newKeypointColor, new MatOfByte(), 2);

			Highgui.imwrite("Matches\\match_"+new File(path).getName(), matchoutput);

			Highgui.imwrite("Matches\\cover_"+new File(path).getName(), img);
		} else {
			// System.out.println("oh no. It don't compare -"+ path);
		}
	}

	public int showResultQuery(JLabel imgResult,JLabel imgCover,JLabel imgMatch,Execute ex,int stt) {
		Vector<Image> results = ex.results;
		if (results.size() == 0) {
			JOptionPane.showMessageDialog(null, "Không có kết quả");
			System.out.println("khong co ket qua");
		} else {
			String path = results.get(stt).getPath();
			String name = new File(path).getName();
			imgResult.setIcon(new ImageIcon("Database\\"+name));
			imgCover.setIcon(new ImageIcon("Matches\\cover_"+name));
			imgMatch.setIcon(new ImageIcon("Matches\\match_"+name));
		}
		if(stt == results.size()-1) {
			return -1;
		}else {
			return stt;
		}
	}

	public void executeQuery(String pathImageQuery) {
		File lib = null;
		results = new Vector<Image>();
		
		File dirMatch = new File("Matches");
		for(File f : dirMatch.listFiles()) {
			f.delete();
		}
		
		// Phần này chưa hiểu là để làm gì
		String os = System.getProperty("os.name");

		String bitness = System.getProperty("sun.arch.data.model");

		if (os.toUpperCase().contains("WINDOWS")) {

			if (bitness.endsWith("64")) {

				lib = new File("libs//x64//" + System.mapLibraryName("opencv_java2411"));

			} else {

				lib = new File("libs//x86//" + System.mapLibraryName("opencv_java2411"));

			}

		}

		// System.out.println(lib.getAbsolutePath());

		Mat objectDescription = this.detectAndCompute(pathImageQuery);

		File dirData = new File("Database");

		File files[] = dirData.listFiles();

		for (File f : files) {
			String path = f.getPath();
			Mat dataDescription = this.detectAndCompute(path);
			this.compare(objectDescription, dataDescription, path,pathImageQuery);
			// System.out.println("end compare with: "+path);
		}
		
		this.sortResult();
	}

	public BufferedImage showImageQuery(String path) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat mat = Highgui.imread(path);

		// Imgcodecs.imwrite("result.jpg", mat);

		MatOfByte mob = new MatOfByte();
		byte ba[];
		try {
			Highgui.imencode(".jpg", mat, mob);
			ba = mob.toArray();
			BufferedImage image = ImageIO.read(new ByteArrayInputStream(ba));
			System.out.println(image);
			return image;
		} catch (IOException e) {
			System.out.println("loi r ban oi");
			e.printStackTrace();
		}
		return null;
	}

	private void sortResult() {
		Vector<Image> sort = new Vector<Image>();
		int matchs = 0;
		Image node = null;
		while(results.size()!=0) {
			for(Image img : results) {
				if(img.getMatches()>matchs) {
					matchs = img.getMatches();
					node = img;
				}
			}
			sort.addElement(node);
			results.remove(node);
			matchs = 0;
		}
		results = sort;
	}
}
