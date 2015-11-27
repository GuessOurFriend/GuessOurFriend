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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.SupportingClasses.OutgoingChallenge;
import inc.guessourfriend.R;

/**
 * Created by Laura on 10/31/2015.
 */
public class ChallengeAFriendController extends SlideNavigationController {

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


        ArrayList<Friend> friendList = new ArrayList<Friend>();
        friendList = model.fbProfileModel.friendList;

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View item,
                                    int position, long id) {
                Friend friend = listAdapter.getItem(position);
                friend.toggleChallenged();
                FriendViewHolder viewHolder = (FriendViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(friend.isChallenged());
                long challengeeId = model.fbProfileModel.friendList.get(position).facebookID;

                model.outgoingChallengeListModel.addOutgoingChallenge(
                        new OutgoingChallenge(challengeeId));

                //Send the request to the server
                NetworkRequestHelper.sendChallenge(Long.toString(challengeeId));

            }
        });

        listAdapter = new FriendArrayAdapter(this, friendList);
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
            CheckBox checkBox ;
            TextView textView ;

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
                FriendViewHolder viewHolder = (FriendViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }

            checkBox.setOnClickListener( new View.OnClickListener() {
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v ;
                    Friend friend = (Friend) cb.getTag();
                    friend.setChallenged(cb.isChecked());
                    long challengeeId = model.fbProfileModel.friendList.get(itemPosition).facebookID;

                    model.outgoingChallengeListModel.addOutgoingChallenge(
                            new OutgoingChallenge(challengeeId));

                    //Send the request to the server
                    NetworkRequestHelper.sendChallenge(Long.toString(challengeeId));

                }
            });

            checkBox.setTag( friend );
            checkBox.setChecked( friend.isChallenged());
            textView.setText( friend.firstName );

            return convertView;
        }

    }


      /*  ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, friendNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int itemPosition = position;
                final String itemValue = (String) listView.getItemAtPosition(position);

                AlertDialog.Builder adb = new AlertDialog.Builder(ChallengeAFriendController.this);
                adb.setTitle("Send Challenge Request");
                adb.setMessage("Send " + itemValue + " a challenge request?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Get the facebook id of the row we clicked
                                long challengeeId = model.fbProfileModel.friendList.get(itemPosition).facebookID;

                                model.outgoingChallengeListModel.addOutgoingChallenge(
                                        new OutgoingChallenge(challengeeId));

                                //Send the request to the server
                                NetworkRequestHelper.sendChallenge(Long.toString(challengeeId));

                                Toast.makeText(getApplicationContext(),
                                        "Challenged: " + challengeeId, Toast.LENGTH_SHORT).show();
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
    } */
}
