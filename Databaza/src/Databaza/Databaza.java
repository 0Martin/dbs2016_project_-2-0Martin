package Databaza;

import java.sql.*;
import java.util.*;

import Objekty.*;
import redis.clients.jedis.Jedis;

public class Databaza {

		static boolean pomocneVypisy = true;

		/**
		 * metoda  pre pripojenie k databaze
		 * @param autoComit ci sa ma vykonavat auto commit
		 * @return  vracia objekt Pripojenie ktore sluzi na ulozenie oboch premennych Connection a Statement
		 */
		public static Pripojenie pripojDB(boolean autoComit){
			Connection con = null;
			Statement st = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection("jdbc:mysql://localhost:3306/nove","root","");
				con.setAutoCommit(autoComit);
				st = con.createStatement();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return new Pripojenie(con,st);
		}
		 
		/**
		 * metoda pre zatvorenie spojenia k databaze
		 * @param pripojenie pripojenie na zatvorenie
		 */
		 public static void zatvor(Pripojenie pripojenie){
			 try {
				if (pripojenie.st != null)
					pripojenie.st.close();
				if (pripojenie.con != null)
					pripojenie.con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		 }

}
