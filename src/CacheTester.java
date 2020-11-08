import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 * Code to test an <tt>LRUCache</tt> implementation.
 */
public class CacheTester {
	@Test
	public void leastRecentlyUsedIsCorrect () {
		DataProvider<Integer,String> provider = new SquareProvider(); // Need to instantiate an actual DataProvider
		Cache<Integer,String> cache = new LRUCache<Integer,String>(provider, 5);
		for (int i = 0; i < 5; i++) {	//fill cache
			cache.get(i);
		}
		cache.get(5);	//should cause an eviction of the key
		int missesBefore = cache.getNumMisses();
		cache.get(0);	//if 0 was properly evicted should increase the misses
		assertTrue(cache.getNumMisses() > missesBefore);
	}
}
