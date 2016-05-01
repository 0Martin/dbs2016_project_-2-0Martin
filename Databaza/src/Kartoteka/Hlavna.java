package Kartoteka;

import java.sql.SQLException;
import java.util.*;

import javax.swing.JFrame;

import Databaza.Databaza;
import Databaza.DatabazaDoktor;
import Databaza.DatabazaPacient;
import Databaza.DatabazaPoistovna;
import Objekty.*;
import Redis.RedisLieky;
import redis.clients.jedis.Jedis;
import GUI.*;

public class Hlavna {
	

	public static void main(String[] args)  {
		
		PrihlasovanieGUI prihlasovanieGUI;
		
		prihlasovanieGUI = new PrihlasovanieGUI();
		prihlasovanieGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		prihlasovanieGUI.setVisible(true);
		
	}

	
}
