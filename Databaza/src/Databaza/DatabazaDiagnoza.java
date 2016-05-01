package Databaza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Objekty.Diagnoza;

public class DatabazaDiagnoza {

	/**
	 * Vrati zoznam vsetkych dostupnych diagnoz
	 * (zvacsa vyuzivane v ComboBox na ponuknutie vsetkych moznosti na stanovenie diagnozy)
	 * @return zoznam diagnoz
	 */
	public static List<Diagnoza> getAll() {
		Pripojenie p;
		ResultSet rs;
		List<Diagnoza> diagnozy = new ArrayList<Diagnoza>();
		Diagnoza diagnoza;
		
		p=Databaza.pripojDB(true);
		String s="";
		try {
				s = "SELECT id,nazov FROM  diagnoza   ORDER BY nazov";
			if (Databaza.pomocneVypisy)
				System.out.println(s);
			rs = p.st.executeQuery(s);
			while (rs.next()){
				diagnoza = new Diagnoza(rs.getInt("id"));
				diagnoza.setNazov(rs.getString("nazov"));
				diagnozy.add(diagnoza);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			Databaza.zatvor(p);
			return diagnozy;
		}
	}
	
}
