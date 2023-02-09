import javax.lang.model.util.Elements;
import java.util.Arrays;

/**
 * A better implementation of insertion sort.
 * 
 * The ith major iteration of insertion sort puts element a[i] to its correct position.
 * Your algorithm should use *binary search* to find the position of the first element 
 * that is greater than (> not >=) a[i], or i if there does exist such an element.
 * 
 * Read the {@code Element} class carefully before you start.
 * It uses an extra field *originalPos* to store the index of this element in the input.
 * When an element is output, originalPos is printed in parentheses.
 * 
 * If your implementation is correct, elements of the same value should respect their original order,
 * e.g., for input {1.25, 0, 1.25, 2.5, 10, 2.5, 1.25, 5, 2.5}, the output should be  
 * [0.0 (1), 1.25 (0), 1.25 (2), 1.25 (6), 2.5 (3), 2.5 (5), 2.5 (8), 5.0 (7), 10.0 (4)].
 */
public class Sorting {
    /*
     * Each element has a double value and the original position in the input array.
     */
    static class Element {
        private int originalPos;
        double value;
        public Element(int i, double v) {
            originalPos = i;
            value = v;
        }
        public String toString() {
            return (String.valueOf(value)) + " (" + String.valueOf(originalPos) + ")";
        }
    }

    /**
     * Running time: O( n^2 ).
     */
    public static void insertionSort(Element[] a) {
        int temp_pos, DestinationIndex, n_swap, n = a.length;
        double temp_v;
        for (int i = 1; i < n; i++) {
            //checking
            //System.out.println("After sorted: " + Arrays.toString(a));
            //System.out.println("Key: " + a[i].value);
            //Binary search running time: O( log n ).
            DestinationIndex = binarySearch(a, a[i].value, i);
            if (DestinationIndex != -1){ // a[i-1] < a[i], i >= 1
                n_swap = i - DestinationIndex; //calculate the number of swaps needed to move the element there
                // Running time: O( n ). Worst case would be to swap until the first element
                for (int j = 0; j < n_swap; j++) {
                    //swapping
                    temp_v = a[i - j].value;
                    temp_pos = a[i - j].originalPos;
                    a[i - j].value = a[i - j - 1].value;
                    a[i - j].originalPos = a[i - j - 1].originalPos;
                    a[i - j - 1].value = temp_v;
                    a[i - j - 1].originalPos = temp_pos;
                    //---checking
                    //System.out.println("Swapping " + temp_v + " & " + a[i - j].value);
                }
            }
            else {
                System.out.println("Error!");
                break;
            }
        }
    }

    /**
     * Working principle of this binary search:
     * Using the same working principal, divide the sorted array into 2 parts, and look left/right if the key is smaller/bigger
     * than Element[mid].value. Some boundary cases were considered to prevent index out of range issues. The function will return
     * the index of the correct position to insert into, so that the main function, insertionSort, can calculate the number of swaps
     * required to move the element there.
     * Running time of binary search O (log n)
     */
    public static int binarySearch(Element[] a, double key, int sortedLen) {
        int low = 0, high = sortedLen - 1;
        while (low <= high) {
            //round up by casting into float then +0.5 and cast it back to int
            float f_low = (float) low;
            float f_high = (float) high;
            float f_mid = (f_low + (f_high - f_low) / 2);
            int mid = (int) (f_mid + 0.5);
            if (high == 0 && a[0].value > key) return mid; //boundary case: left end of sorted array
            if (a[mid].value == key) { //look right, do linear search to find the index-1 when a different number is found
                if (mid == high) return ++mid; //boundary case: right end of sorted array, the element on right is itself so just return its index so that n_swap = itself - itself = 0
                if (key < a[mid + 1].value) return ++mid; //element on right is bigger than key and a[mid].value (key == a[mid].value) so it will be the first element larger than key
                low = mid + 1; //look at right half
            }
            else if (a[mid].value > key) { //look left, look at element mid-1,
                /* if key >= a[mid-1], then the current element is the first element that is larger than key
                 *  if key < a[mid -1], look left half
                 **/
                if (key >= a[mid - 1].value) return mid; //element on left is smaller or equal to key, this element is the first element that is larger than key
                high = mid - 1; //look at the left half
            }
            else if (a[mid].value < key) { //look right, look at element mid+1
                /* if key <= a[mid+1], look right half
                 * special care for boundary case where mid == high (right end of the sorted array), the element on right is itself so just return its index so that n_swap = itself - itself = 0
                 **/
                if (mid == high && a[mid + 1].value <= key) return ++mid; //boundary case: right end of the sorted part
                low = mid + 1; //look at right half
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        /*  Personal test cases
         *  9, 0, 5, 6, 1, 1, 9, 2, 0, 10, 0, 0.5
         *  1, 1, 1, 1, 1, 1
         */
        double input[] = {1.25, 0, 2.5, 2.5, 10, 2.5, 1.25, 5, 2.5}; // try different inputs.
        int n = input.length;
        Element[] a = new Element[n];
        for (int i = 0; i < input.length; i++)
            a[i] = new Element(i, input[i]);
        System.out.println("Original: " + Arrays.toString(a));
        insertionSort(a);
        System.out.println("After sorted: " + Arrays.toString(a));
    }
}
