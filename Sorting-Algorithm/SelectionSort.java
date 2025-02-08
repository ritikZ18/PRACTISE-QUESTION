public class SelectionSort {
    // leftmost
    // min value

    public static void main(String[] args) {
        int[] arr = { 5, 4, 3, -2, 1, 0, 0 };
        selectionSort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

    }

    static void selectionSort(int[] arr) {

        int n = arr.length;
        //

        for (int i = 0; i < n - 1; i++) {

            // find leftmost element in array i.e i = 0
            int leftmost = i;

            // lets find the smallest element in array
            // we are checking the min value in array, it has no interference with the other code for swapping, its sets arr[0] as smallest and compare
            // ... to find the smallest and returns the smallest value, so the for(i) loop can swap the elements
            for (int j = i + 1; j < n; j++) {
                if (arr[leftmost] > arr[j]) {
                    leftmost = j;
                }
            }

            int temp = arr[leftmost];
            arr[leftmost] = arr[i];
            arr[i] = temp;

        }
    }
}
