package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import Databaza.DatabazaDoktor;



public class PrihlasovanieGUI extends JFrame {
	
	JPanel obsahPanel;
	JLabel menoLabel;
	JTextField menoTextField;
	JLabel hesloLabel;
	JPasswordField hesloTextField;
	JButton ok;
	int id;
	
	public PrihlasovanieGUI(){
		super("Prihlasenie");
		setSize(250,100);
		setLocationRelativeTo(null);
		setResizable(false);
		
		obsahPanel = new JPanel();
		menoLabel = new JLabel("meno: ");
		menoTextField = new JTextField(15);
		menoTextField.setText("Dr. Zaludok");
		ok = new JButton("ok");
		
		obsahPanel.add(menoLabel);
		obsahPanel.add(menoTextField);
		
		add(obsahPanel, BorderLayout.CENTER);
		add(ok, BorderLayout.SOUTH);
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Gui gui;
				
				id=DatabazaDoktor.overPrihlasenie(menoTextField.getText());
				
				if (id == -1)
					JOptionPane.showMessageDialog(null, "Nespravne meno.");
				else {
					gui = new Gui(id);
					gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					gui.setVisible(true);
					dispose();
				}
					
			}
		});
	}

}
