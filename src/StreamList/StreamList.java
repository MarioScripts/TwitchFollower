package StreamList;

import Exceptions.DuplicateStreamException;

import java.util.NoSuchElementException;


public class StreamList {

    // First and last element in list
    private StreamNode front, rear;

    // Number of elements in list
    private int count;

    /**
     * Constructor
     */
    public StreamList() {
        front = rear = null;
        count = 0;
    }

    /**
     * Adds StreamNode object to list
     * @param node Node that you wish to add
     * @throws DuplicateStreamException thrown when node with same name is already available
     */
    public void add(StreamNode node) throws DuplicateStreamException {
        if (contains(node.getName())) {
            throw new DuplicateStreamException("This stream already exists");
        } else {
            if (size() == 0) {
                front = rear = node;
            } else {
                rear.setNext(node);
                rear = rear.getNext();
            }

            count++;
        }
    }

    /**
     * Removes StreamNode object from list
     * @param name Name of StreamNode to remove
     */
    public void remove(String name) {
        name = name.toLowerCase();

        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        boolean found = false;

        StreamNode curr = front;
        StreamNode prev = null;

        while (curr != null && !found) {
            if (curr.getName().toLowerCase().equals(name)) {
                found = true;
            } else {
                prev = curr;
                curr = curr.getNext();
            }

        }

        if (found) {
            count--;
            if (prev == null) {
                front = front.getNext();
            } else {
                if (prev.getNext() != null && prev.getNext() == rear) {
                    rear = prev;
                }
                prev.setNext(prev.getNext().getNext());
            }
        }
    }

    /**
     * Checks to see if there exists a StreamNode in the list with the given name
     * @param name Name you wish to look for
     * @return Boolean, returns false if no duplicates are found
     */
    public boolean contains(String name) {
        StreamIterator iter = iterator();
        StreamNode temp;
        while (iter.hasNext()) {
            temp = iter.next();
            if (temp.getName().toLowerCase().equals(name.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if list is empty
     * @return Boolean, true if empty
     */
    public boolean isEmpty() {
        return count <= 0;
    }

    /**
     * Returns size of list
     * @return size as int
     */
    public int size() {
        return count;
    }

    /**
     * Returns iterator for the list
     * @return List iterator as StreamIterator
     */
    public StreamIterator iterator() {
        return new StreamIterator(front, count);
    }

}

