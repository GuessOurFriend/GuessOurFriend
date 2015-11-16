package inc.guessourfriend;

import android.test.AndroidTestCase;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mgarg on 11/15/15.
 */
public class IncomingChallengeListModelTest extends AndroidTestCase {

    IncomingChallengeListModel incomingChallengeListModel;
    public void setUp() throws Exception {
        super.setUp();
        incomingChallengeListModel = new IncomingChallengeListModel();

    }

    @Test
    public void getincomingchallengelist(){
        List<IncomingChallenge> teststring = new ArrayList<>();
        IncomingChallenge test = new IncomingChallenge(77777);
        teststring.add(test);
        Assert.assertEquals(teststring, incomingChallengeListModel.getIncomingChallengeList());

    }

}