package Objekty;

public class Nemocnica {

	private int id;
	private String nazov;
	
	public Nemocnica(int id){
		this.id = id;
	}
	public void setNazov(String nazov){
		this.nazov = nazov;
	}
	public int getId(){
		return id;
	}
	public String getNazov(){
		return nazov;
	}
}
