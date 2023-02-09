import java.util.Arrays;

/*
 * You have been delivered a hand of cards, and you sorted them in the suit-first order:
 * spades, hearts, clubs, and diamonds, each suit in decreasing order. 
 *
 * See {@code main} where the input is in this order.
 * [♠A, ♠10, ♠8, ♠4, ♠2, ♥K, ♥J, ♥9, ♥8, ♥2, ♣10, ♣4, ♦A, ♦J, ♦9, ♦7, ♦6, ♦4]
 *
 * Your task is to reorder them into rank-first order: for cards of the same rank, you follow the order of spade, heart, club, and then diamond.
 * 
 * For the given hand, the correct result should be: 
 * [♠A, ♦A, ♥K, ♥J, ♦J, ♠10, ♣10, ♥9, ♦9, ♠8, ♥8, ♦7, ♦6, ♠4, ♣4, ♦4, ♠2, ♥2]
 * 
 * You need to set your "text file encoding" to UTF-8 to run this class.
 * Otherwise, the suits of cards cannot be shown properly. 
 * 
 * The class {@code Card} is at the end of this file.
 */
public class CardGame {
    /**
     * Running time: O( n^2 ).
     */ 
    public static void reorder(Card[] hand) {
        int DestinationIndex, n_swap, n = hand.length;
        byte temp_rank, temp_suit;
        for (int i = 1; i < n; i++) {
            //Binary search running time: O( log n ).
            DestinationIndex = binarySearch(hand, hand[i].rank, hand[i].suit, i);
            if (DestinationIndex != -1) {
                n_swap = i - DestinationIndex; //calculate the number of swaps needed to move the card there
                // Running time: O( n ). Worst case would be to swap until the first card
                for (int j = 0; j < n_swap; j++) {
                    //swapping
                    temp_rank = hand[i - j].rank;
                    temp_suit = hand[i - j].suit;
                    hand[i - j].rank = hand[i - j - 1].rank;
                    hand[i - j].suit = hand[i - j - 1].suit;
                    hand[i - j - 1].rank = temp_rank;
                    hand[i - j - 1].suit = temp_suit;
                }
                //checking
                //System.out.println("after reordering: " + Arrays.toString(hand));
            }
            else {
                System.out.println("Error!");
                break;
            }
        }
    }

    /**
     * Working principle of this binary search:
     * Using the same working principal, divide the sorted array into 2 parts, and look left/right if the *rank* is bigger/smaller
     * than hand[mid].rank. Some boundary cases were considered to prevent index out of range issues. The function will return
     * the index of the correct position to insert into, so that the main function, insertionSort, can calculate the number of swaps
     * required to move the element there.
     * Running time of binary search O( log n )
     *
     * How to deal with suit?
     * Once the proper insert location has been found, a while loop is initiated to calculate the difference between the key's suit
     * and target location's to obtain the "distance" between the 2 suits. (Ex: key's suit = 3, hand[mid].suit = 0, diff = 3-0 = +3)
     * If the result is positive and non-zero, move mid to left by one (or two, see line 109) and recalculate the difference.
     * If negative, move mid to right (or break the loop since the condition has been satisfied) and recalculate.
     * This will ensure that the suit will be inserted by ascending order, unlike rank which is descending.
     * Running time of while loop O( 1 )
     */
    public static int binarySearch(Card[] hand, byte rank, byte suit, int sortedLen){
        int low = 0, high = sortedLen - 1;
        while (low <= high) {
            //round up by casting into float then +0.5 and cast it back to int
            float f_low = (float) low;
            float f_high = (float) high;
            float f_mid = (f_low + (f_high - f_low) / 2);
            int mid = (int) (f_mid + 0.5);
            if (high == 0) { //boundary case: left end of sorted array
                if (hand[0].rank < rank || (hand[0].rank == rank && hand[0].suit > suit)) return mid; // swap
                return ++mid; //no swap, first major iteration
            }
            if (hand[mid].rank == rank) { //look left
                if (mid == high) { //boundary case: right end of sorted array, the card on right is itself
                    if (hand[mid].suit <= suit) return ++mid; //no swap
                    return mid; //hand[mid].suit > suit, swap
                }
                if (hand[mid - 1].rank >= rank) { //card on left is bigger or same
                    while (suit - hand[mid].suit != 0 && rank == hand[mid].rank) {
                        if (rank == hand[mid + 1].rank && suit > hand[mid].suit && suit < hand[mid + 1].suit) break;
                        else if (suit - hand[mid].suit > 0) ++mid;
                        else {
                            if (mid == 0) break; //boundary case: left end
                            --mid;
                        }
                    }
                    return mid;
                }
                high = mid - 1; //look at left half
            }
            else if (hand[mid].rank > rank) { //look right, look at element mid+1,
                if (mid == high) return ++mid; //no swap
                if (hand[mid + 1].rank <= rank) { //card on right is smaller or same
                    if (rank == hand[mid + 1].rank) {
                        while (suit - hand[mid + 1].suit != 0 && rank == hand[mid + 1].rank) {
                            if (suit - hand[mid + 1].suit > 0) mid += 2;
                            else break;
                            if (mid == sortedLen) break; //boundary case: right end
                        }
                        return mid;
                    }
                    return ++mid; //swap, card on right is smaller
                }
                low = mid + 1; //look at right half
            }
            else if (hand[mid].rank < rank) { //look left, look at element mid-1
                if (hand[mid - 1].rank >= rank) { //card on left is bigger or same
                    while (suit - hand[mid - 1].suit != 0 && rank == hand[mid - 1].rank) {
                        if (suit - hand[mid - 1].suit > 0) break;
                        else --mid;
                    }
                    return mid;
                }
                high = mid - 1; //look at left half
            }
        }
        return -1;
    }
    
    public static void main(String[] args) {
        /*
         * The following 12 lines are for setting our test data.
         * You can revise them to change the hand for testing.
         * */
        byte[][] data = {{14, 10, 8, 4, 2}, // Spades
                {13, 11, 9, 8, 2}, // Hearts
                {10, 4}, // Clubs
                {14, 11, 9, 7, 6, 4} // Diamonds
        };
        Card[] hand = new Card[18];
        for (int i = 0; i < hand.length; i++) {
            for (byte suit = 0; suit < 4; suit++) {
                for (int j = 0; j < data[suit].length; j++)
                    hand[i++] = new Card(suit, data[suit][j]);
            }
        }
        System.out.println("original: " + Arrays.toString(hand));
        reorder(hand);
        System.out.println("after reordering: " + Arrays.toString(hand));
    }
}

/*
 * Each Card has a suit and a rank, between 2 and 14 (A).
 */
class Card {
    byte suit;
    byte rank;
    public static final byte SPADE = 0;
    public static final byte HEART = 1;
    public static final byte CLUB = 2;
    public static final byte DIAMOND = 3;

    public Card(byte suit, byte rank) {
        this.suit = suit;
        this.rank = rank;
    }
    public String toString() {
        String s = null;
        switch(suit) {
            case SPADE : s = "\u2660"; break;
            case HEART : s = "\u2665"; break;
            case CLUB : s = "\u2663"; break;
            case DIAMOND : s = "\u2666"; break;
        }
        char[] c = {'J', 'Q', 'K', 'A'};
        s += rank > 10?c[rank-11]:String.valueOf(rank);
        return s;
    }
}
