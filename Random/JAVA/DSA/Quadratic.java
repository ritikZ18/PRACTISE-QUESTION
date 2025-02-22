// How Many Solutions Does This Quadratic Have?
// A quadratic equation a x² + b x + c = 0 has either 0, 1, or 2 distinct solutions for real values of x. Given a, b and c, you should return the number of solutions to the equation.

// Examples
// solutions(1, 0, -1) ➞ 2
// // x² - 1 = 0 has two solutions (x = 1 and x = -1).

// solutions(1, 0, 0) ➞ 1
// // x² = 0 has one solution (x = 0).

// solutions(1, 0, 1) ➞ 0
// // x² + 1 = 0 has no real solutions.
// Notes
// You do not have to calculate the solutions, just return how many there are.
// a will always be non-zero.


/*
 * we will  do the D > < = solution 
 * 
 * if D > 0 = 2 solution 
 * if D = 0 = 1 solution 
 * if D < 0 = 0 solution [ imaginary] 
 * 
 */

 package JAVA.DSA ; 
public class Quadratic {


    public static void main(String[] args) {
      int arr[] = { -1 , -1, -1} ; 
      int a = numOfSolutions(arr);
      System.out.println(a);
    }

    public static int numOfSolutions( int arr[]) {
        int solutions ; 
        int a = arr[0], b = arr[1], c = arr[2] ; 
        
        int D = ((b * b) - ( 4 *a*c)) ; 

        if(D > 0){
            solutions = 2 ;
        }
        else if ( D == 0 ){
            solutions = 1 ;
        } 
         else {
            return 0;
         }
        return  solutions ; 
    }
    
}
