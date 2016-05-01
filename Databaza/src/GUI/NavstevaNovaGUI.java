package GUI;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import Databaza.DatabazaNavsteva;
import Objekty.Pacient;
import Objekty.Problem;
import Objekty.Riesenie_navstevy;
import Redis.RedisLieky;

public class NavstevaNovaGUI extends JFrame {

	List<Riesenie_navstevy> riesenia;
	List<Problem> problemy;
	int index=1; //plni funkciu AUTO_INCREMENT kedze riesenie_problemu nemaju id dane databazou
				 //a je potrebny unikatny identifikator
	
	JPanel obsahPanel;
	JTable obsah;
	JScrollPane obsahSkrol;
	JLabel liekyLabel;
	JScrollPane skrolLieky;
	JTextArea liekyTextArea;
	JPanel tlacidla;
	JButton vytvoritNovy;
	JButton doplnitExistujuci;
	JButton upravit;
	JButton ulozit;
	JButton odstranit;
	
	/**
	 * Nova navsteva
	 * @param gui
	 * @param pacient pacient ktory je na navsteve
	 * @param doktorId id doktora u ktore je na navsteve
	 */
	public NavstevaNovaGUI (final Gui gui, final Pacient pacient, final int doktorId) {
		super("Nova navsteva");
		setSize(700,450);
		setLocationRelativeTo(null);
		riesenia = new ArrayList<Riesenie_navstevy>();
		problemy = new ArrayList<Problem>();
		
		obsahPanel = new JPanel();
		obsahPanel.setLayout(new BoxLayout(obsahPanel, BoxLayout.PAGE_AXIS));
		obsah = new JTable();
		obsahSkrol = new JScrollPane(obsah);
		liekyLabel = new JLabel("Uzivane lieky za posledny rok:");
		liekyTextArea = new JTextArea(zistiUzivaneLiekyZaPoslednyRok(pacient.getId()));
		liekyTextArea.setRows(10);
		liekyTextArea.setEditable(false);
		skrolLieky = new JScrollPane(liekyTextArea);
		tlacidla = new JPanel();
		vytvoritNovy = new JButton("vytvorit novy zaz. prob.");
		doplnitExistujuci = new JButton("doplnit existujuci zaz. prob.");
		upravit = new JButton("upravit");
		ulozit = new JButton("ulozit vsetky");
		odstranit = new JButton("odstranit");
		
		obsahPanel.add(obsahSkrol);
		obsahPanel.add(liekyLabel);
		obsahPanel.add(skrolLieky);
		tlacidla.add(vytvoritNovy);
		tlacidla.add(doplnitExistujuci);
		tlacidla.add(upravit);
		tlacidla.add(ulozit);
		tlacidla.add(odstranit);
		add(obsahPanel,BorderLayout.CENTER);
		add(tlacidla,BorderLayout.SOUTH);
		
		vytvoritNovy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProblemNovyGUI problemNovyGUI = new ProblemNovyGUI(NavstevaNovaGUI.this,index,doktorId,pacient.getId());
				index++; //AUTO_INCREMENT(bol vytvorene nove risenie_problemu v problemNovyGUI)
				problemNovyGUI.setVisible(true);
			}
		});
		doplnitExistujuci.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String filter="";
				for(Problem p: problemy)
					filter+=p.getId()+", ";
				if(filter.length()>2)
				  filter = filter.substring(0, filter.length()-2);
				ProblemyGUI problemyGUI = new ProblemyGUI( gui, pacient, NavstevaNovaGUI.this,index,filter);
				index++; //AUTO_INCREMENT(bol vytvorene nove risenie_problemu v problemNovyGUI)
				problemyGUI.setVisible(true);
			}
		});
		upravit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (obsah.getSelectedRow() != -1){
					RieseniePridatGUI rieseniePridatGUI = new RieseniePridatGUI(problemy.get(obsah.getSelectedRow()), NavstevaNovaGUI.this,riesenia.get(obsah.getSelectedRow()),riesenia.get(obsah.getSelectedRow()).getId());
					rieseniePridatGUI.setVisible(true);
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte zaznam.");
			}
		});
		
		odstranit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (obsah.getSelectedRow() != -1){
					// prehladata riesenia a ziskat taky ktory ma problme c.?
					problemy.remove(obsah.getSelectedRow());
					riesenia.remove(obsah.getSelectedRow());
					refresh();
				}
				else
					JOptionPane.showMessageDialog(null, "Vyberte zaznam.");
			}
		});
		ulozit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DatabazaNavsteva.ulozNavstevu(problemy,riesenia,doktorId,pacient.getId());
				dispose();
			}
		});
	}

	/**
	 * Pridanie risenia_navstevy spolu s problemom na ktory je naviazany
	 * @param problem 
	 * @param riesenie
	 */
	public void pridajRiesenie(Problem problem, Riesenie_navstevy riesenie) {
		for (int i=0;i<riesenia.size();i++){
			//Ak islo o upravu stareho tak sa pred pridanim odstrani stare
			if (riesenie.getId()==riesenia.get(i).getId()){
				problemy.remove(i);
				riesenia.remove(i);
			}
		}
		problemy.add(problem);
		riesenia.add(riesenie);
		refresh();
	}
	
	/**
	 * Nacitanie ulozenych problemov do JTable
	 */
	public void refresh(){
		Object nazovStlpcov[] = {"nazov","diagnoza","Datum pos. navstevy"};
		Object udajeTabulky[][] = new Object [problemy.size()][3];
		for (int i=0;i<problemy.size();i++){
				udajeTabulky[i][0]=problemy.get(i).getNazov();
				udajeTabulky[i][1]=problemy.get(i).getDiagnoza();
				udajeTabulky[i][2]=problemy.get(i).getPoslednaNavsteva();
		}
		obsah = new JTable(udajeTabulky,nazovStlpcov);
		obsah.repaint();
		obsahSkrol.setViewportView(obsah);
		obsahSkrol.repaint();
	}

	/**
	 * Zisti vsetky lieky uzivane pacientom za posledny rok
	 * @param pacient_id ID pacienta
	 * @return String so zoznamom liekov kazdy na novom riadku
	 */
	public String zistiUzivaneLiekyZaPoslednyRok(int pacient_id){
		Set<String> lieky = null;
		String vysledok="";
		
		lieky=RedisLieky.getLiekyZaObdobie(pacient_id, new Timestamp(new java.util.Date().getTime()));
		if (lieky.isEmpty())
			vysledok="Ziadne";
		else{
			for(String s: lieky)
				vysledok+=s+"\n";
		}
		
		return vysledok;
	}
	
	
}
