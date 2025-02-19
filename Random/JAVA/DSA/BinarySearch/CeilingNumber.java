package BinarySearch ; 
/*
 * Given a sorted array 
 * ceiling is target number 
 * Smallest element in array smallest , greater than the target element or equal to target
 *  int arr[] = { 1,2,4,6,8,10, 11,15,18,19};
 * target = 13
 * output = { 15 }
 */
public class CeilingNumber {

    public static void main(String[] args) {
        int arr[] = { 1,2,4,6,8,10, 11,15,18,19};
        int target = 13 ; 
        int output = ceilingNumber(arr, target) ; 
        System.out.println(output);

    }
    
    public static int ceilingNumber(int arr[], int target){
        int n = arr.length ; 
        int start = 0 ; 
        int end = n-1 ; 
        int result = -1 ; 
    while(start <= end){ 
            int mid = start + (end-start)/2 ; 

            if(arr[mid] > target ){
                end = mid  ; 
                
                
            }
            else if ( arr[mid] <= target){
                    result = mid ; 
                    start = mid ; 

            }
            

                
            
        }
        return result ; 

    }
}
