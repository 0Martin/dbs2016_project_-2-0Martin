package Databaza;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Objekty.Doktor;

public class DatabazaDoktor {
	
		/**
		 * Overuje ci sa doktor s dannym menom nachadza v databaze
		 * @param meno meno doktora
		 * @return id najdeneho doktora so zadanym menom
		 */
		public static int overPrihlasenie(String meno) {
			Pripojenie p;
			ResultSet rs;
			int id=-1;
					
			p=Databaza.pripojDB(true);
					
			try {
				String s = "SELECT id FROM doktor WHERE meno='"+meno+"'";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				if(rs.next())
				id=rs.getInt("id");	
			} catch (SQLException e) {
					e.printStackTrace();
			} finally{
					Databaza.zatvor(p);
					return id;
			}
		}
		
	
}
