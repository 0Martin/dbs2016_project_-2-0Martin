package Objekty;

public class PredpisLieku {
	private int id;
	private int kusov;
	private int liek_id;
	private String nazov_lieku;
	private int navsteva_id;
	
	public PredpisLieku(int id) {
		this.id = id;
	}
	public void setLiek_id(int liek_id){
		this.liek_id = liek_id;
	}
	public void setNazov_lieku(String nazov_lieku){
		this.nazov_lieku = nazov_lieku;
	}
	public void setNavsteva_id(int navsteva_id){
		this.navsteva_id = navsteva_id;
	}
	public void setKusov(int kusov){
		this.kusov=kusov;
	}
	public int getId(){
		return id;
	}
	public int getLiek_id(){
		return liek_id;
	}
	public String getNazov_lieku(){
		return nazov_lieku;
	}
	public int getNavsteva_id(){
		return navsteva_id;
	}
	public int getKusov(){
		return kusov;
	}
}
