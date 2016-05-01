package Redis;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Set;

import Databaza.Databaza;
import Databaza.Pripojenie;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.Tuple;

public class RedisLieky {

	/**
	 * Zisti vsetky lieky ktore boli predpisane a ich pocet
	 * @param pacient_id id pacienta v pripade 0 znamena vsetci
	 * @return vysledok vo forme String
	 */
	public static Set<Tuple> getStatistikaLiekov(int pacient_id) {
		Jedis jedis = Redis.Pripoj();
		Set<Tuple> vysledok;
		
		
		vysledok =  jedis.zrevrangeWithScores(Integer.toString(pacient_id), 0, -1);
		Redis.odpoj(jedis);
		return vysledok;
	}
	
	/**
	 * Vrati zoznam liekov uzivanzch paciento od obdobia v timestamp
	 * @param pacientId ID pacienta
	 * @param timestamp cas odkedy sa maju zapocitavat lieky
	 * @return zoznam liekov
	 */
	public static Set<String> getLiekyZaObdobie(int pacientId, Timestamp timestamp){
		Jedis jedis = Redis.Pripoj();
		Set<String> vysledok;
		
		int score = timestamp.getYear()+1899;
	    score*=100;
	    score+=(timestamp.getMonth()+1); //dvojmiestne
	    score*=100;
	   score+=timestamp.getDate(); //dvojmiestne
		
	   vysledok = jedis.zrangeByScore("liekyZaRok:"+Integer.toString(pacientId), Integer.toString(score), "+inf");
		Redis.odpoj(jedis);
		return vysledok;
	}
	
	/**
	 * Prida predpis do Redisu ako viacero zazanmov cez tranzakciu
	 * (statistika pre jednotlivca, statistika pre vsetkych, uzivane lieky za dane obdobie)
	 * @param pacientId ID pacienta
	 * @param pocetKusov pocet kusov lieku
	 * @param nazovLieku nazov lieku
	 * @param timestamp kedy bol liek predpisany
	 */
	public static void PridajPredpis(int pacientId, int pocetKusov, String nazovLieku,Timestamp timestamp){
		Jedis jedis = Redis.Pripoj();
		Transaction tranzakcia = jedis.multi();
		
		try{
			tranzakcia.zincrby(Integer.toString(pacientId), pocetKusov, nazovLieku); // uzivane lieky konkretnym pacientom
			tranzakcia.zincrby(Integer.toString(0), pocetKusov, nazovLieku); // vsetci pacienti uzivane lieky a pocet
		
			int score = timestamp.getYear()+1900;
		    	score*=100; //posun o dve miesta (2016 -> 201600)
		    	score+=(timestamp.getMonth()+1);
		    	score*=100; //posun o dve miesta (201604 -> 20160400)
		    	score+=timestamp.getDate();
		    tranzakcia.zadd("liekyZaRok:"+Integer.toString(pacientId), score, nazovLieku); // uzivane lieky pacientom s casom predpisu
		
		    tranzakcia.exec();
		}catch (RuntimeException e){
		    tranzakcia.discard();
		    e.printStackTrace();
		}finally{
			Redis.odpoj(jedis);
		}
	}
	

	
}
