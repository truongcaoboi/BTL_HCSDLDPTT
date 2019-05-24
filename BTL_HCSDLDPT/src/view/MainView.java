package view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JFormattedTextField;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

import control.Execute;
import control.ExecuteWithKMean;

import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.beans.PropertyChangeEvent;

public class MainView extends JFrame {

	private JPanel contentPane;
	private JTextField txtContentSearch;
	private int stt = 0;
	private Execute ex = new Execute();
	ExecuteWithKMean exe = new ExecuteWithKMean();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainView frame = new MainView();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MainView() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1389, 970);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Hệ thống truy vấn ảnh");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblTitle.setBounds(579, 11, 284, 49);
		contentPane.add(lblTitle);
		
		JPanel panelQuery = new JPanel();
		panelQuery.setBounds(20, 122, 407, 326);
		contentPane.add(panelQuery);
		panelQuery.setLayout(null);
		
		JButton btnChooseFile = new JButton("Chọn file");
		btnChooseFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jFileChooser = new JFileChooser();
				jFileChooser.setDialogTitle("Chọn file tìm kiếm");
				jFileChooser.setCurrentDirectory(new File("Query"));
				jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int result = jFileChooser.showOpenDialog(null);
				if(result == jFileChooser.APPROVE_OPTION) {
					String fileName = jFileChooser.getSelectedFile().getPath();
					txtContentSearch.setText(fileName);
					panelQuery.removeAll();
					JLabel lblQ = new JLabel(new ImageIcon(new Execute().showImageQuery(fileName)));
					lblQ.setBounds(0, 0, 407, 326);
					//lblQ.setVisible(true);
					panelQuery.add(lblQ);
					panelQuery.revalidate();
					panelQuery.repaint();
				}
			}
		});
		btnChooseFile.setBounds(52, 64, 120, 36);
		contentPane.add(btnChooseFile);
		
		txtContentSearch = new JTextField();
		txtContentSearch.setEditable(false);
		txtContentSearch.setBounds(198, 64, 739, 36);
		contentPane.add(txtContentSearch);
		txtContentSearch.setColumns(10);
		
		JPanel panelResult = new JPanel();
		panelResult.setBounds(449, 122, 895, 774);
		contentPane.add(panelResult);
		panelResult.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 895, 774);
		panelResult.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		JLabel imgResult1 = new JLabel("");
		//imgResult.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\out.png"));
		imgResult1.setBounds(10, 11, 418, 362);
		panel.add(imgResult1);
		
		JLabel imgResult3 = new JLabel("");
		//imgMatch.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\matchoutput.jpg"));
		imgResult3.setBounds(10, 399, 418, 362);
		panel.add(imgResult3);
		
		JLabel imageResult2 = new JLabel("");
		//imageCover.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\img.jpg"));
		imageResult2.setBounds(465, 11, 418, 318);
		panel.add(imageResult2);
		
		JLabel imgResult4 = new JLabel("");
		imgResult4.setBounds(465, 399, 418, 362);
		panel.add(imgResult4);
		
		imgResult1.setVerticalTextPosition(JLabel.BOTTOM);
		imgResult1.setHorizontalTextPosition(JLabel.CENTER);
		
		imageResult2.setVerticalTextPosition(JLabel.BOTTOM);
		imageResult2.setHorizontalTextPosition(JLabel.CENTER);
		
		imgResult3.setVerticalTextPosition(JLabel.BOTTOM);
		imgResult3.setHorizontalTextPosition(JLabel.CENTER);
		
		imgResult4.setVerticalTextPosition(JLabel.BOTTOM);
		imgResult4.setHorizontalTextPosition(JLabel.CENTER);

		
		JLabel info = new JLabel("");
		
		JButton btnPrev = new JButton("Trước");
		JButton btnNext = new JButton("Sau");
		//button search
		JButton btnSearch = new JButton("Tìm kiếm");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String query = txtContentSearch.getText();
				if(query==null | query.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Chưa có nội dung tìm kiếm");
				}else {
//					ex = new Execute();
//					ex.executeQuery(txtContentSearch.getText());
//					stt = ex.showResultQuery(imgResult,imageCover,imgMatch,ex,0);
//					btnPrev.setEnabled(false);
//					if(ex.results.size()==0|ex.results.size()==1) {
//						btnNext.setEnabled(false);
//					}
					
					stt = 0;
					exe.ExcuteQuery(query);
					if(exe.results.size() <= 0) {
						//JOptionPane.showMessageDialog(null, "Không tìm thấy kết quả phù hợp");
					}else {
						int size = exe.results.size();
						//JOptionPane.showMessageDialog(null, "Kích thước: "+exe.results.size());
						int size1 = exe.results.size()>10?exe.results.size()/10:exe.results.size();
//						for(int i = 0;i<size1;i++) {
//							JOptionPane.showMessageDialog(null, exe.results.get(i).getPath());
//						}
						stt = exe.showResultQuery(imgResult1, imageResult2, imgResult3, imgResult4, stt, 0);
						info.setText(stt+" trên "+size+" kết quả");
					}
					
				}
				
			}
		});
		btnSearch.setBounds(952, 64, 120, 36);
		contentPane.add(btnSearch);
		
		
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(440, 154, 2, 522);
		contentPane.add(panel_1);
		
		
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_3.setBounds(10, 108, 1183, 3);
		contentPane.add(panel_3);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_4.setBounds(10, 151, 2, 525);
		contentPane.add(panel_4);
		
		//phim truoc
		btnPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int size = exe.results.size();
				stt  = exe.showResultQuery(imgResult1, imageResult2, imgResult3,imgResult4, stt,1);
				btnNext.setEnabled(true);
				if(stt <= 4) {
					btnPrev.setEnabled(false);
				}
				info.setText(stt+" trên "+size+" kết quả");
			}
		});
		btnPrev.setBounds(791, 907, 89, 23);
		contentPane.add(btnPrev);
		
		//phim sau
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int size = exe.results.size();
				stt  = exe.showResultQuery(imgResult1, imageResult2, imgResult3,imgResult4, stt,0);
				btnPrev.setEnabled(true);
				if(stt == size) {
					btnNext.setEnabled(false);
				}
				info.setText(stt+" trên "+size+" kết quả");
			}
		});
		btnNext.setBounds(910, 907, 89, 23);
		contentPane.add(btnNext);
		
		
		info.setFont(new Font("Tahoma", Font.PLAIN, 14));
		info.setBounds(1048, 908, 201, 22);
		contentPane.add(info);
		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
}
