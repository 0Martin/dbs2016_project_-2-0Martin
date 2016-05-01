package GUI;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import Databaza.Databaza;
import Databaza.DatabazaDoktor;
import Databaza.DatabazaNavsteva;
import Objekty.Navsteva;
import Objekty.PredpisLieku;
import Objekty.Problem;
import Objekty.Riesenie_navstevy;

public class ProblemZobrazitGUI extends JFrame {

	List<Riesenie_navstevy> riesenia;
	List<PredpisLieku> predpisy;
	JScrollPane skrolObsah;
	JTextArea obsah;
	
	public ProblemZobrazitGUI(Problem problem){
		super(problem.getNazov()+" ("+problem.getDiagnoza()+")");
		System.out.println("ZACIATOK");
		setSize(500, 350);
		setLocationRelativeTo(null);
		
		riesenia=DatabazaNavsteva.getAllbyProblemId(problem.getId());
		System.out.println("ZACIATOK2: "+riesenia.size());
		obsah=new JTextArea();
		obsah.setEditable(false);
		for (Riesenie_navstevy rn: riesenia){
			obsah.append("Datum: "+rn.getDatum().toString()+"\n");
			obsah.append("Doktor: "+rn.getDoktor()+"\n");
			obsah.append("Lieky: ");
			if (rn.getPredpisy().size() == 0) //pokial nema riesenie predpisane lieky
				obsah.append("ziadne");
			else{
				for (PredpisLieku pl: rn.getPredpisy())
					obsah.append(pl.getNazov_lieku()+" ks."+pl.getKusov()+", ");
				//ostrani posledne dva znaky teda ", "
				obsah.setText(obsah.getText().substring(0,obsah.getText().length()-2));
			}
			obsah.append("\nPopis: "+rn.getObsah());
			obsah.append("\n\n\n");
		}
		skrolObsah=new JScrollPane(obsah);
		
		
		add(skrolObsah);
	}
}
