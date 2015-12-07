package inc.guessourfriend.Controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.Models.IncomingChallengeListModel;
import inc.guessourfriend.Models.OutgoingChallengeListModel;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.NetworkCommunication.OnTaskCompleted;
import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.SupportingClasses.Game;
import inc.guessourfriend.SupportingClasses.IncomingChallenge;
import inc.guessourfriend.SupportingClasses.OutgoingChallenge;
import inc.guessourfriend.R;


public class ChallengeAFriendController extends SlideNavigationController implements OnTaskCompleted {

    private Model model;
    ListView mainListView;
    private ArrayAdapter<Friend> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // Set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_challenge_a_friend_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);

        mainListView = (ListView) findViewById(R.id.challengeafriendlist);


        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item,
                                    int position, long id) {
                final int itemPosition = position;
                final Friend friend = listAdapter.getItem(position);
                //friend.toggleChallenged();
                final FriendViewHolder viewHolder = (FriendViewHolder) item.getTag();
                //viewHolder.getCheckBox().setChecked(friend.isChallenged());

                /////////////****  start*////////////////////
                String itemValue = model.fbProfileModel.friendList.get(position).firstName;
                final boolean Challengedstatus = model.fbProfileModel.friendList.get(itemPosition).isChallenged;
                String dialogmsg = new String();
                String dialogmsg2 = new String();
                if (Challengedstatus == false) {
                    dialogmsg = "Send " + itemValue + " a challenge request?";
                    dialogmsg2 = "Send Challenge Request";
                } else {
                    dialogmsg = "Delete " + itemValue + " a challenge request?";
                    dialogmsg2 = "Delete Challenge Request";
                }

                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengeAFriendController.this);
                adb.setTitle(dialogmsg2);
                adb.setMessage(dialogmsg)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Get the facebook id of the row we clicked
                                long challengeeId = model.fbProfileModel.friendList.get(itemPosition).facebookID;
                                friend.toggleChallenged();
                                viewHolder.getCheckBox().setChecked(friend.isChallenged());
                                if(friend.isChallenged){
                                    viewHolder.getCheckBox().setButtonDrawable(R.drawable.checked);
                                }else{
                                    viewHolder.getCheckBox().setButtonDrawable(R.drawable.unchecked);
                                }
                                if (Challengedstatus == false) {
                                    model.outgoingChallengeListModel.addOutgoingChallenge(
                                            new OutgoingChallenge(challengeeId));

                                    //Send the request to the server
                                    NetworkRequestHelper.sendChallenge(Long.toString(challengeeId));
                                } else {
                                    model.outgoingChallengeListModel.deleteOutgoingChallenge(
                                            new OutgoingChallenge(challengeeId));

                                    //Send the request to the server
                                    // dummy challenge id for now TODO: Complete it later
                                    NetworkRequestHelper.deleteChallengeFromChallenger(challengeeId);

                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = adb.create();
                alertDialog.show();

            }
        });

        listAdapter = new FriendArrayAdapter(this, model.fbProfileModel.friendList);
        NetworkRequestHelper.getAllGames(ChallengeAFriendController.this);
        NetworkRequestHelper.getIncomingChallenges(ChallengeAFriendController.this);
        NetworkRequestHelper.getOutgoingChallenges(ChallengeAFriendController.this);
        mainListView.setAdapter(listAdapter);
    }

    private static class FriendViewHolder {
        private CheckBox checkBox ;
        private TextView textView ;
        public FriendViewHolder() {}
        public FriendViewHolder( TextView textView, CheckBox checkBox ) {
            this.checkBox = checkBox ;
            this.textView = textView ;
        }
        public CheckBox getCheckBox() {
            return checkBox;
        }
        public void setCheckBox(CheckBox checkBox) {
            this.checkBox = checkBox;
        }
        public TextView getTextView() {
            return textView;
        }
        public void setTextView(TextView textView) {
            this.textView = textView;
        }
    }

    private class FriendArrayAdapter extends ArrayAdapter<Friend> {

        private LayoutInflater inflater;

        public FriendArrayAdapter( Context context, List<Friend> friendList ) {
            super( context, R.layout.customlayout_challenge_a_friend_controller, R.id.friendchallengelist, friendList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent) {
            final int itemPosition = position;
            Friend friend = (Friend) this.getItem( itemPosition );

            // The child views in each row.
            final CheckBox checkBox ;
            TextView textView ;
            FriendViewHolder viewHolder;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.customlayout_challenge_a_friend_controller, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.friendchallengelist );
                checkBox = (CheckBox) convertView.findViewById( R.id.challengeafriend_checkbox );


                convertView.setTag( new FriendViewHolder(textView,checkBox) );

            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                viewHolder = (FriendViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }

            checkBox.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final CheckBox cb = (CheckBox) v;
                    final Friend friend = (Friend) cb.getTag();
                    //friend.setChallenged(cb.isChecked());

                    String itemValue = model.fbProfileModel.friendList.get(itemPosition).firstName;
                    final boolean Challengedstatus = model.fbProfileModel.friendList.get(itemPosition).isChallenged;
                    String dialogmsg = new String();
                    String dialogmsg2 = new String();
                    if (Challengedstatus == false) {
                        dialogmsg = "Send " + itemValue + " a challenge request?";
                        dialogmsg2 = "Send Challenge Request";
                    } else {
                        dialogmsg = "Delete " + itemValue + " a challenge request?";
                        dialogmsg2 = "Delete Challenge Request";
                    }


                    AlertDialog.Builder adb = new AlertDialog.Builder(ChallengeAFriendController.this);
                    adb.setTitle(dialogmsg2);
                    adb.setMessage(dialogmsg)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //Get the facebook id of the row we clicked
                                    long challengeeId = model.fbProfileModel.friendList.get(itemPosition).facebookID;
                                    friend.setChallenged(cb.isChecked());
                                    if (Challengedstatus == false) {
                                        checkBox.setButtonDrawable(R.drawable.checked);
                                        model.outgoingChallengeListModel.addOutgoingChallenge(
                                                new OutgoingChallenge(challengeeId));

                                        //Send the request to the server
                                        NetworkRequestHelper.sendChallenge(Long.toString(challengeeId));
                                    } else {
                                        checkBox.setButtonDrawable(R.drawable.unchecked);
                                        model.outgoingChallengeListModel.deleteOutgoingChallenge(
                                                new OutgoingChallenge(challengeeId));

                                        //Send the request to the server
                                        // dummy challenge id for now TODO: Complete it later
                                        NetworkRequestHelper.deleteChallengeFromChallenger(challengeeId);
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    checkBox.setChecked(friend.isChallenged());
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alertDialog = adb.create();
                    alertDialog.show();

                }
            });



            checkBox.setTag(friend);
            textView.setText(friend.firstName + " " + friend.lastName);
            // convertView.setEnabled and .setClickable are super weird and you need to pass in
            //      the opposite boolean into it for it to work
            if(friend.isChallenged){
                checkBox.setButtonDrawable(R.drawable.checked);
                checkBox.setChecked(true);
                checkBox.setEnabled(true);
                checkBox.setClickable(true);
                convertView.setEnabled(false);
                convertView.setClickable(false);
            }else if(friend.isInGameWithMe || friend.hasChallengedMe) {
                checkBox.setButtonDrawable(R.drawable.unavailable);
                checkBox.setEnabled(false);
                checkBox.setClickable(false);
                convertView.setEnabled(true);
                convertView.setClickable(true);
            }else{
                checkBox.setButtonDrawable(R.drawable.unchecked);
                checkBox.setChecked(false);
                checkBox.setEnabled(true);
                checkBox.setClickable(true);
                convertView.setEnabled(false);
                convertView.setClickable(false);
            }
            return convertView;
        }
    }

    // listview - setClickable(true) setClickable(false)
    // checkbox - checkBox.setEnabled(false)

    public void onTaskCompleted(String taskName, Object result) {
        if(taskName.equalsIgnoreCase("gamesLoaded")){
            model.currentGameListModel.setCurrentGameList((List<Game>) result);
            List<Game> gameList = model.currentGameListModel.getCurrentGameList();
            //Update the model's list and view's list with the result
            for (int i = 0; i < model.fbProfileModel.friendList.size(); i++) {
                model.fbProfileModel.friendList.get(i).isInGameWithMe = false;
                for (int j = 0; j < gameList.size(); j++) {
                    if (model.fbProfileModel.friendList.get(i).facebookID == gameList.get(j).opponentID) {
                        model.fbProfileModel.friendList.get(i).isInGameWithMe = true;
                        break;
                    }
                }
            }
            listAdapter.notifyDataSetChanged();
        }else if(taskName.equalsIgnoreCase("getIncomingChallenges")){
            //Update the model's list and view's list with the result
            model.incomingChallengeListModel = (IncomingChallengeListModel) result;
            List <IncomingChallenge> iclist = new ArrayList<IncomingChallenge>();
            iclist = model.incomingChallengeListModel.getIncomingChallengeList();
            for (int i = 0; i < model.fbProfileModel.friendList.size(); i++) {
                model.fbProfileModel.friendList.get(i).hasChallengedMe = false;
                for (int j = 0; j < iclist.size(); j++) {
                    if (model.fbProfileModel.friendList.get(i).facebookID == iclist.get(j).fbId) {
                        model.fbProfileModel.friendList.get(i).hasChallengedMe = true;
                        break;
                    }
                }
            }
            listAdapter.notifyDataSetChanged();
        }else if(taskName.equalsIgnoreCase("getOutgoingChallenges")){
            //Update the model's list and view's list with the result
            model.outgoingChallengeListModel = (OutgoingChallengeListModel) result;
            ArrayList <OutgoingChallenge> oclist = new ArrayList<OutgoingChallenge>();
            oclist = model.outgoingChallengeListModel.getOutgoingChallengeList();
            for (int i = 0; i < model.fbProfileModel.friendList.size(); i++) {
                model.fbProfileModel.friendList.get(i).isChallenged = false;
                for (int j = 0; j < oclist.size(); j++) {
                    if (model.fbProfileModel.friendList.get(i).facebookID == oclist.get(j).fbID) {
                        model.fbProfileModel.friendList.get(i).isChallenged = true;
                        break;
                    }
                }
            }
            listAdapter.notifyDataSetChanged();
        }
    }

}
