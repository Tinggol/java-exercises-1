/**
 * A circularly and doubly linked list. 
 * 
 * The task is to split the original list into three circularly and doubly linked lists of equal length.
 */
public class CDList {
    class DNode {
        int element;
        DNode previous, next;

        DNode(int data) { this.element = data; }
    }
	private DNode head;
	public void insert(int e) {
		DNode newNode = new DNode(e);
		if (head == null) {
			newNode.next = newNode;
			newNode.previous = newNode;
			head = newNode;
			return;
		}
		newNode.next = head;
		newNode.previous = head.previous;
		head.previous = newNode;
		newNode.previous.next = newNode;
	}
	
    /**
     * Running time: O( n ).
     */ 
	public CDList[] split() {
		CDList[] ans = new CDList[3];
        DNode cur = head, temp;
        int counter = 0, n;
        //Running time: O( n ), counting will loop for n times
        do {
            cur = cur.next;
            counter++;
        } while (cur != head); //counts the number of nodes and make sures that cur points to head node
        n = (int) ((float) counter / 3) - 1; //assuming the length is a multiple of three (3k)
        //Running time: O( n ), finding the (k) node takes ((n / 3) - 1) times, and we need to find at most 2 times so 2((n / 3) - 1)
        for (int i = 0; i < 3; i++) {
            ans[i] = new CDList(); //allocate memory for the new list
            if (i == 2) {
                ans[i].head = head; //the leftover part of the input list is the final circularly and doubly linked list, save time
                head = null; //release memory
                break;
            }
            for (int j = 0; j < n; j++) cur = cur.next; //find the (k) node, now cur points to (k) node
            temp = cur.next; //assigning a pointer to point to (k + 1) node so that it is still possible to track it back
            cur.next = head; //(k) node's next points to head node
            head.previous.next = temp; //the node behind head node's next points to temp, which is (k + 1) node
            temp.previous = head.previous; //(k + 1) node's previous point to head node's previous node
            head.previous = cur; //head node's previous points to cur, which is (k) node. Now the first (k) nodes have been isolated from the main linked list
            ans[i].head = head; //ans[i]'s head node points to the current head node (which is still part of the isolated linked list) so that we can access the isolated list
            head = temp; //point the head node to (k + 1) node, now (k) nodes have been completely isolated from the main linked list and these (k) notes can be accessed by ans[i].head
            cur = head; //points cur to the new head, which is now part of the main linked list so that the for loop can continue;
        }
        return ans;
	}

	public String toString() {
        if (head == null) return "The list is empty.";
        StringBuilder sb = new StringBuilder();
        DNode cur = head;
        sb.append(cur.element);
        cur = cur.next;
        while ( cur != head ) {
            sb.append(", " + cur.element);
            cur = cur.next;
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int[] a = {11, 12, 13, 55, 52, 58, 29, 26, 20};
        CDList l = new CDList();
        for (int i: a) l.insert(i);
        System.out.println(l);
        CDList[] lists = l.split();
        System.out.println(lists[0]);
        System.out.println(lists[1]);
        System.out.println(lists[2]);
    }
}
