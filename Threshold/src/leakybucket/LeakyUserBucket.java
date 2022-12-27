//$Id$
package leakybucket;

import java.util.HashMap;
import java.util.Map;

public class LeakyUserBucket {
	
	Map<String, LeakyBucket> userBucket ;
	
	public LeakyUserBucket(String Ip, Integer limit) {
		this.userBucket = new HashMap<>();
		userBucket.put(Ip, new LeakyBucket(limit));
	}
	
	public boolean userAccess(String Ip) {
		if(userBucket.get(Ip).grandAccess()) {
			return true;
		}
		return false;
	}
}
