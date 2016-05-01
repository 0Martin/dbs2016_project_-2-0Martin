package Objekty;

import java.sql.Date;

public class Problem {

	private int id;
	private int pacient_id;
	private int doktor_id;
	private int diagnoza_id;
	private String diagnoza;
	private String nazov;
	private Date poslednaNavsteva;
	
	public Problem(int id){
		this.id = id;
	}
	public void setPacient_id(int pacient_id){
		this.pacient_id = pacient_id;
	}
	public void setDoktor_id(int doktor_id){
		this.doktor_id = doktor_id;
	}
	public void setDiagnoza_id(int diagnoza_id){
		this.diagnoza_id=diagnoza_id;
	}
	public void setDiagnoza(String diagnoza){
		this.diagnoza = diagnoza;
	}
	public void setNazov(String nazov){
		this.nazov = nazov;
	}
	public void setPoslednaNavsteva(Date poslednaNavsteva){
		this.poslednaNavsteva = poslednaNavsteva;
	}
	
	public int getId(){
		return id;
	}
	public int getPacient_id(){
		return pacient_id;
	}
	public int getDoktor_id(){
		return doktor_id;
	}
	public int getDiagnoza_id(){
		return diagnoza_id;
	}
	public String getDiagnoza(){
		return diagnoza;
	}
	public String getNazov(){
		return nazov;
	}
	public Date getPoslednaNavsteva(){
		return poslednaNavsteva;
	}
}
