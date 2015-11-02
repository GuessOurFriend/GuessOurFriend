package inc.guessourfriend;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;


/**
 * Created by mgarg on 11/1/15.
 */
public class LeaderboardListModel {


    private Map <Long,Long> leaderboardList = new HashMap();
    // HashMap which stores the list of FacebookID's of all users as key and the values is the total points
    // The above map is unsorted by values(points)
    private Map <Long,Long> sortedleaderboardList = new LinkedHashMap<Long,Long>();
    //sortedleaderboardList contain the entries sorted by points. Can just be used to populate the view by simple iteration.

    public LeaderboardListModel(){
        populateLeaderboardList();
    }

    public Map <Long,Long> getLeaderboardList(){
        return leaderboardList;
    }

    public void sortLeaderboardList(Map <Long,Long> leaderboardList){
        List<Map.Entry<Long, Long>> list = new LinkedList<Map.Entry<Long, Long>>(leaderboardList.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Long, Long>>() {
            public int compare(Map.Entry<Long, Long> o1,
                               Map.Entry<Long, Long> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });
        for (Iterator<Map.Entry<Long, Long>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Long, Long> entry = it.next();
            this.sortedleaderboardList.put(entry.getKey(), entry.getValue());
        }
    }

    public void populateLeaderboardList(){
        // TODO: Get data from the database
        // Just need to insert the user's Facebook's ID and points in the hashmap using puts
    }


}
