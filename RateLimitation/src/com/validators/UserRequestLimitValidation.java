//$Id$
package com.validators;

import java.util.HashMap;
import com.connectors.RedisConnector;
import com.constants.Constants;
import com.constants.Constants.tags;

import redis.clients.jedis.Jedis;

public class UserRequestLimitValidation {

	private static final int minute= 1000*60;
	private static final int second = 1000;
	public static boolean reqLimitValidation(String ip, String uri) {
		if(!ip.isEmpty()) {
				Jedis jedis = RedisConnector.getRedisConnector();
				
//				Map<String, String> hash = new HashMap<>();
//				hash.put("hello","test");
//				hash.put("asd","saas");
//				hash.put("ads","sade");
//				
//				try {
//				jedis.hset("user_1", hash);
//				}catch(Exception ex) {
//					System.out.println("Exc..."+ex);
//				}
				
				Constants constant = new Constants();
				HashMap<String, Integer> count = constant.getConstants("login");
				UserRequestLimitValidation cache = new UserRequestLimitValidation();
				String cacheKey = cache.getCacheKey(ip, uri);
				if(!jedis.exists(cacheKey)) {
					cache.updateCache(jedis, cacheKey, (count.get(tags.SECONDCOUNT.getValue())-1), (count.get(tags.MINUTECOUNT.getValue())-1));
					return true;
				}
				else if(jedis.exists(cacheKey)) {
					String userInfo = cache.getCacheData(cacheKey, jedis);
					if(!userInfo.isEmpty()) {
						String userData[] = userInfo.split(",",3);
						int remainingSecCount = Integer.parseInt(userData[1]);
						int remainingMinCount = Integer.parseInt(userData[2]);
						Long lastReqTimeGap = (System.currentTimeMillis() - Long.parseLong(userData[0]));
						//within second
						if(lastReqTimeGap < second && remainingSecCount > 0 && remainingMinCount > 0) {
							cache.deleteAndUpdateCache(jedis, cacheKey, remainingSecCount-1, remainingMinCount-1);
							return true;
						}
						else if(lastReqTimeGap > second) {
							//within a minute
							if((lastReqTimeGap < minute) && remainingMinCount > 0) {
								cache.deleteAndUpdateCache(jedis, cacheKey, remainingSecCount, remainingMinCount-1);
								return true;
							}
							//after a minute
							if(lastReqTimeGap > minute) {
								cache.deleteAndUpdateCache(jedis, cacheKey, remainingSecCount-1, remainingMinCount-1);
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

	private void deleteAndUpdateCache(Jedis jedis, String cacheKey, int perSecCount, int perMinCount) {
		Long currentTime = System.currentTimeMillis();
		String value = currentTime.toString()+","+String.valueOf(perSecCount)+","+String.valueOf(perMinCount);
		try {
			jedis.del(cacheKey);
			jedis.set(cacheKey, value);
		}catch(Exception ex) {
			System.out.println("Exception while deleting/putting data in cache");
		}
	}

	private void updateCache(Jedis jedis, String cacheKey, int perSecCount, int perMinCount) {
		Long currentTime = System.currentTimeMillis();
		String value = currentTime.toString()+","+String.valueOf(perSecCount)+","+String.valueOf(perMinCount);
		try {
			jedis.set(cacheKey, value);
		}catch(Exception ex) {
			System.out.println("Exception while updating data in cache");
		}
	}
	private String getCacheData(String cacheKey, Jedis connector) {
		try {
			return connector.get(cacheKey);
		}catch(Exception ex){
			System.out.println("Exception while getting data from cache");
		}
		return null;
	}

	private String getCacheKey(String ip, String uri) {
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
