package Objekty;

public class Pacient {
	private int id;
	private String rodne_cislo;
	private String meno;
	private String priezvisko;
	private int poistovna_id;
	
	public Pacient(int id){
		this.id = id;
	}
	public void setRodne_cislo(String rodne_cislo){
		this.rodne_cislo = rodne_cislo;
	}
	public void setMeno(String meno){
		this.meno = meno;
	}
	public void setPriezvisko(String priezvisko){
		this.priezvisko = priezvisko;
	}
	public void setPoistovna_id(int poistovna_id){
		this.poistovna_id = poistovna_id;
	}
	public int getId(){
		return id;
	}
	public String getRodne_cislo(){
		return rodne_cislo;
	}
	public String getMeno(){
		return meno;
	}
	public String getPriezvisko(){
		return priezvisko;
	}
	public int getPoistovna_id(){
		return poistovna_id;
	}
}
