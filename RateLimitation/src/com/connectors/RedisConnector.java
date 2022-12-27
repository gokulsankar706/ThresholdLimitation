//$Id$
package com.connectors;

import redis.clients.jedis.Jedis;

public class RedisConnector {

	public static Jedis getRedisConnector() {
		try {
			Jedis jedis = new Jedis("localhost",6379);
			System.out.println("Successfully Connected with redis...");
			return jedis;
		}catch(Exception ex) {
			System.out.println("Exception while making redis connection");
		}
		return null;
	}
}
