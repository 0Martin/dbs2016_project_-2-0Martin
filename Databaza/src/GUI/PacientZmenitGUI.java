package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

import Databaza.*;
import Kartoteka.Hlavna;
import Objekty.*;

public class PacientZmenitGUI extends JFrame{
// upravuje pacienta ktory bol vyznaceny
	
	Pacient pacient;
	List<Poistovna> poistovne;
	JPanel obsah;
	JLabel menoLabel;
	JTextField menoTextField;
	JLabel priezviskoLabel;
	JTextField priezviskoTextField;
	JLabel rodne_cisloLabel;
	JTextField rodne_cisloTextField;
	JComboBox poistovneCombo;
	List<String> poistovneZoznam;
	Poistovna nastavenaPoistovna;
	JButton ok;
	
	public PacientZmenitGUI(int pacient_id, final Gui gui) {
		super("Zmena");
		setSize(250,175);
		setLocationRelativeTo(null);
		setResizable(false);
		
		this.pacient = DatabazaPacient.getPacient(pacient_id);
		poistovne=DatabazaPoistovna.getAll();
		
		obsah = new JPanel();
		menoLabel = new JLabel("meno:         ");
		menoTextField = new JTextField(15);
		priezviskoLabel = new JLabel("priezvisko: ");
		priezviskoTextField = new JTextField(15);
		rodne_cisloLabel = new JLabel("rodne_cislo:  ");
		rodne_cisloTextField = new JTextField(14);
		poistovneCombo = new JComboBox();
		for (int i=0;i<poistovne.size();i++){
			poistovneCombo.addItem(poistovne.get(i).getNazov());
			//overuje ci je to pacientova poistovna
			if (poistovne.get(i).getId()==pacient.getPoistovna_id())
				poistovneCombo.setSelectedIndex(i);
		}
		ok = new JButton("ok");
		
		menoTextField.setText(pacient.getMeno());
		priezviskoTextField.setText(pacient.getPriezvisko());
		rodne_cisloTextField.setText(pacient.getRodne_cislo());
		
		obsah.add(menoLabel);
		obsah.add(menoTextField);
		obsah.add(priezviskoLabel);
		obsah.add(priezviskoTextField);
		obsah.add(rodne_cisloLabel);
		obsah.add(rodne_cisloTextField);
		obsah.add(poistovneCombo);
		
		add(obsah, BorderLayout.CENTER);
		add(ok, BorderLayout.SOUTH);
		
		
		// akcie
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					//Kontrola spravnosti formatu zadanych udajov
					if (menoTextField.getText().equals("") || priezviskoTextField.getText().equals("") || rodne_cisloTextField.getText().equals(""))
						JOptionPane.showMessageDialog(null, "Je potrebne vyplnit vsetky udaje.");
					else if ( !rodne_cisloTextField.getText().matches("[0-9]+"))
						JOptionPane.showMessageDialog(null, "Rodne cislo musi byt vo formate cisel.");
					else{
						PacientZmenitGUI.this.pacient.setMeno(menoTextField.getText());
						PacientZmenitGUI.this.pacient.setPriezvisko(priezviskoTextField.getText());
						PacientZmenitGUI.this.pacient.setRodne_cislo(rodne_cisloTextField.getText());
						PacientZmenitGUI.this.pacient.setMeno(menoTextField.getText());
						PacientZmenitGUI.this.pacient.setPoistovna_id(poistovne.get(poistovneCombo.getSelectedIndex()).getId());
						gui.nacitajPacientov(DatabazaPacient.setPacient(PacientZmenitGUI.this.pacient));
						PacientZmenitGUI.this.dispose();
					}
			}
		});
		
	}
}
