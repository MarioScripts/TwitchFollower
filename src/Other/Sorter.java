package Other;

import StreamList.StreamIterator;
import StreamList.StreamList;
import StreamList.StreamNode;

public class Sorter {
    public static void viewSort(StreamList streams) {
        boolean switched = false;
        do {
            switched = false;
            StreamIterator iter = streams.iterator();
            while (iter.hasNext()) {
                StreamNode tempOne = iter.next();
                StreamNode tempTwo = tempOne.getNext();
                if (tempTwo != null) {
//                    if ((tempTwo.getViews() < tempOne.getViews()) && (tempTwo.getVodcast() && !tempOne.getVodcast())){
                    if (tempTwo.getViews() < tempOne.getViews()) {
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
}
