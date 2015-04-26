
public class InsertionSort {
	
	public static void main(String[] args){
		int[] arr = {2, 4, 6, 8, 10};
		insertIntoSorted(arr);
	}
	
	 public static void insertIntoSorted(int[] ar) {
	        int size = ar.length;
	        boolean done = false;
	        int outOfOrderElem = ar[size-1];
	        int outOfOrderElemIndex = size-1;
	        int i = size-2;
	       while(!done)
	        {
	        	if( i >= 0 && outOfOrderElem < ar[i])
	        		ar[outOfOrderElemIndex] = ar[i];
	        	else{
	        		ar[outOfOrderElemIndex] = outOfOrderElem;
	        		done = true;
	        	}
	        	outOfOrderElemIndex--;
	        	for(int j = 0; j <size; j++){
	        		System.out.print(ar[j]+" ");
	        		
	        	}
	        	System.out.println();
	        	i--;
	        }
	    }

}
