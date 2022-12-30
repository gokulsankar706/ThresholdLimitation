//$Id$
package com.validators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.connectors.RedisConnector;
import com.constants.Constants;
import com.constants.Constants.tags;

import redis.clients.jedis.Jedis;

public class UserRequestLimitValidation {

	private static final int second = 1000;
	private static final String LASTREQTIME = "lastreqtime";
	private static Jedis jedis = null;
	public static boolean reqLimitValidation(String ip, String uri) {
		if(!ip.isEmpty()) {
			if(jedis == null) {
				jedis = RedisConnector.getRedisConnector();
			}
			Constants constant = new Constants();
			String[] userData = uri.split("/");
			String URL = userData[userData.length-1];
			HashMap<String, Integer> count = constant.getConstants(URL);
			if(count == null) {
				return true;
			}
			UserRequestLimitValidation cache = new UserRequestLimitValidation();
			String cacheKey = cache.getCacheKey(ip, uri);
			if(!jedis.exists(cacheKey)) {
				createNewCache(cacheKey, count);
				return true;
			}
			else if(jedis.exists(cacheKey)) {
				Map<String, Integer> data = new HashMap<>();
				Map<String, String> cacheData = getCache(cacheKey);
				Long milliSeconds = System.currentTimeMillis() - Long.parseLong(cacheData.get(LASTREQTIME));
				cacheData.remove(LASTREQTIME);
				cacheData.forEach((k,v) -> data.put(k, Integer.parseInt(v)));
				Map<String, Integer> timeLaps = getTimeDIffrence(milliSeconds);
				int seconds = timeLaps.get("seconds");
				int minutes = timeLaps.get("minutes");
				//int hours = timeLaps.get("hours");
				int remainingSecCount = data.get(tags.SECONDCOUNT.getValue());
				int remainingMinCount = data.get(tags.MINUTECOUNT.getValue());

				if(minutes > 0) {
					createNewCache(cacheKey, count);
					return true;
				}
				else if(seconds > 0 && remainingMinCount > 0){
					updateCache(cacheKey, count.get(tags.SECONDCOUNT.getValue()), remainingMinCount-1);
					return true;
				}
				else if(milliSeconds < second && remainingSecCount > 0 && remainingMinCount > 0) {
					deleteAndUpdate(cacheKey, data);
					return true;
				}
			}
			else {
				System.out.println("Somthing Wrong....");
			}
		}
		return false;
	}

	private static Map<String, Integer> getTimeDIffrence(Long milliSeconds) {
		int seconds = (int) (milliSeconds / 1000);
		int hours = seconds / 3600;
		int minutes = (seconds % 3600) / 60;
		seconds = (seconds % 3600) % 60;

		Map<String, Integer> timeLaps = new HashMap<>();
		timeLaps.put("seconds",seconds);
		timeLaps.put("minutes",minutes);
		timeLaps.put("hours",hours);

		return timeLaps;
	}

	private static void deleteAndUpdate(String cacheKey, Map<String,Integer> remainingCounts) {
		Map<String, String> cacheData = new HashMap<>();
		if(remainingCounts.get(LASTREQTIME) != null) {
			remainingCounts.remove(LASTREQTIME);
		}
		remainingCounts.forEach((k,v) -> cacheData.put(k, String.valueOf(v-1)));
		cacheData.put(LASTREQTIME, String.valueOf(System.currentTimeMillis()));
		
		try {
			deleteCache(cacheKey);
			jedis.hset(cacheKey, cacheData);
		}catch(Exception ex) {
			System.out.println("Exception while deleting/putting data in cache"+ex);
		}
	}

	private static void createNewCache(String cacheKey, HashMap<String, Integer> defaultCounts){
		Map<String, String> cacheData = new HashMap<>();
		defaultCounts.forEach((k,v) -> cacheData.put(k, String.valueOf(v)));
		cacheData.put(LASTREQTIME, String.valueOf(System.currentTimeMillis()));
		
		try {
			deleteCache(cacheKey);
			jedis.hset(cacheKey, cacheData);
		}catch(Exception ex) {
			System.out.println("Exception while updating data in cache");
		}
	}

	private static void deleteCache(String cacheKey) {
		Set<String> hashKeys = jedis.hkeys(cacheKey);
		for(String field: hashKeys) {
			jedis.hdel(cacheKey, field);
		}
	}
	
	private static void updateCache(String cacheKey, int secCount, int minCount) {
		Map<String, String> cacheData = new HashMap<>();
		cacheData.put(LASTREQTIME, String.valueOf(System.currentTimeMillis()));
		cacheData.put(tags.SECONDCOUNT.getValue(), String.valueOf(secCount));
		cacheData.put(tags.MINUTECOUNT.getValue(), String.valueOf(minCount));
		try {
		deleteCache(cacheKey);
		jedis.hset(cacheKey, cacheData);
		}catch(Exception ex) {
			System.out.println("Exception while updating cache "+ex);
			}
	}
	
	private static Map<String, String> getCache(String cacheKey) {
		try {
			return jedis.hgetAll(cacheKey);
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
