package Objekty;
	

public class Diagnoza {
		
	private int id;
	private String nazov;
	private int pocet;
	
	public Diagnoza(int id){
		this.id = id;
	}
	public void setNazov(String nazov){
		this.nazov=nazov;
	}
	public void setPocet(int vyliecenost){
		this.pocet = vyliecenost;
	}
	
	public int getId(){
		return id;
	}
	public String getNazov(){
		return nazov;
	}
	public int getPocet(){
		return pocet;
	}
}
