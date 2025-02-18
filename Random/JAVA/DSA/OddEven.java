public class OddEven{



    public static void main(String[] args) {
        int[] nums = { 1,53,3,45,12,53,12,3,53,2};

        for( int i = 0 ; i < nums.length ; i++){
            if((nums[i] & 1 )== 1){
                System.out.println("Odd Number : " + nums[i]);

            }
            else{
                System.out.println("Number is Even : " + nums[i]);
            }
        }

    }



}