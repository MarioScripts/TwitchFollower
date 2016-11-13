package StreamList;

import Exceptions.DuplicateStreamException;

import java.util.NoSuchElementException;


public class StreamList {
    private StreamNode front, rear;
    private int count;

    public StreamList(){
        front = rear = null;
        count = 0;
    }

    public void add(StreamNode node) throws DuplicateStreamException {
        if(contains(node.getName())){
            throw new DuplicateStreamException("This stream already exists");
        }else{
            if(size() == 0){
                front = rear = node;
            }else{
                rear.setNext(node);
                rear = rear.getNext();
            }

            count++;
        }
    }

    public void remove(String name){
        name = name.toLowerCase();

        if(isEmpty()){
            throw new NoSuchElementException();
        }

        boolean found = false;

        StreamNode curr = front;
        StreamNode prev = null;

        while(curr != null && !found){
            if(curr.getName().toLowerCase().equals(name)){
                found = true;
            }else{
                prev = curr;
                curr = curr.getNext();
            }

        }

        if(found){
            count--;
            if(prev == null){
                front = front.getNext();
            }else{
                if(prev.getNext() != null && prev.getNext() == rear){
                    rear = prev;
                }
                prev.setNext(prev.getNext().getNext());
            }
        }
    }

    public boolean contains(String name){
        StreamIterator iter = iterator();
        StreamNode temp;
        while(iter.hasNext()){
            temp = iter.next();
            if(temp.getName().toLowerCase().equals(name.toLowerCase())){
                return true;
            }
        }

        return false;
    }

    public boolean isEmpty(){
        return count <= 0;
    }

    public int size(){
        return count;
    }

    public StreamIterator iterator(){
        return new StreamIterator(front, count);
    }

}

