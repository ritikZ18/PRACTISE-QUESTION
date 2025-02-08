
class BubbleSort {

    public static void main(String[] args) {
        int[] arr = { 2, 45, 43, 53, 12, 53, 3, 12, 42, 0, 0, 0, 12, 42, 41 };
        bubbleSort(arr);
        for( int items = 0 ; items < arr.length ; items++){

            System.out.print(arr[items]+ " ");
        }

    }

    public static void bubbleSort(int[] arr) {

        int n = arr.length;
        for (int i = 0; i < n; i++) {       // number of iteratiosn on the loop
            for (int j = 1; j < n - i; j++) {   //internal rounds, to swap elements

                if (arr[j-1] > arr[j]) {
                   int temp = arr[j-1];
                    arr[j-1] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
}