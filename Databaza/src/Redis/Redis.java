package Redis;

import redis.clients.jedis.Jedis;

public class Redis {

	public static Jedis Pripoj(){
		Jedis jedis = new Jedis("localhost");
		return jedis;
	}
	public static void odpoj(Jedis jedis){
		if(jedis!=null)
			jedis.close();
	}
}
