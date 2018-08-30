package Other;

import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;

public class Sorter {

    public static void sort(StreamList streams){
        boolean switched;
        do {
            switched = false;
            StreamIterator iter = streams.iterator();
            while (iter.hasNext()) {
                StreamNode tempOne = iter.next();
                StreamNode tempTwo = tempOne.getNext();
                if (tempTwo != null) {
                    if(compare(tempOne, tempTwo)){
                        switched = true;
                        //switch
                        StreamNode tempSwitch = new StreamNode(tempOne);
                        tempOne.setNode(tempTwo);
                        tempTwo.setNode(tempSwitch);
                    }
                }
            }
        } while (switched);
    }

    private static boolean compare(StreamNode nodeOne, StreamNode nodeTwo){
        switch(Settings.getSort()){
            case 1: return nameCompare(nodeOne.getDisplayName(), nodeTwo.getDisplayName());
            case 2: return gameCompare(nodeOne.getGame(), nodeTwo.getGame());
            default: return viewCompare(nodeOne.getViews(), nodeTwo.getViews());
        }
    }

    private static boolean nameCompare(String nameOne, String nameTwo){
        System.out.println("name");
        return nameOne.compareToIgnoreCase(nameTwo) < 0;
    }

    private static boolean gameCompare(String gameOne, String gameTwo){
        System.out.println("game");
        return gameOne.compareToIgnoreCase(gameTwo) < 0;
    }

    private static boolean viewCompare(int viewOne, int viewTwo){
        System.out.println("view");
        return viewOne > viewTwo;
    }

}
