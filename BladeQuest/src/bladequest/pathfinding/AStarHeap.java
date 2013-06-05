package bladequest.pathfinding;


//implements a Pairing Heap.  Wikipedia it, asshole.
public class AStarHeap {

	AStarNode auxBuffer;
	AStarNode headNode;  
	public AStarHeap() 
	{
	}
	void insert(AStarNode node)
	{
		//do NOT NOT NOT NOT NOTN NOTNOT NTONTONT change child data here!  Unless you like failure
		if (auxBuffer == null)
		{
			auxBuffer = node;
			return;
		}
		else
		{
			auxBuffer.prev = node;
			node.next = auxBuffer;
			auxBuffer = node;
		}
	}
	void unlink(AStarNode node)
	{
		//doesn't unlink children!
		if (node.prev != null)
		{
			if (node.prev.child == node) //parent is prev
			{
				node.prev.child = node.next;
			}
			else
			{
				node.prev.next = node.next;
			}			
		}
		if (node.next != null)
		{
			node.next.prev = node.prev;
		}
	}
	void makeChild(AStarNode left, AStarNode right)
	{
		unlink(right);
		if (left.child == null)
		{
			left.child = right;
			right.next = null;
			right.prev = left;
			return;
		}
		else
		{
			left.child.prev = right;
			right.next = left.child;
			right.prev = left;
			left.child = right;
		}
	}
	AStarNode merge(AStarNode left, AStarNode right) 
	{
		if (left == null) return right;
		if (right == null) return left;
		 
		if (left.score < right.score) 
		{
			makeChild(left, right);
			return left;
		}
		else
		{
			makeChild(right, left);
			return right;
		}
	}
	AStarNode peek()
	{
		//don't do auxbuffer here, not needed
		return headNode;
	}
	AStarNode mergePairs(AStarNode firstChild)
	{
		AStarNode next = firstChild.next;
		if (next == null) return firstChild;
		AStarNode tail = next.next;
		AStarNode merged = merge(firstChild, next);
		if (tail != null) tail = mergePairs(tail);
		return merge(merged, tail);
	}
	AStarNode pop()
	{
		if (auxBuffer != null)
		{
			headNode = merge(headNode, mergePairs(auxBuffer));
			auxBuffer = null;
		}
		if (headNode == null) return null;
		AStarNode out = headNode;
		if (out.child != null)
		{
			out.child.prev = null;
			insert(mergePairs(out.child));
			out.child = null;
		}
		headNode = null;		
		return out;
	}
	void decreaseKey(AStarNode node)
	{
		if (node == headNode)
		{
			return; //easy cheesy
		}
		//fairly cheesy easy peasy etc etc
		unlink(node);
		insert(node);
	}
}
