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

public class PacientPridatGUI extends JFrame{
// pripadava pacintov 
	
	Pacient pacient;
	List<Poistovna> poistovne;
	JPanel obsah;
	JLabel menoLabel;
	JTextField menoTextField;
	JLabel priezviskoLabel;
	JTextField priezviskoTextField;
	JLabel rodne_cisloLabel;
	JTextField rodne_cisloTextField;
	List<String> poistovneZoznam;
	JComboBox poistovneCombo;
	JButton ok;
	
	public PacientPridatGUI(final Gui gui) {
		super("Pridanie");
		setSize(250,175);
		setLocationRelativeTo(null);
		setResizable(false);
		
		this.pacient = new Pacient(0);
		poistovne=DatabazaPoistovna.getAll();
		
		obsah = new JPanel();
		menoLabel = new JLabel("meno:         ");
		menoTextField = new JTextField(15);
		priezviskoLabel = new JLabel("priezvisko: ");
		priezviskoTextField = new JTextField(15);
		rodne_cisloLabel = new JLabel("rodne_cislo:  ");
		rodne_cisloTextField = new JTextField(14);
		poistovneZoznam = new ArrayList<String>();
		for (Poistovna p: poistovne)
			poistovneZoznam.add(p.getNazov());
		poistovneCombo = new JComboBox(poistovneZoznam.toArray());
		ok = new JButton("ok");
		
		
		obsah.add(menoLabel);
		obsah.add(menoTextField);
		obsah.add(priezviskoLabel);
		obsah.add(priezviskoTextField);
		obsah.add(rodne_cisloLabel);
		obsah.add(rodne_cisloTextField);
		obsah.add(poistovneCombo);
		
		add(obsah, BorderLayout.CENTER);
		add(ok, BorderLayout.SOUTH);
		
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Kontrola spravnosti formatu zadanych udajov
				if (menoTextField.getText().equals("") || priezviskoTextField.getText().equals("") || rodne_cisloTextField.getText().equals(""))
					JOptionPane.showMessageDialog(null, "Je potrebne vyplnit vsetky udaje.");
				else if ( !rodne_cisloTextField.getText().matches("[0-9]+"))
					JOptionPane.showMessageDialog(null, "Rodne cislo musi byt vo formate cisel.");
				else{
					PacientPridatGUI.this.pacient.setMeno(menoTextField.getText());
					PacientPridatGUI.this.pacient.setPriezvisko(priezviskoTextField.getText());
					PacientPridatGUI.this.pacient.setRodne_cislo(rodne_cisloTextField.getText());
					PacientPridatGUI.this.pacient.setPoistovna_id(poistovne.get(poistovneCombo.getSelectedIndex()).getId());
					gui.nacitajPacientov(DatabazaPacient.pridajPacienta(PacientPridatGUI.this.pacient));
					PacientPridatGUI.this.dispose();
				}
			}
		});
		
	}
}
