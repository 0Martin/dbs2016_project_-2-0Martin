package Databaza;

import java.sql.Connection;
import java.sql.Statement;

import redis.clients.jedis.Jedis;

public class Pripojenie {

	 Connection con;
	 Statement st;

	public Pripojenie(Connection con, Statement st){
		this.con=con;
		this.st=st;
	}
	public void Zatvor(Jedis jedis){
		jedis.close();
	}
}
