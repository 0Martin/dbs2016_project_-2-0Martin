package Databaza;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Objekty.Doktor;
import Objekty.Poistovna;

public class DatabazaPoistovna {
	
	
	
		/**
		 * Vrati zoznam vsetkych poisovni ulozenych v databaze
		 * @return zoznam poistovni
		 */
		public static List<Poistovna> getAll(){
			Pripojenie p;
			ResultSet rs;
			List<Poistovna> poistovne = new ArrayList();
			Poistovna poistovna;
			
			p=Databaza.pripojDB(true);
			
			try {
				String s = "SELECT * FROM poistovna ORDER BY nazov";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while (rs.next()){
					poistovna = new Poistovna(rs.getInt("id"));
					poistovna.setNazov(rs.getString("nazov"));
					poistovne.add(poistovna);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return poistovne;
			}
		}	

}
