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
		setBounds(100, 100, 1389, 793);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblTitle = new JLabel("Demo truy van anh");
		lblTitle.setFont(new Font("Tahoma", Font.BOLD, 23));
		lblTitle.setBounds(490, 11, 234, 49);
		contentPane.add(lblTitle);
		
		JPanel panelQuery = new JPanel();
		panelQuery.setBounds(20, 122, 407, 326);
		contentPane.add(panelQuery);
		panelQuery.setLayout(null);
		
		JButton btnChooseFile = new JButton("Chon file");
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
		txtContentSearch.setBounds(198, 64, 739, 36);
		contentPane.add(txtContentSearch);
		txtContentSearch.setColumns(10);
		
		JPanel panelResult = new JPanel();
		panelResult.setBounds(449, 122, 895, 598);
		contentPane.add(panelResult);
		panelResult.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 895, 603);
		panelResult.add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(null);
		
		JLabel imgResult = new JLabel("");
		//imgResult.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\out.png"));
		imgResult.setBounds(10, 11, 418, 275);
		panel.add(imgResult);
		
		JLabel imgMatch = new JLabel("");
		//imgMatch.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\matchoutput.jpg"));
		imgMatch.setBounds(39, 299, 774, 278);
		panel.add(imgMatch);
		
		JLabel imageCover = new JLabel("");
		//imageCover.setIcon(new ImageIcon("E:\\laptrinhjava\\luyentap java\\DemoOpenCV\\img.jpg"));
		imageCover.setBounds(463, 11, 430, 275);
		panel.add(imageCover);
		
		JButton btnPrev = new JButton("Trước");
		JButton btnNext = new JButton("Sau");
		
		JButton btnSearch = new JButton("Tim kiem");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String query = txtContentSearch.getText();
				if(query==null | query.trim().equals("")) {
					JOptionPane.showMessageDialog(null, "Chưa có nội dung tìm kiếm");
				}else {
					
					ex.executeQuery(txtContentSearch.getText());
					stt = ex.showResultQuery(imgResult,imageCover,imgMatch,ex,0);
					btnPrev.setEnabled(false);
					if(ex.results.size()==0|ex.results.size()==1) {
						btnNext.setEnabled(false);
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
				stt  = ex.showResultQuery(imgResult, imageCover, imgMatch, ex, stt-1);
				if(stt == 0) {
					btnPrev.setEnabled(false);
				}
				btnNext.setEnabled(true);
			}
		});
		btnPrev.setBounds(795, 731, 89, 23);
		contentPane.add(btnPrev);
		
		//phim sau
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stt  = ex.showResultQuery(imgResult, imageCover, imgMatch, ex, stt+1);
				if(stt == -1) {
					btnNext.setEnabled(false);
					stt = ex.results.size()-1;
				}
				btnPrev.setEnabled(true);
			}
		});
		btnNext.setBounds(920, 731, 89, 23);
		contentPane.add(btnNext);
		
		this.setResizable(false);
		this.setLocationRelativeTo(null);
	}
}
