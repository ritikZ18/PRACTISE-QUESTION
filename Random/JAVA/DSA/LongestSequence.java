import java.util.Arrays;
public class LongestSequence {
    



        public static int maxProduct(int[] nums) {

            int n = nums.length;
          
             if( n ==0 ){
                return 0 ; 
            }
             if( n ==1 ){
                return 1 ; 
            }
            
             
            
                            Arrays.sort(nums);
                            System.out.println(Arrays.toString(nums));
                            return Math.max(nums[n-1] * nums[n-2], nums[0] * nums[1]);
        }
    
        public static void main(String[] args) {
            int[] nums = {-1,1,-3,6,2,6,-43};
            System.out.println(maxProduct(nums)); // Output: 30
        }
    }
    

