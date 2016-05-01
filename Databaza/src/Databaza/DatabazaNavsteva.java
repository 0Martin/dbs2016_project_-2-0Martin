package Databaza;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.Statement;

import Objekty.Navsteva;
import Objekty.PredpisLieku;
import Objekty.Problem;
import Objekty.Riesenie_navstevy;
import Redis.Redis;
import Redis.RedisLieky;
import redis.clients.jedis.Jedis;

public class DatabazaNavsteva {

			/**
			 * Vrati vsetky riesenia spolu s naviazanymi predpismi liekov a meno doktora
			 * tykajuce sa danneho problemu
			 * (Podrobnejsi opis v dokumentacii)
			 * @param problem_id id problemu
			 * @return zoznem rieseni_navstev
			 */
			public static List<Riesenie_navstevy> getAllbyProblemId(int problem_id) {
				Pripojenie p;
				ResultSet rs;
				List<Riesenie_navstevy> riesenia = new ArrayList<Riesenie_navstevy>();
				Riesenie_navstevy riesenie = null;
				List<PredpisLieku> predpisy = null;
				PredpisLieku predpis;
				
				p=Databaza.pripojDB(true);
				String s="";
				try {
						s = "SELECT * FROM riesenie_navstevy rn "
							+ "JOIN navsteva n ON rn.navsteva_id=n.id "
							+ "JOIN doktor d ON n.doktor_id=d.id "
							+ "LEFT JOIN predpis_lieku pl ON rn.id=pl.riesenie_navstevy_id "
							+ "LEFT JOIN liek l ON pl.liek_id=l.id "
							+ "WHERE problem_id='"+problem_id+"' "
							+ "ORDER BY datum";
					if (Databaza.pomocneVypisy)
						System.out.println(s);
					rs = p.st.executeQuery(s);
					while(rs.next()){
						//zistuje sa ci ide o nove riesenie problemu
						if (riesenie == null || rs.getInt("rn.id")!=riesenie.getId()){
							if (riesenie != null){
								riesenie.setPredpisy(predpisy); //naviazanie zoznamu predpisov liekov k rieseniu
							}
							predpisy = new ArrayList<PredpisLieku>();
							riesenie = new Riesenie_navstevy(rs.getInt("rn.id"));
							riesenie.setObsah(rs.getString("rn.obsah"));
							riesenie.setDatum(rs.getDate("n.datum"));
							riesenie.setDoktor_id(rs.getInt("n.doktor_id"));
							riesenie.setDoktor(rs.getString("d.meno"));
							riesenia.add(riesenie);
						}
						//pridanie predpisu lieku do zoznamu
						if (rs.getInt("pl.id") != 0){
							System.out.println("CISLO: "+rs.getInt("pl.id"));
							predpis = new PredpisLieku(rs.getInt("pl.id"));
							predpis.setNazov_lieku(rs.getString("l.nazov"));
							predpis.setKusov(rs.getInt("pl.kusov"));
							predpisy.add(predpis);
						}
					}
					riesenie.setPredpisy(predpisy);
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					Databaza.zatvor(p);
					return riesenia;
				}
			}

			/**
			 * Ulozi navstevu pomocu tranzakcie tak ze najprv ulozi navsetvu, 
			 * potom jednotlive riesenie navstevy, ktore na nu naviazu a ak sa tykaju noveho problemu, 
			 * tak predtym este vytvoria ten a na ten sa naviazu. 
			 * Nakoniec pridavaju sa predpisy, ktore sa naviazu na ulozene riesenia.
			 * (Podrobnejsi opis v dokumentacii)
			 * @param problemy zoznam problemov riesenych na navsteve
			 * @param riesenia zoznam rieseni problemov na navsteve
			 * @param doktorId id doktora ktory riesil problemy
			 * @param pacientId id pacienta, ktory bol na navsteve
			 */
			public static void ulozNavstevu(List<Problem> problemy, List<Riesenie_navstevy> riesenia,int doktorId,int pacientId) {
			
				Pripojenie p;
				ResultSet rs;
				Jedis jedis;
				java.sql.PreparedStatement preparedStatement = null;
				String ulozProblemString = "INSERT INTO problem VALUES (0,?,?,?,?);";
				String ulozRiesenieString = "INSERT INTO riesenie_navstevy VALUES (0,?,?,?);";
				String ulozNavstevaString = "INSERT INTO navsteva VALUES(0,?,?,?)";
				String ulozPredpisString = "INSERT INTO predpis_lieku VALUES(0,?,?,?)";
				String updateProblem = "UPDATE problem SET diagnoza_id=? WHERE id=?";
				Problem problem;
				Riesenie_navstevy riesenie;
				List<PredpisLieku> predpisy;
				
				p=Databaza.pripojDB(false);
				try{
					preparedStatement = p.con.prepareStatement(ulozNavstevaString,Statement.RETURN_GENERATED_KEYS);
					Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
					preparedStatement.setTimestamp(1, timestamp);
					preparedStatement.setInt(2, doktorId);
					preparedStatement.setInt(3, pacientId);
					preparedStatement.executeUpdate();
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					int navstevaId = rs.getInt(1);
					for (int i=0; i<problemy.size(); i++){
						problem=problemy.get(i);
						riesenie=riesenia.get(i);
						if (problemy.get(i).getId()==0){
							preparedStatement = p.con.prepareStatement(ulozProblemString,Statement.RETURN_GENERATED_KEYS);
							preparedStatement.setString(1, problem.getNazov());
							preparedStatement.setInt(2, problem.getPacient_id());
							preparedStatement.setInt(3, problem.getDoktor_id());
							if (problem.getDiagnoza_id() == 0 || problem.getDiagnoza()==null)
								preparedStatement.setNull(4, java.sql.Types.INTEGER);
							else
								preparedStatement.setInt(4, problem.getDiagnoza_id());
							System.out.println(preparedStatement);
							preparedStatement.executeUpdate();
							rs = preparedStatement.getGeneratedKeys();
							rs.next();
							riesenie.setProblem_id(rs.getInt(1));
						}
						else{
							preparedStatement = p.con.prepareStatement(updateProblem);
							if  (problemy.get(i).getDiagnoza_id()==0 || problem.getDiagnoza()==null)
								preparedStatement.setNull(1, java.sql.Types.INTEGER);
							else
								preparedStatement.setInt(1, problemy.get(i).getDiagnoza_id());
							preparedStatement.setInt(2, problemy.get(i).getId());
							preparedStatement.executeUpdate();
						}
						preparedStatement = p.con.prepareStatement(ulozRiesenieString,Statement.RETURN_GENERATED_KEYS);
						preparedStatement.setInt(1, riesenie.getProblem_id());
						preparedStatement.setInt(2, navstevaId);
						preparedStatement.setString(3, riesenie.getObsah());
						preparedStatement.executeUpdate();
						rs = preparedStatement.getGeneratedKeys();
						rs.next();
						int riesenie_id = rs.getInt(1);
						predpisy = riesenie.getPredpisy();
						for (PredpisLieku pr: predpisy){
							RedisLieky.PridajPredpis(pacientId, pr.getKusov(), pr.getNazov_lieku(),timestamp);
							//jedis.zadd("poslednyRok:"+Integer.toString(pacientId), pr.getKusov(), pr.getNazov_lieku());	//REDIS
							preparedStatement = p.con.prepareStatement(ulozPredpisString);
							preparedStatement.setInt(1, pr.getKusov());
							preparedStatement.setInt(2, pr.getLiek_id());
							preparedStatement.setInt(3, riesenie_id);
							preparedStatement.executeUpdate();
						}
					}
					p.con.commit();
				} catch (SQLException e ){
					if (p.con!=null)
						try {
							p.con.rollback();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					e.printStackTrace();
				} finally{
					Databaza.zatvor(p);
				}
			}
	
}
