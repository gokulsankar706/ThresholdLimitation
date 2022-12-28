//$Id$
package com.connection;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.connectors.MySqlConnector;
import com.handlers.RequestHandler;
import com.mysql.cj.MysqlConnection;

import redis.clients.jedis.Jedis;

public class CacheConnection extends HttpServlet {

	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		redis();
	}
	
	public static void redis() {
		try {
			//RequestHandler.login("gokul", "gokul@123");
			RequestHandler.signUp("gopinath", "gokul@123");
			Jedis jet = new Jedis("localhost",6379);
			System.out.println("Connected....");
			//System.out.println(jet.info());
			System.out.println("pushing...."+jet.lpush("apple","sweet"));
			System.out.println("getting...."+jet.lpop("apple"));
			
			
			String ip = "0.0.0.1";
			Long currentTime = System.currentTimeMillis();
			int count = 5-1;
			Map<String, String> hash = new HashMap<>();
			
//			JSONObject obj=new JSONObject();    
//			obj.put("time", currentTime.toString());
//			obj.put("count", String.valueOf(count));
			hash.put("time",currentTime.toString());
			hash.put("count", String.valueOf(count));
			
			//jet.hset(ip, hash);
			jet.hset(ip, "time", currentTime.toString());
			jet.hset(ip, "count", String.valueOf(count));
			
//			String tt = currentTime.toString()+","+String.valueOf(count);
//			jet.set(ip,tt);
			String hello = jet.get(ip);
			
			boolean isexist = jet.exists(ip);
			
//			Map<String, String> returnedValue = jet.hgetAll(ip);
//			String t = returnedValue.get("time");
//			int co = Integer.parseInt(returnedValue.get("count"));
//			System.out.println("time "+t+" : "+"count "+co);
				
			System.out.println(isexist);
			
		}catch(Exception ex) {
			System.out.println("Error......."+ex);
		}
	}
}
