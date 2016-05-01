package Objekty;

public class Liek {
	private int id;
	private String nazov;
	private int cena;
	
	public Liek(int id){
		this.id = id;
	}
	public void setNazov(String nazov){
		this.nazov = nazov;
	}
	public void setCena(int cena){
		this.cena = cena;
	}
	public int getId(){
		return id;
	}
	public String getNazov(){
		return nazov;
	}
	public int getCena(){
		return cena;
	}
}
