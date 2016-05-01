package Objekty;

import java.sql.Date;
import java.sql.Timestamp;

public class Navsteva {
	
	private int id;
	private String obsah;
	private Timestamp datum;
	private int doktor_id;
	private int pacient_id;
	private int zaznam_id;
	
	public Navsteva(int id){
		this.id = id;
	}
	public void setObsah(String obsah){
		this.obsah = obsah;
	}
	public void setDaatum(Timestamp date){
		this.datum = date;
	}
	public void setDoktor_id(int doktor_id){
		this.doktor_id = doktor_id;
	}
	public void setPacientId(int pacient_id) {
		this.pacient_id = pacient_id;
	}
	public void setZaznam_id(int zaznam_id){
		this.zaznam_id = zaznam_id;
	}
	public int getId(){
		return id;
	}
	public String getObsah(){
		return obsah;
	}
	public Timestamp getDaatum(){
		return datum;
	}
	public int getDoktor_id(){
		return doktor_id;
	}
	public int getPcientId(){
		return pacient_id;
	}
	public int getZaznam_id(){
		return zaznam_id;
	}

}
