package inc.guessourfriend.NetworkCommunication;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * A class so the network stuff can easily be run on a separate thread
 */
public class NetworkRequestRunner extends AsyncTask<JSONObject, String, JSONObject> {

    //Store whether this is a GET, POST, PUT, etc.
    private String httpMethod;

    //Store the URL for the request this runner will make
    private URL url = null;

    //Store an auth token if we have one
    private String authToken = null;

    public NetworkRequestRunner(String httpMethod, String url) {
        this.httpMethod = httpMethod;

        //Create a URL object pointing to the location of our method
        try {
            this.url = new URL(url);
        } catch (MalformedURLException ex) {
            //This can't really happen unless we mess up, but AndroidStudio will complain without it
            ex.printStackTrace();
        }
    }

    public NetworkRequestRunner(String httpMethod, String url, String authToken) {
        //Call the base constructor
        this(httpMethod, url);

        //Also set up the auth token
        this.authToken = authToken;
    }

    @Override
    protected JSONObject doInBackground(JSONObject... params) {
        //Set up a variable to return as our result
        String stringResult = null;

        //Make the request
        HttpURLConnection urlConnection = null;
        try {
            //Open the connection to be POSTed to
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(httpMethod);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");

            //Add an authentication header if there is one
            if (authToken != null) {
                urlConnection.setRequestProperty("Authorization", authToken);
            }

            //If we have content, write it
            if (params.length > 0) {
                //Allow output and connect
                urlConnection.setDoOutput(true);
                urlConnection.connect();

                //Set up a writer to write out our data
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream());
                writer.write(params[0].toString());
                writer.flush();
            }

            //TODO: Handle .getResponseCode() not being 200?
            //if (urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) { }

            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line + "\n");
            }
            reader.close();
            stringResult = builder.toString();
        } catch (IOException ex) {
            ex.printStackTrace();
            //TODO: Handle this error more appropriately
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        //If it is empty, return an empty JSONObject
        if (stringResult == null || stringResult.isEmpty()) {
            return new JSONObject();
        }

        //Parse the string result as JSON
        JSONObject jsonResult = null;
        try {
            jsonResult = new JSONObject(stringResult);
        } catch (JSONException ex) {
            //This shouldn't happen unless the server messes up and passes back non-JSON
            ex.printStackTrace();
        }

        //Return the parsed JSONObject
        return jsonResult;
    }
}
