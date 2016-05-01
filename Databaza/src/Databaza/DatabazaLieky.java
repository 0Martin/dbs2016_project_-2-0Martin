package Databaza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Objekty.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

public class DatabazaLieky {

	
		/**
		 * Vrati zoznam vsetkych liekov				
		 * @return zoznam liekov
		 */
		public static List<Liek> getAll() {
			Pripojenie p;
			ResultSet rs;
			List<Liek> lieky = new ArrayList<Liek>();
			Liek liek;
							
			p=Databaza.pripojDB(true);
							
			try {
				String s = "SELECT * FROM liek ORDER BY nazov";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while (rs.next()){
					liek = new Liek(rs.getInt("id"));
					liek.setNazov(rs.getString("nazov"));
					liek.setCena(rs.getInt("cena"));
					lieky.add(liek);
					}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
				return lieky;
		}
						}

}
