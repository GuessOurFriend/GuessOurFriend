package inc.guessourfriend.Controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import inc.guessourfriend.Application.Model;
import inc.guessourfriend.NetworkCommunication.NetworkRequestHelper;
import inc.guessourfriend.R;
import inc.guessourfriend.SupportingClasses.Game;

/**
 * Created by Laura on 12/5/2015.
 */
public class ReportABugController extends SlideNavigationController {

    private Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // get the models
        model = (Model) getApplicationContext();
        // set up slide navigation
        getLayoutInflater().inflate(R.layout.activity_report_a_bug_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);

        final Intent intent = getIntent();
        final String intentFrom;
        if (savedInstanceState == null) {
            Bundle extras = intent.getExtras();
            if(extras == null) {
                intentFrom = null;
            } else {
                intentFrom= extras.getString("intentFrom");
            }
        } else {
            intentFrom= (String) savedInstanceState.getSerializable("intentFrom");
        }

        setTitle(listArray[position]);
        Button button = (Button) findViewById(R.id.report_bug_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                final String intentFr = newString;
                EditText bugTitle = (EditText) findViewById(R.id.bug_title);
                EditText bugContent = (EditText) findViewById(R.id.bug_content);
                String bugTitleString = bugTitle.getText().toString();
                String bugContentString = bugContent.getText().toString();
                NetworkRequestHelper.sendBugReport(bugTitleString, bugContentString);
                Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                if (intentFrom.equalsIgnoreCase("MiddleOfGameController")) {
                    Game game = (Game) intent.getParcelableExtra("game");
                    Intent i = new Intent(ReportABugController.this, MiddleOfGameController.class);
                    i.putExtra("gameId", game.ID);
                    i.putExtra("opponentID", game.opponentID);
                    i.putExtra("opponentFirstName", game.opponentFirstName);
                    i.putExtra("opponentLastName", game.opponentLastName);
                    startActivity(i);
                } else if (intentFrom.equalsIgnoreCase("OptionsController")) {
                    Intent i = new Intent(ReportABugController.this, OptionsController.class);
                    startActivity(i);
                }

            }
        });

    }

}
