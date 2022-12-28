//$Id$
package com.validators;

import com.connectors.RedisConnector;
import redis.clients.jedis.Jedis;

public class UserRequestLimitValidation {

	public static boolean reqLimitValidation(String ip, String uri) {
		if(!ip.isEmpty()) {
			Jedis jedis = RedisConnector.getRedisConnector();
			String cacheKey = getCacheKey(ip, uri);
			if(!jedis.exists(cacheKey)) {
				deleteAndUpdateUserCache(jedis, cacheKey, (2-1), (5-1), false);
				return true;
			}
			else if(jedis.exists(cacheKey)) {
				String userInfo = getCacheData(cacheKey, jedis);
				if(!userInfo.isEmpty()) {
					String userData[] = userInfo.split(",",3);
					int remainingSecCount = Integer.parseInt(userData[1]);
					int remainingMinCount = Integer.parseInt(userData[2]);
					Long lastReqTime = (System.currentTimeMillis() - Long.parseLong(userData[0]));
					//within second
					if(lastReqTime < 1000 && remainingSecCount > 0 && remainingMinCount > 0) {
						deleteAndUpdateUserCache(jedis, cacheKey, remainingSecCount-1, remainingMinCount-1,true);
						return true;
					}
					else if(lastReqTime > 1000) {
						//within a minute
						if((lastReqTime < 1000*60) && remainingMinCount > 0) {
							deleteAndUpdateUserCache(jedis, cacheKey, 2, remainingMinCount-1, true);
							return true;
						}
						//after a minute
						if(lastReqTime > 1000*60) {
							deleteAndUpdateUserCache(jedis, cacheKey, (2-1), (5-1), true);
							return true;
						}
					}
					else {
						if(remainingSecCount <= 0) {
							System.out.println("You have crossed your throttal limit(sec)");
						}else {
							System.out.println("You have crossed your throttal limit(min)");
						}
					}
				}
			}
		}
		return false;
	}

	private static void deleteAndUpdateUserCache(Jedis jedis, String cacheKey, int perSecCount, int perMinCount, boolean isUpdate) {
		Long currentTime = System.currentTimeMillis();
		String value = currentTime.toString()+","+String.valueOf(perSecCount)+","+String.valueOf(perMinCount);
		try {
			if(isUpdate) {
				jedis.del(cacheKey);
			}
			jedis.set(cacheKey, value);
		}catch(Exception ex) {
			System.out.println("Exception while deleting/putting data in cache");
		}
	}

	private static String getCacheData(String cacheKey, Jedis connector) {
		try {
			return connector.get(cacheKey);
		}catch(Exception ex){
			System.out.println("Exception while getting data from cache");
		}
		return null;
	}
	
	public static String getCacheKey(String ip, String uri) {
		String uriInfo[] = uri.substring(1).split("/");
		String cacheKey = "";
		for(int i=0; i<uriInfo.length; i++) {
			if(i==0) {
				cacheKey += uriInfo[i];
			}else {
				cacheKey += "."+uriInfo[i];
			}
		}
		return cacheKey+"_"+ip;
	}
}
