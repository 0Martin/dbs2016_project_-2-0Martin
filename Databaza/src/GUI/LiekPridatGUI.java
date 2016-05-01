package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Databaza.DatabazaLieky;
import Databaza.DatabazaPacient;
import Databaza.DatabazaPoistovna;
import Objekty.Liek;
import Objekty.Navsteva;
import Objekty.Pacient;
import Objekty.Poistovna;
import Objekty.PredpisLieku;

public class LiekPridatGUI extends JFrame {
	
	PredpisLieku predpisLieku;
	List<Liek> lieky;
	
	JLabel liekyLabel;
	JComboBox liekyCombo;
	JLabel pocetLiekyLabel;
	JTextField pocetLiekyText;
	JPanel obsah;
	JButton ok;
	
	public LiekPridatGUI(final RieseniePridatGUI rieseniePridatGUI) {
		super("Pridanie lieku");
		setSize(250,125);
		setLocationRelativeTo(null);
		setResizable(false);
		
		this.predpisLieku = new PredpisLieku(0);
		lieky=DatabazaLieky.getAll();
		
		liekyLabel = new JLabel("Lieky:");
		liekyCombo = new JComboBox();
		for (Liek l: lieky)
			liekyCombo.addItem(l.getNazov());
		pocetLiekyLabel = new JLabel("Pocet kusov:");
		pocetLiekyText = new JTextField(10);
		obsah = new JPanel();
		ok = new JButton("ok");
		
		
		obsah.add(liekyLabel);
		obsah.add(liekyCombo);
		obsah.add(pocetLiekyLabel);
		obsah.add(pocetLiekyText);
		
		add(obsah, BorderLayout.CENTER);
		add(ok, BorderLayout.SOUTH);
		
		
		// akcie
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//kontrola spravnosti formatu zadanych udajov
				if (liekyCombo.getSelectedIndex() != -1 && pocetLiekyText.getText().matches("[0-9]+")) {
				predpisLieku.setLiek_id(lieky.get(liekyCombo.getSelectedIndex()).getId());
				predpisLieku.setKusov(Integer.parseInt(pocetLiekyText.getText()));
				predpisLieku.setNazov_lieku(lieky.get(liekyCombo.getSelectedIndex()).getNazov());
				rieseniePridatGUI.pridatLiek(predpisLieku);
				LiekPridatGUI.this.dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte liek a zadajte pocet v tvare cisla.");
			}
		});
		
	}
	
}
