package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import inc.guessourfriend.R;

public class OptionsController extends SlideNavigationController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_options_controller, frameLayout);
        //mDrawerList.setItemChecked(position, true);
        //setTitle(listArray[position]);
        Button blacklistb=(Button)findViewById(R.id.blacklistbutton);
        Button inviteb=(Button)findViewById(R.id.inviteafriendbutton);
        Button reportbugb=(Button)findViewById(R.id.reportabugbutton);


        blacklistb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OptionsController.this, BlacklistController.class);
                startActivity(intent);
                finish();
            }
        });

        reportbugb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(OptionsController.this, ReportABugController.class);
                startActivity(intent);
                finish();
            }
        });

    }


}
