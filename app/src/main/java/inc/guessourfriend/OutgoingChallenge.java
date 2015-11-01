package inc.guessourfriend;

/**
 * Created by Ashwini1505 on 01-11-2015.
 */
public class OutgoingChallenge
{
        private long challengeeId;
        public boolean sentMessage;
        public boolean wasDeclined;

        public long getChallengeeId(){
        return this.challengeeId;
        }

         public boolean getSentMessage(){
        return this.sentMessage;
        }

        public boolean getWasDeclined(){
        return this.wasDeclined;
        }
}
