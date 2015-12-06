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
        setTitle(listArray[position]);
        Button button = (Button) findViewById(R.id.report_bug_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                EditText bugTitle = (EditText) findViewById(R.id.bug_title);
                EditText bugContent = (EditText) findViewById(R.id.bug_content);
                String bugTitleString = bugTitle.getText().toString();
                String bugContentString = bugContent.getText().toString();
                NetworkRequestHelper.sendBugReport(bugTitleString, bugContentString);
                Toast.makeText(getApplicationContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ReportABugController.this, OptionsController.class);
                startActivity(intent);
            }
        });

    }

}
