//$Id$
package leakybucket;

public interface RateLimiter {

	public boolean grandAccess();
}
