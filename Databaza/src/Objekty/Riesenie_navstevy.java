package Objekty;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

public class Riesenie_navstevy {


	private int id;
	private int problem_id;
	private int navsteva_id;
	private int doktor_id;
	private String doktor;
	private String obsah;
	private Date datum;
	private List<PredpisLieku> predpisy;
	
	public Riesenie_navstevy(int id){
		this.id = id;
	}
	public void setProblem_id(int problem_id){
		this.problem_id = problem_id;
	}
	public void setNavsteva_id(int navsteva_id){
		this.navsteva_id = navsteva_id;
	}
	public void setDoktor_id(int doktor_id){
		this.doktor_id = doktor_id;
	}
	public void setDoktor(String doktor){
		this.doktor = doktor;
	}
	public void setObsah(String obsah){
		this.obsah = obsah;
	}
	public void setDatum(Date date){
		this.datum = date;
	}
	public void setPredpisy(List<PredpisLieku> predpisy){
		this.predpisy = predpisy;
	}
	public int getId(){
		return id;
	}
	public int getProblem_id(){
		return problem_id;
	}
	public int getNavsteva_id(){
		return navsteva_id;
	}
	public int getDoktor_id(){
		return doktor_id;
	}
	public String getDoktor(){
		return doktor;
	}
	public String getObsah(){
		return obsah;
	}
	public Date getDatum(){
		return datum;
	}
	public List<PredpisLieku> getPredpisy(){
		return predpisy;
	}
	
}
