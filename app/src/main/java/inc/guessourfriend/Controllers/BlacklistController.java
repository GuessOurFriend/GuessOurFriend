package inc.guessourfriend.Controllers;

import android.content.Context;
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
import java.util.Arrays;
import java.util.List;

import inc.guessourfriend.SupportingClasses.Friend;
import inc.guessourfriend.Application.Model;
import inc.guessourfriend.R;

/**
 * Created by mgarg on 11/11/15.
 */

public class BlacklistController extends SlideNavigationController {

    private Model model;
    private ListView mainListView ;
    private Friend[] friends ;
    private ArrayAdapter<Friend> listAdapter ;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_blacklist_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle("Blacklist");

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.blacklistlist );

        // When item is tapped, toggle checked properties of CheckBox and Planet.
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> parent, View item,
                                     int position, long id) {
                Friend friend = listAdapter.getItem( position );
                friend.toggleBlacklisted();
                FriendViewHolder viewHolder = (FriendViewHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(friend.isBlacklisted());
            }
        });

        if ( friends == null ) {
            friends = new Friend[] {
                    new Friend(7775,"Eric","Eric","someurl", false), new Friend(7774,"Brian","Brian","someurl", false), new Friend(7778,"Laura","Laura","someurl", false), new Friend(7777,"Steve","Steve","someurl", false),new Friend(7779,"Ash","Ash","someurl", false),new Friend(7776,"Manav","Manav","someurl", false)
            };
        }
        ArrayList<Friend> friendList = new ArrayList<Friend>();
        friendList.addAll( Arrays.asList(friends) );

        // Set our custom array adapter as the ListView's adapter.
        listAdapter = new FriendArrayAdapter(this, friendList);
        mainListView.setAdapter( listAdapter );
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

    private static class FriendArrayAdapter extends ArrayAdapter<Friend> {

        private LayoutInflater inflater;

        public FriendArrayAdapter( Context context, List<Friend> friendList ) {
            super( context, R.layout.customlayout_blacklist_controller, R.id.friendblacklist, friendList );
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context) ;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Friend friend = (Friend) this.getItem( position );

            // The child views in each row.
            CheckBox checkBox ;
            TextView textView ;

            // Create a new row view
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.customlayout_blacklist_controller, null);

                // Find the child views.
                textView = (TextView) convertView.findViewById( R.id.friendblacklist );
                checkBox = (CheckBox) convertView.findViewById( R.id.blacklist_checkbox );


                convertView.setTag( new FriendViewHolder(textView,checkBox) );

                checkBox.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        Friend friend = (Friend) cb.getTag();
                        friend.setBlacklisted( cb.isChecked() );
                    }
                });
            }
            // Reuse existing row view
            else {
                // Because we use a ViewHolder, we avoid having to call findViewById().
                FriendViewHolder viewHolder = (FriendViewHolder) convertView.getTag();
                checkBox = viewHolder.getCheckBox() ;
                textView = viewHolder.getTextView() ;
            }


            checkBox.setTag( friend );
            checkBox.setChecked( friend.isBlacklisted());
            textView.setText( friend.firstName );

            return convertView;
        }

    }

}