package Objekty;

public class Poistovna {
	private int id;
	private String nazov;
	
	public Poistovna(int id){
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
