package Databaza;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import Objekty.Diagnoza;
import Objekty.Problem;

public class DatabazaProblem {

		/**
		* Vrati zoznam diagnoz ktore boli pacientovi uz niekedy diagnostikovane
		* (diagnozy ulozenych zdravotnych problem pacienta) spolu s poctom vyskytu
		* @param pacient_id id pacienta
		* @return zoznam diagnoz
		*/
		public static List<Diagnoza> getDiagnozyByPacientId(int pacient_id) {
			Pripojenie p;
			ResultSet rs;
			List<Diagnoza> diagnozy = new ArrayList<Diagnoza>();
			Diagnoza diagnoza;
					
			p=Databaza.pripojDB(true);
			String s="";
			
			try {
				s = "SELECT d.id,d.nazov,COUNT(*) AS pocet "
				+ "FROM problem p "
				+ "LEFT JOIN diagnoza d ON p.diagnoza_id=d.id "
				+ "WHERE pacient_id='"+pacient_id+"' "
				+ "GROUP BY d.nazov,d.id ORDER BY d.nazov";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while (rs.next()){
					diagnoza = new Diagnoza(rs.getInt("d.id"));
					if (rs.getInt("d.id")==0)
						diagnoza.setNazov("neurcena");
					else
						diagnoza.setNazov(rs.getString("d.nazov"));
					diagnoza.setPocet(rs.getInt("pocet"));
					diagnozy.add(diagnoza);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Databaza.zatvor(p);
				return diagnozy;
			}
		}
		
		/**
		 * Vrati zoradeny zoznam problemov pacienta podla pacient_id a diagnoza_id
		 * @param pacient_id id pacienta
		 * @param diagnoza_id iba pre konkretnu diagnozu s id diagnoza_id, ak 0 tak bez urcenej diagnozy(null) ,ak -1 tak vsetky
		 * @param zoradit ak 1 zoradit podla nazvu, ak 2 zoradit podla poslednej navsetvy
		 * @param filter 
		 * @return zoznam problemov pacienta
		 */
		public static List<Problem> getAllbyPacientId(int pacient_id,int diagnoza_id,int zoradit, String filter) {
			Pripojenie p;
			ResultSet rs;
			List<Problem> problemy = new ArrayList<Problem>();
			Problem problem;
			String diagnoza_idWhere=""; //ak diagnoza_id == -1 tak toto ostava nezmenene
			String zoraditPodla="";
			
			p=Databaza.pripojDB(true);
			String s="";
			try {
				if (diagnoza_id==0) //bez urcenej diagnozy
					diagnoza_idWhere="AND d.id IS NULL";
				else if (diagnoza_id != -1) //hlada len pre konkretnu diagnozu
					diagnoza_idWhere="AND d.id='"+diagnoza_id+"'";
				if (zoradit == 1)
					zoraditPodla="ORDER BY p.nazov";
				else if (zoradit == 2)
					zoraditPodla="ORDER BY naj DESC";
				if (filter != "")
					filter="AND p.id NOT IN ( "+filter+" ) ";
					s = "SELECT p.id,p.diagnoza_id,p.nazov,p.pacient_id,p.doktor_id,d.id,d.nazov,MAX(n.datum) as naj "
							+ "FROM problem p "
							+ "LEFT JOIN riesenie_navstevy rn ON p.id=rn.problem_id "
							+ "LEFT JOIN navsteva n ON rn.navsteva_id=n.id "
							+ "LEFT JOIN diagnoza d ON p.diagnoza_id=d.id "
							+ "WHERE  p.pacient_id='"+pacient_id+"' "+diagnoza_idWhere+" "+filter+" "
							+ "GROUP BY p.id "+zoraditPodla;
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				rs = p.st.executeQuery(s);
				while (rs.next()){
					problem = new Problem(rs.getInt("p.id"));
					if (rs.getInt("d.id")==0)
						problem.setDiagnoza("neurcena");
					else
						problem.setDiagnoza(rs.getString("d.nazov"));
					problem.setDiagnoza_id(rs.getInt("p.diagnoza_id"));
					problem.setNazov(rs.getString("p.nazov"));
					problem.setPoslednaNavsteva(rs.getDate("naj"));
					problemy.add(problem);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				Databaza.zatvor(p);
				return problemy;
			}
		}
		

		/**
		 * Odstrani konkretny problem
		 * (s nim aj ostatne informacie ako riesenia_problemu, predpisy_liekov z dovodu constrainu)
		 * @param problem_id id problemu
		 */
		public static void deleteProblem(int problem_id) {
			Pripojenie p;
			ResultSet rs;
			
			p=Databaza.pripojDB(true);
			
			try {
				String s = "DELETE FROM problem WHERE id='"+problem_id+"'";
				if (Databaza.pomocneVypisy)
					System.out.println(s);
				p.st.execute(s);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally{
				Databaza.zatvor(p);
			}
		}

}
