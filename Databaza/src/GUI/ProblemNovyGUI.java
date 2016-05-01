package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Databaza.DatabazaDiagnoza;
import Objekty.Diagnoza;
import Objekty.Problem;

public class ProblemNovyGUI extends JFrame{
	
	List<Diagnoza> diagnozy;
	Diagnoza diagnozaNeurcena;
	
	JPanel obsah;
	JLabel nazovLabel;
	JTextField nazovText;
	JLabel diagnozaLabel;
	JComboBox diagnozaCombo;
	JPanel tlacidla;
	JButton ok;
	JButton zrusit;

	/**
	 * Vytvorenie noveho problemu na ktory sa zaroven naviaze a vytvory nove riesenie_problemu s id zadanym ako index
	 * @param navstevaNovaGUI
	 * @param index id noveho riesenia_problemu 
	 * (musi byt zadany z dovodu aby kazde riesenie malo unikatny identidikator, ktory este nema zadany databazou kvoli tranzakcii)
	 * @param doktorId id doktora u ktoreho prebieha navsteva
	 * @param pacientId id pacienta, ktory je vysetrovany
	 */
	public ProblemNovyGUI(final NavstevaNovaGUI navstevaNovaGUI, final int index, final int doktorId, final int pacientId){
		super("Pridat zaznam");
		setSize(250,125);
		setResizable(false);
		setLocationRelativeTo(null);
		
		//pridanie diagnoz ci uz moznosti neurcenej alebo vsetkych ulozenych v databaze+
		diagnozaNeurcena = new Diagnoza(0);
		diagnozaNeurcena.setNazov("Neurcena");
		diagnozy = new ArrayList<Diagnoza>();
		diagnozy.add(diagnozaNeurcena);
		diagnozy.addAll(DatabazaDiagnoza.getAll());
		
		obsah = new JPanel();
		nazovLabel = new JLabel("Nazov:");
		nazovText = new JTextField(15);
		diagnozaLabel = new JLabel("Diagnoza:");
		diagnozaCombo = new JComboBox();
		for (Diagnoza d: diagnozy)
			diagnozaCombo.addItem(d.getNazov());
		tlacidla = new JPanel();
		ok = new JButton("ok");
		zrusit = new JButton("zrusit");
		
		obsah.add(nazovLabel);
		obsah.add(nazovText);
		obsah.add(diagnozaLabel);
		obsah.add(diagnozaCombo);
		tlacidla.add(ok);
		tlacidla.add(zrusit);
		add(obsah,BorderLayout.CENTER);
		add(tlacidla,BorderLayout.SOUTH);
		
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!nazovText.getText().equals("")){
					Problem problem = new Problem(0);
					problem.setNazov(nazovText.getText());
					problem.setDiagnoza_id(diagnozy.get(diagnozaCombo.getSelectedIndex()).getId());
					problem.setDiagnoza(diagnozy.get(diagnozaCombo.getSelectedIndex()).getNazov());
					problem.setDoktor_id(doktorId);
					problem.setPacient_id(pacientId);
					//vyplnenie noveho riesenia_problemu ktory sa naviaze na tento novovzniknuty problem
					RieseniePridatGUI rieseniePridatGUI = new RieseniePridatGUI(problem, navstevaNovaGUI, null,index);
					rieseniePridatGUI.setVisible(true);
					ProblemNovyGUI.this.dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Je potrebne zadat nazov.");
			}
		});
		zrusit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProblemNovyGUI.this.dispose();
			}
		});
	}
}
