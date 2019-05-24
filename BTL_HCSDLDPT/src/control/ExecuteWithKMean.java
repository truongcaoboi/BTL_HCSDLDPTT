package control;

import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.io.*;
import org.json.JSONObject;

import KNN.*;
import model.Data;
import model.Image;
import model.Result;

public class ExecuteWithKMean {
	private TestLibKmean kmean = new TestLibKmean();
	private Vector<Data> tfs = new Vector<Data>();
	public Vector<Result> results = new Vector<Result>();
	public ExecuteWithKMean() {
		//load dictionary and tfs
		try {
			kmean.loadDictionary();
			BufferedReader reader = new BufferedReader(new FileReader("mainTF.main"));
			String line = reader.readLine();
			while(line != null) {
				JSONObject object = new JSONObject(line);
				Data d = new Data();
				d.setPath(object.getString("pathFile"));
				String temp = object.getJSONArray("tf").toString();
				temp = temp.substring(0, temp.length() -1).substring(1);
				String [] tf = temp.split(",");
				d.setTf(tf);
				tfs.add(d);
				line = reader.readLine();
			}
			reader.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void ExcuteQuery(String path) {
		results = new Vector<Result>();
		//dung tf
		Vector<Integer> tf = kmean.createTF(path);
		//Quet tf
		for(int i = 0;i<tfs.size();i++) {
			double cosi = kmean.distanceCosi2(tfs.get(i).getTf(), tf, tf.size());
//			System.out.println(cosi);
			if(cosi >= 0.7) {
				Result r = new Result();
				r.setCosi(cosi);
				r.setPath(tfs.get(i).getPath());
				results.add(r);
			}
		}
		//sort
		sortResult();
	}
	
	public static void main(String [] args) {
		
	}

	public int showResultQuery(JLabel imgResult1,JLabel imgResult2,JLabel imgResult3,JLabel imgResult4,int stt,int type) {
		int size = results.size();
		if (results.size() == 0) {
			JOptionPane.showMessageDialog(null, "Không có kết quả");
			//System.out.println("khong co ket qua");
		} else {
			if(type == 1) {
				if(stt == size) {
					int du = size%4;
					stt =stt - du -4;
				}else {
					stt -= 8;
				}
			}
			//String path = results.get(stt).getPath();
			String name = "";
			imgResult1.setIcon(null);
			imgResult1.setText("");
			if(stt < size) {
				name = results.get(stt).getPath();
				imgResult1.setIcon(new ImageIcon("Database\\"+name));
				imgResult1.setText("cos: "+results.get(stt).getCosi());
				imgResult1.setToolTipText("cos: "+results.get(stt).getCosi());
				stt++;
			}
			imgResult2.setIcon(null);
			imgResult2.setText("");
			if(stt < size) {
				name = results.get(stt).getPath();
				imgResult2.setIcon(new ImageIcon("Database\\"+name));
				imgResult2.setText("cos: "+results.get(stt).getCosi());
				imgResult2.setToolTipText("cos: "+results.get(stt).getCosi());
				stt++;
			}
			imgResult3.setIcon(null);
			imgResult3.setText("");
			if(stt < size) {
				name = results.get(stt).getPath();
				imgResult3.setIcon(new ImageIcon("Database\\"+name));
				imgResult3.setText("cos: "+results.get(stt).getCosi());
				imgResult3.setToolTipText("cos: "+results.get(stt).getCosi());
				stt++;
			}
			imgResult4.setIcon(null);
			imgResult4.setText("");
			if(stt < size) {
				name = results.get(stt).getPath();
				imgResult4.setIcon(new ImageIcon("Database\\"+name));
				imgResult4.setText("cos: "+results.get(stt).getCosi());
				imgResult4.setToolTipText("cos: "+results.get(stt).getCosi());
				stt++;
			}
		}
		return stt;
	}
	
	public void sortResult() {
		Vector<Result> temp = new Vector<Result>();
		double cos = 0;
		Result t = null;
		while(results.size() != 0) {
			for(Result r : results) {
				if(r.getCosi()>cos) {
					cos = r.getCosi();
					t = r;
				}
			}
			Result add = new Result();
			add.setCosi(t.getCosi());
			add.setPath(t.getPath());
			temp.add(add);
			cos= 0;
			results.removeElement(t);
		}
		results = temp;
	}
}
