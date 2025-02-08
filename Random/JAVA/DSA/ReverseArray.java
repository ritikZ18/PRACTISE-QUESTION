import java.util.Arrays;

public class ReverseArray {

    public static void main(String[] args) {
        int[] arr = { 2, 53, 523, 64, 12, 765, 21, 43, 23, 7, 23, 12, 542, 21, 54, 21, 45, 7, 312 };
        int k = 3;
        reverseArray(arr);
        System.out.println(Arrays.toString(arr));

        reverseSubArray(arr, k);
        System.out.println("\nSubArray Reversed : "+Arrays.toString(arr));

    }

    public static void reverseArray(int[] arr) {

        // reverse array Logic :
        /*
         * 1. Length of array
         * 2. two pointer, n1 and n2,
         * 3. take the value of and just swap with temp and then assign the temp to
         * start and start to end
         */
        int start = 0;
        int n = arr.length;
        int end = n - 1;
        while (start < end) {
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;

            start++;
            end--;
        }

    }

    public static void reverseSubArray(int[] arr, int k) {
        int n = arr.length;

        for (int i = 0; i < n; i = i + k) {
            int start = i;
            int end = Math.min(i + k - 1, n - 1);
            int temp = arr[start];
            arr[start] = arr[end];
            arr[end] = temp;
            start++;
            end--;

        }

    }
}