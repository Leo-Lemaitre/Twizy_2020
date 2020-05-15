package InterfaceG;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Main {

	private JFrame frame;
	private JFileChooser fichier;
	private JTextField textField;
	private ImageLoader im;
	ArrayList<Integer> indexMax;
	private String point;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		panel.setBounds(33, 58, 478, 291);
		frame.getContentPane().add(panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(34, 382, 311, 260);
		frame.getContentPane().add(panel_1);

		frame.setResizable(false);

		JLabel lblNewLabel = new JLabel("Source");
		lblNewLabel.setForeground(new Color(255, 255, 204));
		lblNewLabel.setFont(new Font("Roboto", Font.PLAIN, 12));
		lblNewLabel.setBounds(33, 35, 77, 13);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblResultat = new JLabel("Resultat");
		lblResultat.setForeground(new Color(255, 255, 204));
		lblResultat.setFont(new Font("Roboto", Font.PLAIN, 12));
		lblResultat.setBounds(33, 359, 77, 13);
		frame.getContentPane().add(lblResultat);

		JButton btnImporterBd = new JButton("Importer BD");
		btnImporterBd.setForeground(Color.BLACK);
		btnImporterBd.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnImporterBd.setBackground(SystemColor.activeCaption);
		btnImporterBd.setBounds(1061, 32, 128, 21);
		frame.getContentPane().add(btnImporterBd);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(878, 58, 311, 260);
		frame.getContentPane().add(panel_2);

		JLabel lblBaseDeDonnees = new JLabel("Base de donnees");
		lblBaseDeDonnees.setForeground(new Color(255, 255, 204));
		lblBaseDeDonnees.setFont(new Font("Roboto", Font.PLAIN, 12));
		lblBaseDeDonnees.setBounds(950, 35, 100, 13);
		frame.getContentPane().add(lblBaseDeDonnees);


		textField = new JTextField();
		textField.setBounds(523, 58, 345, 584);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		frame.setBackground(Color.DARK_GRAY);
		frame.setBounds(100, 100, 1252, 724);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton btnNewButton_2_1 = new JButton("->");
		btnNewButton_2_1.setEnabled(false);
		btnNewButton_2_1.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2_1.setBackground(new Color(240, 248, 255));
		btnNewButton_2_1.setBounds(354, 417, 85, 21);
		frame.getContentPane().add(btnNewButton_2_1);

		JButton btnNewButton_2_1_1 = new JButton("<-");
		btnNewButton_2_1_1.setEnabled(false);
		btnNewButton_2_1_1.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2_1_1.setBackground(new Color(240, 248, 255));
		btnNewButton_2_1_1.setBounds(354, 448, 85, 21);
		frame.getContentPane().add(btnNewButton_2_1_1);

		JButton btnNewButton_RUN = new JButton("Run");
		btnNewButton_RUN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				indexMax = src.DetectImage.detect(im.imageOriginale);
				
				
			

				if(indexMax.size()==0) {
					textField.setText("Aucun panneau n'est trouve");
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
		btnNewButton_RUN.setBounds(354, 569, 85, 21);
		frame.getContentPane().add(btnNewButton_RUN);

		JLabel lblNewLabel_1 = new JLabel("...");
		lblNewLabel_1.setBounds(449, 568, 144, 21);
		frame.getContentPane().add(lblNewLabel_1);

		JButton btnNewButton_2 = new JButton("Afficher");
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnNewButton_2.setBackground(new Color(240, 248, 255));
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		btnNewButton_2.setBounds(354, 383, 85, 21);
		frame.getContentPane().add(btnNewButton_2);



		JLabel lblNewLabel_1_1 = new JLabel("...");
		lblNewLabel_1_1.setBounds(449, 382, 144, 21);
		frame.getContentPane().add(lblNewLabel_1_1);

		JButton btnImporterVideo = new JButton("Importer video");
		btnImporterVideo.setForeground(Color.BLACK);
		btnImporterVideo.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnImporterVideo.setBackground(SystemColor.activeCaption);
		btnImporterVideo.setBounds(360, 32, 151, 21);
		frame.getContentPane().add(btnImporterVideo);

		JButton btnStocker = new JButton("Exporter");
		btnStocker.setForeground(Color.BLACK);
		btnStocker.setFont(new Font("Roboto", Font.PLAIN, 10));
		btnStocker.setBackground(SystemColor.activeCaption);
		btnStocker.setBounds(1061, 328, 128, 21);
		frame.getContentPane().add(btnStocker);

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
		btnNewButton.setBounds(194, 32, 151, 21);
		frame.getContentPane().add(btnNewButton);


	}
	public void addDetectedImageToPanel(JPanel panel_1,int indexmax) {
		switch(indexmax){
		case -1:;break;
		case 0:
			im = new ImageLoader("ref30.jpg");
			im.AfficherImage(panel_1,".jpg");
			textField.setText("Panneau 30 détecté");
			break;
		case 1:
			im = new ImageLoader("ref50.jpg");
			im.AfficherImage(panel_1,".jpg");
			break;
		case 2:
			im = new ImageLoader("ref70.jpg");
			im.AfficherImage(panel_1,".jpg");
			break;
		case 3:
			im = new ImageLoader("ref90.jpg");
			im.AfficherImage(panel_1,".jpg");
			break;
		case 4:
			im = new ImageLoader("ref110.jpg");
			im.AfficherImage(panel_1,".jpg");
			break;
		case 5:
			im = new ImageLoader("refdouble.jpg");
			im.AfficherImage(panel_1,".jpg");
			break;
		}
	}
}
