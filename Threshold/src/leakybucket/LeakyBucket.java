//$Id$
package leakybucket;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class LeakyBucket implements RateLimiter{

	BlockingQueue<Integer> reqQueue;
	
	public LeakyBucket(int limit) {
		this.reqQueue = new LinkedBlockingQueue<>(limit);
	}
		
	@Override
	public boolean grandAccess() {
		if(reqQueue.remainingCapacity() >0) {
			reqQueue.add(1);
			return true;
		}
		return false;
	}

}
