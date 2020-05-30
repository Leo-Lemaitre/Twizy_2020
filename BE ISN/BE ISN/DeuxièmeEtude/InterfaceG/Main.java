package InterfaceG;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;

import TraitementImage.*;

public class Main {

	private JFrame frame;
	private JFileChooser fichier;
	private JTextArea textArea;
	private ImageLoader im;
	ArrayList<Integer> indexMax;
	private String point;
	private VideoCapture camera;
	static {
		try {
		    System.load("C:/opencv/build/x64/vc14/bin/opencv_ffmpeg2413_64.dll");
		    } catch (UnsatisfiedLinkError e) {
		        System.err.println("Native code library failed to load.\n" + e);
		        System.exit(1);
		    }
	}

	static Mat imag = null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main window = new Main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Main() {
		initialize();
		fichier = new JFileChooser(); 
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.DARK_GRAY);
		frame.getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(33, 58, 648, 459);
		frame.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(727, 93, 469, 381);
		frame.getContentPane().add(panel_1);

		frame.setResizable(false);

		JLabel lblNewLabel = new JLabel("Traitement d'image");
		lblNewLabel.setForeground(new Color(255, 255, 204));
		lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		lblNewLabel.setBounds(33, 35, 170, 13);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblResultat = new JLabel("Resultat");
		lblResultat.setForeground(new Color(255, 255, 204));
		lblResultat.setFont(new Font("Roboto", Font.PLAIN, 12));
		lblResultat.setBounds(727, 58, 77, 13);
		frame.getContentPane().add(lblResultat);

		JLabel lblBaseDeDonnees = new JLabel("Console");
		lblBaseDeDonnees.setForeground(new Color(255, 255, 204));
		lblBaseDeDonnees.setFont(new Font("Roboto", Font.PLAIN, 12));
	//	lblBaseDeDonnees.setBounds(950, 35, 100, 13)
		lblBaseDeDonnees.setBounds(33, 549, 100, 13);
		frame.getContentPane().add(lblBaseDeDonnees);
		


		textArea = new JTextArea();
	//	textArea.setBounds(523, 58, 345, 584);
		textArea.setBounds(33, 572, 1163, 70 );
		frame.getContentPane().add(textArea);
		textArea.setColumns(10);
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 1252, 724);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnNewButton_2_1 = new JButton("->");
		btnNewButton_2_1.setEnabled(false);
		btnNewButton_2_1.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2_1.setBackground(new Color(240, 248, 255));
		btnNewButton_2_1.setBounds(972, 484, 85, 21);
		frame.getContentPane().add(btnNewButton_2_1);

		JButton btnNewButton_2_1_1 = new JButton("<-");
		btnNewButton_2_1_1.setEnabled(false);
		btnNewButton_2_1_1.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2_1_1.setBackground(new Color(240, 248, 255));
		btnNewButton_2_1_1.setBounds(855, 484, 85, 21);
		frame.getContentPane().add(btnNewButton_2_1_1);

		JButton btnNewButton_RUN = new JButton("Run");
		btnNewButton_RUN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Mat m = im.imageOriginale;
				indexMax = DetectImage.detect(m);
				
				if(indexMax.size()==0) {
					textArea.setText("Aucun panneau n'est trouve");
				}else if(indexMax.size()==1) {
					addDetectedImageToPanel(panel_1,indexMax.get(0));
				}else{
					if(indexMax.size()==2) {
						btnNewButton_2_1.setEnabled(true);
						btnNewButton_2_1_1.setEnabled(true);
						addDetectedImageToPanel(panel_1,indexMax.get(0));
						btnNewButton_2_1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								panel_1.removeAll();
								addDetectedImageToPanel(panel_1,indexMax.get(1));
								panel_1.updateUI();
							}
						});
						


						btnNewButton_2_1_1.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								panel_1.removeAll();
								addDetectedImageToPanel(panel_1,indexMax.get(0));
								panel_1.updateUI();
							}
						});

					}

				}
				System.out.println(indexMax);
			}
		});
		btnNewButton_RUN.setEnabled(false);
		btnNewButton_RUN.setFont(new Font("Roboto", Font.PLAIN, 9));
		btnNewButton_RUN.setForeground(new Color(0, 0, 0));
		btnNewButton_RUN.setBackground(new Color(51, 255, 153));
		btnNewButton_RUN.setBounds(1111, 547, 85, 21);
		frame.getContentPane().add(btnNewButton_RUN);

		JButton btnNewButton_2 = new JButton("Afficher");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2.setBackground(new Color(240, 248, 255));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnNewButton_2.setBounds(1016, 546, 85, 21);
		frame.getContentPane().add(btnNewButton_2);

		JButton btnNewButton = new JButton("Importer image");
		btnNewButton.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton.setForeground(SystemColor.desktop);
		btnNewButton.setBackground(SystemColor.activeCaption);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panel.removeAll();
				panel_1.removeAll();
				panel.updateUI();
				panel_1.updateUI();

				int retval = fichier.showOpenDialog(btnNewButton);
				if(retval==JFileChooser.APPROVE_OPTION) {
					if(indexMax!=null) {
						indexMax.clear();
						btnNewButton_2_1.setEnabled(false);
						btnNewButton_2_1_1.setEnabled(false);
						
					}
					im = new ImageLoader(fichier.getSelectedFile().getPath());
					//	System.out.println(fichier.getSelectedFile().getPath());
					im.AfficherImage(panel,".jpg");
					btnNewButton_RUN.setEnabled(true);

				}
			}
		});
		btnNewButton.setBounds(360, 32, 151, 21);
		frame.getContentPane().add(btnNewButton);
		
		
	}
	public void addDetectedImageToPanel(JPanel panel_1,int indexmax) {
		point=TraitementImage.DetectKeypoints.DetectKeyPoint(im.imageOriginale);
		switch(indexmax){
		case -1:;break;
		case 0:
			im = new ImageLoader("ref30.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau 30 détecté"+"\n"+point);
			break;
		case 1:
			im = new ImageLoader("ref50.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau 50 détecté"+"\n"+point);
			break;
		case 2:
			im = new ImageLoader("ref70.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau 70 détecté"+"\n"+point);
			break;
		case 3:
			im = new ImageLoader("ref90.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau 90 détecté"+"\n"+point);
			break;
		case 4:
			im = new ImageLoader("ref110.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau 110 détecté"+"\n"+point);
			break;
		case 5:
			im = new ImageLoader("refdouble.jpg");
			im.AfficherImage(panel_1,".jpg");
			textArea.setText("Panneau interdiction de dépasser détecté"+"\n"+point);
			break;
		}
	}
	

	public static BufferedImage Mat2bufferedImage(Mat image) {
		MatOfByte bytemat = new MatOfByte();
		Highgui.imencode(".jpg", image, bytemat);
		byte[] bytes = bytemat.toArray();
		InputStream in = new ByteArrayInputStream(bytes);
		BufferedImage img = null;
		try {
			img = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return img;
	}

}
