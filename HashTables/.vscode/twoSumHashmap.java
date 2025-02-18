
import java.util.HashMap;

public class twoSumHashmap {
    

    public static void main(String[] args) {

        int[] arr = { 4,13,74,12,-13}; 
        int target = 0 ; 
        int[] index = twoSum(arr, target) ; 
        System.out.println("Index Number : "+index[0]+ ","+ index[1]);

        
    }
    
    
    public static int[] twoSum( int[] arr, int target) {
        HashMap<Integer, Integer> numsTable = new HashMap<>() ; 

        for( int i = 0 ; i < arr.length; i++){
            int complement = target - arr[i];
            if(numsTable.containsKey(complement)){
                return new int[]{numsTable.get(complement) , i};
            }
          numsTable.put(arr[i], i ) ; 
        }
        throw new IllegalArgumentException("No two sum solution");

    }
}
