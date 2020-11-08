import java.util.HashMap;

/**
 * An implementation of <tt>Cache</tt> that uses a least-recently-used (LRU)
 * eviction policy.
 */
public class LRUCache<T, U> implements Cache<T, U> {
	int numMisses;
	int capacity;
	DataProvider<T, U> provider;
	Node start;
	Node end;
	HashMap<T, Node> hashMap;

	/**
	 * @param provider the data provider to consult for a cache miss
	 * @param capacity the exact number of (key,value) pairs to store in the cache
	 */
	public LRUCache (DataProvider<T, U> provider, int capacity) {
		this.provider = provider;
		this.numMisses = 0;
		this.capacity = capacity;
		this.hashMap = new HashMap<>(capacity);

	}

	/**
	 * Returns the value associated with the specified key.
	 * @param key the key
	 * @return the value associated with the key
	 */
	public U get (T key) {
		Node result = hashMap.get(key);
		if (result == null) {	//Cache miss
			this.numMisses++;
			U item = provider.get(key);
			Node newNode = new Node(key, item);	//Construct the new node to be added

			if (hashMap.size() == capacity) {	//Cache full, eviction needed
				this.hashMap.remove(end.key);
				this.removeEndNode();
			}
			this.prependNode(newNode);
			this.hashMap.put(key, newNode);
			return item;
		} else {	//item exists in cache, update linked list order and return item
			this.removeNode(result);
			this.prependNode(result);
			return result.item;
		}
	}

	/**
	 * Returns the number of cache misses since the object's instantiation.
	 * @return the number of cache misses since the object's instantiation.
	 */
	public int getNumMisses () {
		return this.numMisses;
	}

	/**
	 * Removes the given node from the doubly linked list and updates pointers
	 * @param n the node to remove
	 */
	private void removeNode(Node n) {
		if (n.left != null) {
			n.left.right = n.right;
		} else {
			start = n.right;
		}
		if (n.right != null) {
			n.right.left = n.left;
		} else {
			end = n.left;
		}
	}

	/**
	 * Removes the last node in the linked list, Slight optimization as this operation happens every eviction, and for specifically this case we can assume that
	 * the right pointer of the target node is equal to null
	 */
	private void removeEndNode() {
		if (end.left != null) {
			end.left.right = end.right;
		} else {
			start = end.right;
		}
		end = end.left;
	}

	/**
	 * Adds given node to the start of the doubly linked list
	 * @param n the node to add
	 */
	private void prependNode(Node n) {
		n.left = null;
		n.right = start;
		if (start == null) {
			start = n;
		} else {
			start.left = n;
		}
		if (end == null) {
			end = n;
		}
	}

	//Node for establishing a doubly linked list, in order to keep track of the LRU
	private class Node {
		public Node left;
		public Node right;
		public T key;
		public U item;

		public Node (T key, U item) {
			this.key = key;
			this.item = item;
		}
	}
}
