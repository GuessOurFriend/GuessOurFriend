package inc.guessourfriend.Models;

import java.util.ArrayList;
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


    public Map <Long,Long> leaderboardMap = new HashMap();
    // HashMap which stores the list of FacebookID's of all users as key and the values is the total points
    // The above map is unsorted by values(points)
    public Map <Long,Long> sortedleaderboardMap = new LinkedHashMap<Long,Long>();
    //sortedleaderboardList contain the entries sorted by points. Can just be used to populate the view by simple iteration.

    public List <String> sortedleaderboardList = new ArrayList<>();
    public LeaderboardListModel(){
        populateLeaderboardList();
    }

    public List <String> getLeaderboardList(){
        // Adding test data to both directly and indirectly test the functionality of leaderboard generator logic

        leaderboardMap.put(77777L,98L);
        leaderboardMap.put(77779L,36L);
        leaderboardMap.put(77778L,71L);
        sortLeaderboardList(leaderboardMap);

        leaderboardMap.put(77775L,46L);
        leaderboardMap.put(77774L,64L);
        leaderboardMap.put(77776L, 58L);
        sortLeaderboardList(leaderboardMap);
        //String testEntry1 = "FBID: 77777   Points: 100" ;
        //String testEntry2 = "FBID: 77778   Points: 75" ;
        //this.sortedleaderboardList.add(testEntry1);
        //this.sortedleaderboardList.add(testEntry2);
        return this.sortedleaderboardList;
    }

    public void sortLeaderboardList(Map <Long,Long> leaderboardMap){
        List<Map.Entry<Long, Long>> list = new LinkedList<Map.Entry<Long, Long>>(leaderboardMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Long, Long>>() {
            public int compare(Map.Entry<Long, Long> o1,
                               Map.Entry<Long, Long> o2) {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });
        this.sortedleaderboardList.clear();
        for (Iterator<Map.Entry<Long, Long>> it = list.iterator(); it.hasNext();) {
            Map.Entry<Long, Long> entry = it.next();
            this.sortedleaderboardMap.put(entry.getKey(), entry.getValue());
            String Entry = "FBID: " + entry.getKey() + "       " + "Points: " + entry.getValue();
            this.sortedleaderboardList.add(Entry);
        }
    }

    public void populateLeaderboardList(){
        // TODO: Get data from the database
        // Just need to insert the user's Facebook's ID and points in the hashmap using puts
    }


}
