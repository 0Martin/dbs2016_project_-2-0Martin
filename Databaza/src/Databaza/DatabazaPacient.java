package Databaza;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Objekty.Doktor;
import Objekty.Pacient;
import Objekty.Poistovna;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class DatabazaPacient {
	
	
		/**
		 * Vrati konkretneho pacienta
		 * @param pacient_id id pacienta
		 * @return pacient
		 */
		public static Pacient getPacient(int pacient_id) {
			Pripojenie p;
			ResultSet rs;
			Pacient pacient = null;
				
			p=Databaza.pripojDB(true);
				
			try {
				String s = "SELECT * FROM pacient WHERE id='"+pacient_id+"'";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while(rs.next()){
					pacient = new Pacient(rs.getInt("id"));
					pacient.setRodne_cislo(rs.getString("rodne_cislo"));
					pacient.setMeno(rs.getString("meno"));
					pacient.setPriezvisko(rs.getString("priezvisko"));
					pacient.setPoistovna_id(rs.getInt("poistovna_id"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return pacient;
			}
		}
		
		/**
		 * Vrati info o konkretnom pacientovi
		 * @param pacient_id id pacienta
		 * @return info o pacientovi
		 */
		public static String getPopis(int pacient_id) {
			Pripojenie p;
			ResultSet rs;
			String vysledok="";
				
			p=Databaza.pripojDB(true);
				
			try {
				String s = "SELECT * FROM pacient pa "
						+ "JOIN poistovna po ON pa.poistovna_id=po.id "
						+ "WHERE pa.id='"+pacient_id+"'";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while(rs.next()){
					vysledok += "ID: "+rs.getInt("pa.id")+"\n";
					vysledok += "Meno: "+rs.getString("pa.meno")+"\n";
					vysledok += "Priezvisko: "+rs.getString("pa.priezvisko")+"\n";
					vysledok += "Rodne cislo: "+rs.getString("pa.rodne_cislo")+"\n";
					vysledok += "Poistovna: "+rs.getString("po.nazov");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return vysledok;
			}
		}

		/**
		 * Vrati zoznam vsetkyc hpacientov
		 * @return zoznam pacientov
		 */
		public static List<Pacient> getAll(){
			Pripojenie p;
			ResultSet rs;
			Pacient pacient;
			List<Pacient> pacienti = new ArrayList<Pacient>();
			
			p=Databaza.pripojDB(true);
			
			try {
				String s = "SELECT * FROM pacient ORDER BY priezvisko";
				rs = p.st.executeQuery(s);
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				while (rs.next()){
					pacient = new Pacient(rs.getInt("id"));
					pacient.setRodne_cislo(rs.getString("rodne_cislo"));
					pacient.setMeno(rs.getString("meno"));
					pacient.setPriezvisko(rs.getString("priezvisko"));
					pacient.setPoistovna_id(rs.getInt("poistovna_id"));
					pacienti.add(pacient);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Databaza.zatvor(p);
				return pacienti;
			}
		}
		
		/**
		 * Vyhlada pacientov v databaze ktorych krstne meno alebo prizvisko 
		 * sa zacina na vyhladavane meno 
		 * @param meno vyhladavany string
		 * @return
		 */
		public static List<Pacient> vyhladajPacientov(String meno){
			Pripojenie p;
			ResultSet rs;
			List<Pacient> pacienti = new ArrayList<Pacient>(); 
				
			p=Databaza.pripojDB(true);

			Pacient pacient;
			String s="";
			try {
				s = "SELECT * FROM pacient "
					+ "WHERE meno LIKE '"+meno+"%' "
					+ "OR priezvisko LIKE '"+meno+"%' "
					+ "ORDER BY priezvisko";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while (rs.next()){
					pacient = new Pacient(rs.getInt("id"));
					pacient.setRodne_cislo(rs.getString("rodne_cislo"));
					pacient.setMeno(rs.getString("meno"));
					pacient.setPriezvisko(rs.getString("priezvisko"));
					pacient.setPoistovna_id(rs.getInt("poistovna_id"));
					pacienti.add(pacient);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return pacienti;
			}
		}
		
			
		/**
		 * Ulozi pacienta do databazy
		 * @param pacient pacient na ulozenie
		 * @return  zoznam vsetkych pacientov po ulozeni
		 */
		public static List<Pacient> pridajPacienta(Pacient pacient) {
			Pripojenie p;
			ResultSet rs;
			
			p=Databaza.pripojDB(true);
			
			try {
				String s="INSERT INTO pacient(id,rodne_cislo,meno,priezvisko,poistovna_id) VALUES ("+
					Integer.toString(pacient.getId())+","+ 			// ID
					pacient.getRodne_cislo()+",'"+		// vek
					pacient.getMeno()+"','"+						// krstne meno
					pacient.getPriezvisko()+"','"+					// priezvisko
					pacient.getPoistovna_id()+"')";					// poistovna(id)
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				p.st.executeUpdate(s);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return getAll();
			}
		}
		
		/**
		 * Pozmeni udaje pacienta
		 * @param pacient pacient s udajmi na ulozanie
		 * @return zoznam vsetkych pacientov po zmene
		 */
		public static List<Pacient> setPacient(Pacient pacient){
			Pripojenie p;
			ResultSet rs;
					
			p=Databaza.pripojDB(true);
					
			try {
				String s = "UPDATE pacient SET "+
						   "meno='"+pacient.getMeno()+"', "+
						   "priezvisko='"+pacient.getPriezvisko()+"', "+
						   "rodne_cislo='"+pacient.getRodne_cislo()+"', "+
						   "poistovna_id='"+pacient.getPoistovna_id()+"' "+
						   "WHERE id='"+pacient.getId()+"'";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				p.st.executeUpdate(s);	
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return getAll();
			}		
		}
		
			/**
			 * Odstani pacient s dannym id
			 * @param pacient_id id pacienta
			 * @return zoznam vsetkych pacientov po odstraneni
			 */
			public static List<Pacient> odstranitPacienta(int pacient_id){
				Pripojenie p;
				ResultSet rs;
				
				p=Databaza.pripojDB(true);
				
				try {
					String s = "DELETE FROM pacient WHERE id = '"+pacient_id+"'";
					if (Databaza.pomocneVypisy)
						System.out.println(s);
					p.st.execute(s);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally{
					Databaza.zatvor(p);
					return getAll();
				}
			}
			

			
			
}
