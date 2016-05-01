package GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;

import Databaza.DatabazaDiagnoza;
import Databaza.DatabazaDoktor;
import Databaza.DatabazaLieky;
import Databaza.DatabazaNavsteva;
import Databaza.DatabazaProblem;
import Objekty.Diagnoza;
import Objekty.Liek;
import Objekty.Navsteva;
import Objekty.Pacient;
import Objekty.PredpisLieku;
import Objekty.Problem;
import Objekty.Riesenie_navstevy;

public class RieseniePridatGUI extends JFrame{

	int pacientId;
	Problem problem;
	Diagnoza diagnozaNeurcena;
	List<Diagnoza> diagnozy;
	List<PredpisLieku> predpisy;
	List<Riesenie_navstevy> riesenia;
	Riesenie_navstevy riesenie;
	
	JComboBox vyberDiagnozy;
	JPanel stred;
	JPanel stareNavstevyPanel;
	JLabel stareNavstevyLabel;
	JScrollPane skrolStareNavstevy;
	JTextArea stareNavstevyText;
	JScrollPane skrolNovaNavsteva;
	JPanel novaNavstevaPanel;
	JLabel novaNavstevaLabel;
	JTextArea novaNavstevaText;
	JPanel vychod;
	DefaultListModel model;
	JLabel liekyLabel;
	JList liekyZoznam;
	JScrollPane skrolLieky;
	JPanel liekyTlacidla;
	JButton pridatLiek;
	JButton odstarnitLiek;
	JLabel hospitalizaciaLabel;
	JButton hospitalizaciaButton;
	JPanel hospitalizaciaPanel;
	JPanel tlacidla;
	JButton ok;
	JButton zrusit;
	
	/**
	 * Ked sa  upravuje stare riesenie_navstevy alebo vyplna nove, co zalezi od zadania riesenia ako parametran alebo nezadania teda null
	 * @param problem problem ktoreho s to tyka
	 * @param navstevaNovaGUI
	 * @param riesenie Ak upravujeme uz vytvorene riesenie , ak vyplname nove tak null
	 * @param index lokalne id riesenia(id nie je v databaze lebo nebolo este ulozene v tranzakcii ale docasne pre identifikovanie v Jave)
	 */
	public RieseniePridatGUI(final Problem problem, final NavstevaNovaGUI navstevaNovaGUI, final Riesenie_navstevy riesenie,final int index) {
		
		super(problem.getNazov()+" ("+problem.getDiagnoza()+")");
		setSize(700, 500);
		setLocationRelativeTo(null);
		
		this.problem=problem;
		diagnozaNeurcena = new Diagnoza(0);
		diagnozaNeurcena.setNazov("Neurcena");
		diagnozy = new ArrayList<Diagnoza>();
		diagnozy.add(diagnozaNeurcena);
		diagnozy.addAll(DatabazaDiagnoza.getAll());
		riesenia = DatabazaNavsteva.getAllbyProblemId(problem.getId());
		predpisy = new ArrayList<PredpisLieku>();
		if (riesenie != null)
		predpisy.addAll(riesenie.getPredpisy());
		
		
		//Gui prvky
		vyberDiagnozy = new JComboBox();
		for (int i=0;i<diagnozy.size();i++){
			vyberDiagnozy.addItem(diagnozy.get(i).getNazov());
			if (diagnozy.get(i).getId()== problem.getDiagnoza_id()){ //diagnoza rieseneho problemu
				vyberDiagnozy.setSelectedIndex(i);
			}
		}
		stred = new JPanel(new GridLayout(2,1));
		stareNavstevyPanel = new JPanel();
		stareNavstevyPanel.setLayout(new BoxLayout(stareNavstevyPanel, BoxLayout.PAGE_AXIS));
		stareNavstevyLabel = new JLabel("Predchadzajuce navstevy:");
		stareNavstevyLabel.setMaximumSize(getSize());
		stareNavstevyText = new JTextArea();
		stareNavstevyText.setEditable(false);
		stareNavstevyText.setColumns(25);
		for (Riesenie_navstevy rn: riesenia){
			stareNavstevyText.append("Datum: "+rn.getDatum().toString()+"\n");
			stareNavstevyText.append("Doktor: "+rn.getDoktor()+"\n");
			stareNavstevyText.append("Lieky: ");
			if (rn.getPredpisy().size() == 0) //pokial nema riesenie predpisane lieky
				stareNavstevyText.append("ziadne");
			else{
			for (PredpisLieku pl: rn.getPredpisy())
				stareNavstevyText.append(pl.getNazov_lieku()+" ks."+pl.getKusov()+", ");
				//ostrani posledne dva znaky teda ", "
				stareNavstevyText.setText(stareNavstevyText.getText().substring(0,stareNavstevyText.getText().length()-2));
			}
			stareNavstevyText.append("\nPopis: "+rn.getObsah());
			stareNavstevyText.append("\n\n\n");
		}
		skrolStareNavstevy = new JScrollPane(stareNavstevyText);
		novaNavstevaPanel = new JPanel();
		novaNavstevaPanel.setLayout(new BoxLayout(novaNavstevaPanel, BoxLayout.PAGE_AXIS));
		novaNavstevaLabel = new JLabel("Aktualna navsteva:");
		novaNavstevaLabel.setMaximumSize(getSize());
		novaNavstevaText = new JTextArea();
		novaNavstevaText.setColumns(25);
		if (riesenie != null) //upavuje sa uz vytvorene(nacitanie jeho udajov)
			novaNavstevaText.append(riesenie.getObsah());
		skrolNovaNavsteva=new JScrollPane(novaNavstevaText);
		liekyLabel = new JLabel("Lieky:");
		model = new DefaultListModel();
		if (riesenie != null) //upavuje sa uz vytvorene(nacitanie jeho udajov)
			for (PredpisLieku pl: riesenie.getPredpisy()){
				model.addElement(pl.getNazov_lieku()+" ("+pl.getKusov()+" ks.)");
			}
		liekyZoznam = new JList();
		liekyZoznam.setModel(model);
		skrolLieky = new JScrollPane(liekyZoznam);
		liekyTlacidla = new JPanel();
		pridatLiek = new JButton("pridat");
		odstarnitLiek = new JButton("odstranit");
		liekyTlacidla.add(pridatLiek);
		liekyTlacidla.add(odstarnitLiek);
		liekyTlacidla.setMaximumSize(getSize());
		hospitalizaciaLabel = new JLabel("Hospitalizacia: ziadna ");
		hospitalizaciaButton = new JButton("Zmenit");
		hospitalizaciaPanel = new JPanel();
		hospitalizaciaPanel.setMaximumSize(getSize());
		hospitalizaciaPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		vychod = new JPanel();
		vychod.setLayout(new BoxLayout(vychod, BoxLayout.PAGE_AXIS));
		
		tlacidla = new JPanel();
		ok = new JButton("ok");
		zrusit = new JButton("zrusit");
		tlacidla.add(ok);
		tlacidla.add(zrusit);
		
		add(vyberDiagnozy, BorderLayout.NORTH);
		stareNavstevyPanel.add(stareNavstevyLabel,BorderLayout.NORTH);
		stareNavstevyPanel.add(skrolStareNavstevy,BorderLayout.CENTER);
		stred.add(stareNavstevyPanel);
		novaNavstevaPanel.add(novaNavstevaLabel);
		novaNavstevaPanel.add(skrolNovaNavsteva);
		stred.add(novaNavstevaPanel);
		add(stred, BorderLayout.CENTER);
		vychod.add(liekyLabel);
		vychod.add(skrolLieky);
		vychod.add(liekyTlacidla);
		hospitalizaciaPanel.add(hospitalizaciaLabel);
		hospitalizaciaPanel.add(hospitalizaciaButton);
		vychod.add(hospitalizaciaPanel);
		add(vychod, BorderLayout.EAST);
		add(tlacidla, BorderLayout.SOUTH);
		//nacitajLieky();
		
		// akcie
		
		pridatLiek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LiekPridatGUI liekPridatGUI = new LiekPridatGUI(RieseniePridatGUI.this);
				liekPridatGUI.setVisible(true);
			}
		});
		odstarnitLiek.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (liekyZoznam.getSelectedIndex() != -1) //ci bol vybraty liek
					model.remove(liekyZoznam.getSelectedIndex());
				else
					JOptionPane.showMessageDialog(null, "Vyberte liek.");
			}
		});
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (riesenie == null)
					RieseniePridatGUI.this.riesenie = new Riesenie_navstevy(index);
				else
					RieseniePridatGUI.this.riesenie = riesenie;
				RieseniePridatGUI.this.riesenie.setProblem_id(problem.getId());
				RieseniePridatGUI.this.riesenie.setObsah(novaNavstevaText.getText());
				RieseniePridatGUI.this.riesenie.setPredpisy(predpisy);
				RieseniePridatGUI.this.problem.setDiagnoza(diagnozy.get(vyberDiagnozy.getSelectedIndex()).getNazov());
				RieseniePridatGUI.this.problem.setDiagnoza_id(diagnozy.get(vyberDiagnozy.getSelectedIndex()).getId());
				navstevaNovaGUI.pridajRiesenie( RieseniePridatGUI.this.problem,RieseniePridatGUI.this.riesenie);
				RieseniePridatGUI.this.dispose();
			}
		});

		zrusit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RieseniePridatGUI.this.dispose();
			}
		});
		
	}
		/**
		 * Pridanie zadaneho lieku, ktory je na predpise
		 * @param predpis predpis_lieku
		 */
		public  void pridatLiek(PredpisLieku predpis) {
			predpisy.add(predpis);
			model.addElement(predpis.getNazov_lieku()+" ("+predpis.getKusov()+" ks.)");
			liekyZoznam.setModel(model);
			skrolLieky.repaint();
		}
		

}

