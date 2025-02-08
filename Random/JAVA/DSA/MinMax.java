
import java.util.Arrays;



public class MinMax {

    public static void main(String[] args) {
        int[] arr = { 2,4,53,1,42,64,87,2,4,34,23,1243,6423,4231,5323};
        int[] result = MinMax(arr);
        System.out.println(Arrays.toString(result));
    }

    public static int[] MinMax(int[] arr){
            int min = arr[0] ; 
            int max = arr[0]  ; 

            for( int i = 0 ; i < arr.length ; i++){
                if(arr[i] > arr[items + 1]){
                    min = arr[items];
                    max = arr[items+1];
                }
             
               else{
                min = arr[items+1];
                max = arr[items];
               }

            }


        return new int[]{min,max}; 
    }
    
}
