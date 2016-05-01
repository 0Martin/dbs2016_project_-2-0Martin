package GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Timestamp;
import java.util.*;

import javax.swing.*;

import Databaza.DatabazaDoktor;
import Databaza.DatabazaProblem;
import Kartoteka.Hlavna;
import Objekty.*;
import Redis.RedisLieky;

public class ProblemyGUI extends JFrame{
	
	int pacientId;
	List<Problem> problemy;
	List<Diagnoza> diagnozy;
	String filter;
	
	JScrollPane skrolZoznam;
	JPanel vyberDiagnozyPanel;
	JLabel vyberDiagnozyLabel;
	JComboBox vyberDiagnozyCombo;
	JLabel RadioButtonLabel;
	ButtonGroup skupinaRadioButton;
	JRadioButton nazovRadioButton; 
	JRadioButton datumRadioButton;
	JPanel obsahPanel;
	JTable obsah;
	JPanel tlacidla;
	JButton zobrazit;
	JButton vybrat;
	JButton odstranit;
	
	/**
	 * 
	 * @param gui 
	 * @param pacient pacinta pre ktoreho problemy
	 * @param navstevaNovaGUI
	 * @param index id pre novovzniknute riesenie problemu 
	 * 
	 */
	public ProblemyGUI(Gui gui, final Pacient pacient, final NavstevaNovaGUI navstevaNovaGUI,final int index,String filter) {
		
		super(pacient.getMeno()+" "+pacient.getPriezvisko()+" zaznamy");
		setSize(650, 400);
		setLocationRelativeTo(null);
		
		pacientId=pacient.getId();
		this.filter=filter;
		problemy = new ArrayList<Problem>(DatabazaProblem.getAllbyPacientId(pacient.getId(),-1,1,filter));
		diagnozy = new ArrayList<Diagnoza>(DatabazaProblem.getDiagnozyByPacientId(pacient.getId()));
		
		//Gui prvky
		vyberDiagnozyPanel = new JPanel();
		vyberDiagnozyLabel = new JLabel("Diagnoza: ");
		vyberDiagnozyCombo = new JComboBox();
		vyberDiagnozyCombo.addItem("vsetky");
		for (Diagnoza d: diagnozy)
			vyberDiagnozyCombo.addItem(d.getNazov()+" ("+d.getPocet()+")");
		Object nazovStlpcov[] = {"nazov","diagnoza  ","Datum pos. navstevy"};
		Object udajeTabulky[][] = new Object [problemy.size()][3];
		for (int i=0;i<problemy.size();i++){
				udajeTabulky[i][0]=problemy.get(i).getNazov();
				udajeTabulky[i][1]=problemy.get(i).getDiagnoza();
				udajeTabulky[i][2]=problemy.get(i).getPoslednaNavsteva();
		}
		 RadioButtonLabel = new  JLabel("Zoradit podla: ");
		 skupinaRadioButton = new ButtonGroup();
		 nazovRadioButton = new JRadioButton("nazov");
		 datumRadioButton = new JRadioButton("datum");
		skupinaRadioButton.add(nazovRadioButton);
		skupinaRadioButton.add(datumRadioButton);
		obsahPanel = new JPanel();
		obsahPanel.setLayout(new BoxLayout(obsahPanel, BoxLayout.PAGE_AXIS));
		obsah = new JTable(udajeTabulky,nazovStlpcov);
		skrolZoznam = new JScrollPane(obsah);
		tlacidla = new JPanel();
		zobrazit = new JButton("zobrazit");
		vybrat = new JButton("vybrat");
		odstranit = new JButton("odstranit");
		tlacidla.add(zobrazit);
		if (navstevaNovaGUI != null)
			tlacidla.add(vybrat);
		else
			tlacidla.add(odstranit);
		
		vyberDiagnozyPanel.add(vyberDiagnozyLabel);
		vyberDiagnozyPanel.add(vyberDiagnozyCombo);
		vyberDiagnozyPanel.add(RadioButtonLabel);
		vyberDiagnozyPanel.add(nazovRadioButton);
		vyberDiagnozyPanel.add(datumRadioButton);
		add(vyberDiagnozyPanel, BorderLayout.NORTH);
		obsahPanel.add(skrolZoznam);
		add(obsahPanel, BorderLayout.CENTER);
		add(tlacidla, BorderLayout.SOUTH);
		
		// akcie
		vyberDiagnozyCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (vyberDiagnozyCombo.getSelectedIndex() == 0) //pre vsetky diagnozy
					nacitajZaznamy(pacient.getId(),-1);
				else if (vyberDiagnozyCombo.getSelectedIndex() != -1) //pre konkretnu diagnozu
					nacitajZaznamy(pacient.getId(),diagnozy.get(vyberDiagnozyCombo.getSelectedIndex()-1).getId());
			}
		});
		nazovRadioButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (vyberDiagnozyCombo.getSelectedIndex() == 0) //pre vsetky diagnozy
					nacitajZaznamy(pacient.getId(),-1);
				else if (vyberDiagnozyCombo.getSelectedIndex() != -1) //pre konkretnu diagnozu zoradene podla mena
					nacitajZaznamy(pacient.getId(),diagnozy.get(vyberDiagnozyCombo.getSelectedIndex()-1).getId());
			}
		});
		datumRadioButton.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (vyberDiagnozyCombo.getSelectedIndex() == 0) //pre vsetky diagnozy
					nacitajZaznamy(pacient.getId(),-1);
				else if (vyberDiagnozyCombo.getSelectedIndex() != -1) //pre konkretnu diagnozu zoradene podla poslednej navstevy
					nacitajZaznamy(pacient.getId(),diagnozy.get(vyberDiagnozyCombo.getSelectedIndex()-1).getId());
			}
		});
		zobrazit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (obsah.getSelectedRow() != -1){ //ci bol vybrany zaznam
					ProblemZobrazitGUI problemZobrazitGUI  = new ProblemZobrazitGUI(problemy.get(obsah.getSelectedRow()));
					problemZobrazitGUI.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte zaznam.");
			}
		});
		vybrat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (obsah.getSelectedRow() != -1){ //ci bol vybrany zaznam
					RieseniePridatGUI rieseniePridatGUI = new RieseniePridatGUI(problemy.get(obsah.getSelectedRow()), navstevaNovaGUI,null,index);
					rieseniePridatGUI.setVisible(true);
					ProblemyGUI.this.dispose();
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte zaznam.");
			}
		});
		odstranit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (obsah.getSelectedRow() != -1){ //ci bol vybrany zaznam
					DatabazaProblem.deleteProblem(ProblemyGUI.this.problemy.get(obsah.getSelectedRow()).getId());
					nacitaj(pacientId);
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte zaznam.");
			}
		});
	}
	
		/**Nacita diagnozy do ComboBox pre konkretneho pacinta a zavola aj metodu nacitajZaznamy(pacient_id,-1) na nacitanie zaznamov
		 * 
		 * @param pacient_id id pacienta
		 */
		public void nacitaj(int pacient_id){
			this.diagnozy = new ArrayList<Diagnoza>(DatabazaProblem.getDiagnozyByPacientId(pacient_id));
			vyberDiagnozyCombo.removeAllItems();
			vyberDiagnozyCombo.addItem("vsetky");
			for (Diagnoza d: diagnozy)
				vyberDiagnozyCombo.addItem(d.getNazov());
			vyberDiagnozyCombo.repaint();
			nacitajZaznamy(pacient_id,-1);
		}
		
		/**
		 * Nacita zaznamy s danou diagnozou pre konkretneho pacienta do JListu 
		 * @param pacient_id id pacienta
		 * @param diagnoza_id tie zaznamy ktore maju id diagnoza_id, ak -1 tak vsetky
		 */
		public void nacitajZaznamy(int pacient_id, int diagnoza_id){
			int zoradit=1;
			if (nazovRadioButton.isSelected()) //zoradit podla nazvu
				zoradit=1;
			else if (datumRadioButton.isSelected()) //zoradit podla poslednej navstevy
				zoradit=2;
			problemy = DatabazaProblem.getAllbyPacientId(pacient_id,diagnoza_id,zoradit,filter);
			Object nazovStlpcov[] = {"nazov","diagnoza","Datum pos. navstevy"};
			Object udajeTabulky[][] = new Object [problemy.size()][3];
			for (int i=0;i<problemy.size();i++){
					udajeTabulky[i][0]=problemy.get(i).getNazov();
					udajeTabulky[i][1]=problemy.get(i).getDiagnoza();
					udajeTabulky[i][2]=problemy.get(i).getPoslednaNavsteva();
			}
			obsah = new JTable(udajeTabulky,nazovStlpcov);
			skrolZoznam.setViewportView(obsah);
			skrolZoznam.repaint();
		}
		
		
		
}
