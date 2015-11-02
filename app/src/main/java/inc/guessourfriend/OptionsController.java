package inc.guessourfriend;

import android.os.Bundle;

public class OptionsController extends SlideNavigationController {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_options_controller, frameLayout);
        mDrawerList.setItemChecked(position, true);
        setTitle(listArray[position]);
    }
}
