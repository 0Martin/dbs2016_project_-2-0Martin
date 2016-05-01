package Objekty;

public class Doktor {

	private int id;
	private String meno;
	private String specializacia;
	private int nemocnica_id;
	
	public Doktor(int id){
		this.id = id;
	}
	public void setMeno(String meno){
		this.meno = meno;
	}
	public void setSpecializacia(String specializacia){
		this.specializacia = specializacia;
	}
	public void setNemocnica_id(int nemocnica_id){
		this.nemocnica_id = nemocnica_id;
	}
	public int getId(){
		return id;
	}
	public String getMeno(){
		return meno;
	}
	public String getSpecializacia(){
		return specializacia;
	}
	public int getNemocnica_id(){
		return nemocnica_id;
	}
}
