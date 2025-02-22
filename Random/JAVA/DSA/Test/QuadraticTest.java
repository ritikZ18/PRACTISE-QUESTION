package Test ;

import JAVA.DSA.Quadratic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class QuadraticTest{

    @Test
    public void testNumSolutions(){
        assertEquals(0, Quadratic.numOfSolutions(new int[]{1, 1, 1})); // Should return 0 (D < 0)
        assertEquals(2, Quadratic.numOfSolutions(new int[]{1, 0, -1})); // Two real solutions
        assertEquals(1, Quadratic.numOfSolutions(new int[]{1, 0, 0})); // One real solution
        assertEquals(2, Quadratic.numOfSolutions(new int[]{2, 5, -3})); // Two real solutions
        assertEquals(0, Quadratic.numOfSolutions(new int[]{1, 2, 5})); // No real solutions
    }


}