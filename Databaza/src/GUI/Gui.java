package GUI;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.html.HTMLDocument.HTMLReader.HiddenAction;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Objekty.*;
import Redis.RedisLieky;
import redis.clients.jedis.Tuple;
import Databaza.*;
import Kartoteka.Hlavna;

public class Gui extends JFrame{
	
	List<Pacient> pacienti;
	
	JLabel vyhladavacLabel;
	JTextArea vyhladavac;
	JPanel vyhladavacPanel;
	DefaultListModel model;
	JList zoznam;
	JScrollPane skrolZoznam;
	JTextArea obsah;
	JScrollPane skrolObsah;
	JPanel tlacidla;
	JButton pridat;
	JButton zmenit;
	JButton prehlad;
	JButton novaNavsteva;
	JButton odstranit;
	JButton statistikaLiekov;
	JButton odhlasitSa;
	
	
	public Gui(final int doktorId){
		super("Kartoteka");
		setSize(800,500);
		setLocationRelativeTo(null);
		
		
		vyhladavacLabel = new JLabel("Vyhladat:");
		vyhladavac = new JTextArea();
		vyhladavac.setBorder(BorderFactory.createLineBorder(Color.black));
		vyhladavac.setColumns(25);
		vyhladavacPanel = new JPanel();
		zoznam = new JList();
		skrolZoznam = new JScrollPane(zoznam);
		obsah = new JTextArea();
		obsah.setEditable(false);
		skrolObsah = new JScrollPane(obsah);
		tlacidla = new JPanel();
		tlacidla.setLayout(new FlowLayout());
		pridat = new JButton("pridat");
		zmenit = new JButton("zmenit");
		odstranit = new JButton("odstranit");
		prehlad = new JButton("prehlad");
		novaNavsteva = new JButton("nova navsteva");
		statistikaLiekov = new JButton("statistika liekov");
		odhlasitSa = new JButton("odhlasit sa");
		
		nacitajPacientov(DatabazaPacient.getAll());
		
		vyhladavacPanel.add(vyhladavacLabel);
		vyhladavacPanel.add(vyhladavac);
		tlacidla.add(pridat);
		tlacidla.add(zmenit);
		tlacidla.add(odstranit);
		tlacidla.add(prehlad);
		tlacidla.add(novaNavsteva);
		tlacidla.add(statistikaLiekov);
		tlacidla.add(odhlasitSa);
		
		add(vyhladavacPanel, BorderLayout.NORTH);
		add(skrolZoznam, BorderLayout.WEST);
		add(skrolObsah, BorderLayout.CENTER);
		add(tlacidla, BorderLayout.SOUTH);
		
		//akcie
		vyhladavac.addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent e) {}
			public void keyReleased(KeyEvent e) {}
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER){ // ked sa stlaci Enter
					e.consume(); // odstranenie Entera
					nacitajPacientov(DatabazaPacient.vyhladajPacientov(vyhladavac.getText()));
				}
			}
		});
		zoznam.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (zoznam.getSelectedIndex() == -1) // nie je vybrany prvok
					obsah.setText("");
				// iba pri stlaceni mysi volalo iba raz a nie aj pri stlaceni a uvolneni
				else if (e.getValueIsAdjusting() == false) 
					obsah.setText(DatabazaPacient.getPopis(Gui.this.pacienti.get(zoznam.getSelectedIndex()).getId()));
			}
		});
		pridat.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				zoznam.clearSelection();
				PacientPridatGUI pridatpacientaGui = new PacientPridatGUI(Gui.this);
				pridatpacientaGui.setVisible(true);
			}
		});
		zmenit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (zoznam.getSelectedIndex() == -1)
					JOptionPane.showMessageDialog(null, "Vyberte pacienta");
				else{
					PacientZmenitGUI pacientZmenitGUI = new PacientZmenitGUI(Gui.this.pacienti.get(zoznam.getSelectedIndex()).getId(),Gui.this);
					zoznam.clearSelection();
					pacientZmenitGUI.setVisible(true);
				}
			}
		});
		odstranit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (zoznam.getSelectedIndex() == -1)
					JOptionPane.showMessageDialog(null, "Vyberte pacienta");
				else
					nacitajPacientov(DatabazaPacient.odstranitPacienta(Gui.this.pacienti.get(zoznam.getSelectedIndex()).getId()));
			}
		});
		prehlad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (zoznam.getSelectedIndex() == -1)
					JOptionPane.showMessageDialog(null, "Vyberte pacienta");
				else{
					ProblemyGUI problemyGUI = new ProblemyGUI( Gui.this, pacienti.get(zoznam.getSelectedIndex()), null,-1,"");
					problemyGUI.setVisible(true);
				}
			}
		});
		// zobrazenie zaznamov k vybranemu pacientovy zo zoznamu
		novaNavsteva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (zoznam.getSelectedIndex() == -1)
					JOptionPane.showMessageDialog(null, "Vyberte pacienta");
				else{
					NavstevaNovaGUI novaNavstevaGUI = new NavstevaNovaGUI(Gui.this, Gui.this.pacienti.get(zoznam.getSelectedIndex()), doktorId);
					zoznam.clearSelection();
					novaNavstevaGUI.setVisible(true);
				}
			}
		});
		statistikaLiekov.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					Set<Tuple> lieky;
					if (zoznam.getSelectedIndex() == -1)
						JOptionPane.showMessageDialog(null, "Vyberte pacienta");
					else{
					obsah.setText("Statistika liekov\n\n");
					obsah.append(Gui.this.pacienti.get(zoznam.getSelectedIndex()).getMeno());
					obsah.append(" "+Gui.this.pacienti.get(zoznam.getSelectedIndex()).getPriezvisko()+":\n");
					lieky = RedisLieky.getStatistikaLiekov(Gui.this.pacienti.get(zoznam.getSelectedIndex()).getId());
					if (lieky.isEmpty())
						obsah.append("ziadne\n");
					for(Tuple l: lieky)
						obsah.append(l.getElement()+" "+(int)l.getScore()+"\n");
					obsah.append("\nVSETCI:\n");
					lieky = RedisLieky.getStatistikaLiekov(0);
					if (lieky.isEmpty())
						obsah.append("ziadne\n");
					for(Tuple l: lieky)
						obsah.append(l.getElement()+" "+(int)l.getScore()+"\n");
				}
			}
		});
		odhlasitSa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PrihlasovanieGUI prihlasovanieGui = new PrihlasovanieGUI();
				prihlasovanieGui.setVisible(true);
				Gui.this.dispose();
			}
		});
	}
	
		/**
		 * Nacita pacientov do Jlistu
		 * @param pacienti zoznam pacientov
		 */
		public  void nacitajPacientov(List<Pacient> pacienti){
			this.pacienti = pacienti;
			model = new DefaultListModel();
			for (Pacient p: pacienti)
				model.addElement(p.getPriezvisko()+" "+p.getMeno());
			zoznam.setModel(model);
			zoznam.setPreferredSize(new Dimension(150,0));
			skrolZoznam.repaint();
		}
}
