/**
 * D-Heap
 */

public class DHeap
{
	
    private int size, max_size, d;
    private DHeap_Item[] array;

	// Constructor
	// m_d >= 2, m_size > 0
    
    DHeap(int m_d, int m_size) 
    {
               max_size = m_size;
			   d = m_d;
               array = new DHeap_Item[max_size];
               size = 0;
    }
	
	/**
	 * public int getSize()
	 * Returns the number of elements in the heap.
	 */
	public int getSize() 
	{
		return size;
	}
	
  /**
     * public int arrayToHeap()
     *
     * The function builds a new heap from the given array.
     * Previous data of the heap should be erased.
     * preconidtion: array1.length() <= max_size
     * postcondition: isHeap()
     * 				  size = array.length()
     * Returns number of comparisons along the function run. 
	 */
    public int arrayToHeap(DHeap_Item[] array1) 
    {
    	this.size=array1.length;
    	if(this.size == 0)
    	{
    		return 0;
    	}
    	setArray(array1);
    	int sP = parent(array1.length-1 , this.d);
    	int countComp = 0;
    	for(int i=sP; i>=0; i--)
    	{
    		countComp += heapifyDown(array1[i]);    		
    	}
        return countComp;
    }
    
    private void setArray(DHeap_Item[] array1)
    {
    	for(int i=0; i<array1.length; i++)
    	{
    		array1[i].setPos(i);
    		this.array[i]=array1[i];
    	}
    }

    private int heapifyDown(DHeap_Item item) 
    {
    	int[] res = minChildIndex(item);
    	int minChildInd = res[0];
    	int count = res[1];
    	boolean keepHipifying = (minChildInd>0);
    	while(keepHipifying)
    	{
    		switchItems(item, array[minChildInd]);
    		res=minChildIndex(item);
    		minChildInd=res[0];
    		count+=res[1];
    		keepHipifying = (minChildInd>0);
    	}
    	return count;
    }

	private int[] minChildIndex(DHeap_Item item) 
	{
		int pos=item.getPos();
		int min = item.getKey();
		int minInd =0;
		int numOfComp=this.d;
		int child;
		for(int i=1; i<=this.d; i++)
		{
			child = child(pos,i,this.d);
			if(child>=this.size)
			{
				numOfComp=i-1;
				break;
			}
			else
			{
				if(this.array[child].getKey()<min)
				{
					min=this.array[child].getKey();
					minInd=child;
				}
			}
		}
		int[] res=new int[] {minInd,numOfComp};
		return res;
	}
	
	
	private void switchItems(DHeap_Item item1, DHeap_Item item2) 
	{
		this.array[item1.getPos()]=item2;
		this.array[item2.getPos()]=item1;
		int tempPos = item1.getPos();
		item1.setPos(item2.getPos());
		item2.setPos(tempPos);
	}

	/**
     * public boolean isHeap()
     *
     * The function returns true if and only if the D-ary tree rooted at array[0]
     * satisfies the heap property or has size == 0.
     *   
     */
    public boolean isHeap() 
    {
    	int lP = parent(this.size-1,this.d);
    	for(int i=0; i<=lP; i++)
    	{
    		if(!validParent(this.array[i]))
    		{
    			return false;
    		}
    	}
        return true;
    }
    
    private boolean validParent(DHeap_Item item) 
    {
    	int pos = item.getPos();
    	int child;
		for(int i=1; i<=this.d ; i++)
		{
			child = child(pos,i,this.d);
			if(child>=this.size)
			{
				break;
			}
			else
			{
				if(item.getKey()>this.array[child].getKey())
				{
					return false;
				}
			}
		}
		return true;
	}

/**
     * public static int parent(i,d), child(i,k,d)
     * (2 methods)
     *
     * precondition: i >= 0, d >= 2, 1 <= k <= d
     *
     * The methods compute the index of the parent and the k-th child of 
     * vertex i in a complete d-ary tree stored in an array. 
     * Note that indices of arrays in Java start from 0.
     */
    public static int parent(int i, int d) {return (int)(i-1)/d;}
    public static int child (int i, int k, int d) {return (d*i)+k;}

    /**
    * public int Insert(DHeap_Item item)
    *
	* Inserts the given item to the heap.
	* Returns number of comparisons during the insertion.
	*
    * precondition: item != null
    *               isHeap()
    *               size < max_size
    * 
    * postcondition: isHeap()
    */
    public int Insert(DHeap_Item item) 
    {
    	this.array[this.size]=item;
    	item.setPos(this.size);
    	this.size++;
    	int countComp = heapifyUp(item);
    	return countComp;
    }
    
    private int heapifyUp(DHeap_Item item) 
    {
    	int countComp = 0;
    	int pos = item.getPos();
    	int parent = parent(pos,this.d);
    	while(!(pos==0 && parent==0))
    	{
    		if(item.getKey()<array[parent].getKey())
    		{
    			switchItems(item,array[parent]);
    			countComp++;
    			pos=item.getPos();
    			parent=parent(pos,this.d);
    		}
    		else
    		{
    			countComp++;
    			break;
    		}
    	}
    	return countComp;
    }

 /**
    * public int Delete_Min()
    *
	* Deletes the minimum item in the heap.
	* Returns the number of comparisons made during the deletion.
    * 
	* precondition: size > 0
    *               isHeap()
    * 
    * postcondition: isHeap()
    */
    public int Delete_Min()
    {
     	switchItems(this.array[0],this.array[this.size-1]);
     	this.size--;
     	int countComp = heapifyDown(this.array[0]);
    	return countComp;
    }


    /**
     * public DHeap_Item Get_Min()
     *
	 * Returns the minimum item in the heap.
	 *
     * precondition: heapsize > 0
     *               isHeap()
     *		size > 0
     * 
     * postcondition: isHeap()
     */
    public DHeap_Item Get_Min()
    {
    	return this.array[0];
    }
	
  /**
     * public int Decrease_Key(DHeap_Item item, int delta)
     *
	 * Decerases the key of the given item by delta.
	 * Returns number of comparisons made as a result of the decrease.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Decrease_Key(DHeap_Item item, int delta)
    {
    	item.setKey(item.getKey()-delta);
    	int countComp = heapifyUp(item);
    	return countComp;
    }
	
	/**
     * public int Delete(DHeap_Item item)
     *
	 * Deletes the given item from the heap.
	 * Returns number of comparisons during the deletion.
	 *
     * precondition: item.pos < size;
     *               item != null
     *               isHeap()
     * 
     * postcondition: isHeap()
     */
    public int Delete(DHeap_Item item)
    {
    	int delta = item.getKey()-this.Get_Min().getKey()+1;
    	int countComp = Decrease_Key(item, delta);
    	countComp+=this.Delete_Min();
    	return countComp;
    }
	
	/**
	* Sort the array in-place using heap-sort (build a heap, and 
	* perform n times: get-min, del-min).
	* Sorting should be done using the DHeap, name of the items is irrelevant.
	* 
	* Returns the number of comparisons performed.
	* 
	* postcondition: array1 is sorted 
	*/
	public static int DHeapSort(int[] array1, int d)
	{
		DHeap_Item[] heap = new DHeap_Item[array1.length];
		for(int i=0; i<array1.length; i++)
		{
			heap[i]=new DHeap_Item(String.valueOf(i),array1[i]);
		}
		DHeap dHeap = new DHeap (d, heap.length);
		int countComp = dHeap.arrayToHeap(heap);
		DHeap_Item temp;
		for(int i=0; i<heap.length; i++)
		{
			temp = dHeap.Get_Min();
			countComp+=dHeap.Delete_Min();
			array1[i]=temp.getKey();
		}
		return countComp;
	}
	
	public String toString() //method for our tests.
	{
		String str = "[";
		for(int i=0; i<this.size; i++)
		{
			str=(str+"(" + this.array[i].getKey()+","+ this.array[i].getPos() +")");
		}
		str=(str+"]");
		return str;
	}
	
}
