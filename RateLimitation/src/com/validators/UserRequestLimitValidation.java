//$Id$
package com.validators;

import com.connectors.RedisConnector;

import redis.clients.jedis.Jedis;

public class UserRequestLimitValidation {

	public static void reqLimitValidation(String Ip) {
		RedisConnector connector = new RedisConnector();
		if(!Ip.isEmpty()) {
			Jedis jedis = connector.getRedisConnector();
			if(!jedis.exists(Ip)) {
				deleteAndUpdateUserCache(jedis, Ip, 4, false);
				//Should handle allow
			}
			else if(jedis.exists(Ip)) {
				String userInfo = jedis.get(Ip);
				String userData[] = userInfo.split(userInfo,2);
				int count = Integer.parseInt(userData[0]);
				Long lastVistTime = (System.currentTimeMillis() - Long.parseLong(userData[1]));
				if(lastVistTime < 1000 && count > 0) {
					deleteAndUpdateUserCache(jedis, Ip, count-1, true);
					//Should handle allow
				}
				else if(lastVistTime > 1000) {
					deleteAndUpdateUserCache(jedis, Ip, 4, true);
				}
				else {
					//should handle
				}
			}
		}
	}

	private static void deleteAndUpdateUserCache(Jedis jedis, String Ip, int count, boolean isUpdate) {
		Long currentTime = System.currentTimeMillis();
		String value = currentTime.toString()+","+String.valueOf(count-1);
		if(isUpdate) {
			jedis.del(Ip);
		}
		jedis.set(Ip, value);
	}
}
