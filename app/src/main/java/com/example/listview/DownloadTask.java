package com.example.listview;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @author lynn
 *
 */
public class DownloadTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "DownloadTask";
    private static final int BUFFER_SIZE = 8096;
    Activity_ListView myActivity;

    // 1 second
    private static final int TIMEOUT = 1000;
    private String myQuery = "bikes.json";
    String myURL;
    int statusCode = 0;

    DownloadTask(Activity_ListView activity) {
        attach(activity);
    }

    //
    /**
     * @param name
     * @param value
     * @return this allows you to build a safe URL with all spaces and illegal
     *         characters URLEncoded usage mytask.setnameValuePair("param1",
     *         "value1").setnameValuePair("param2",
     *         "value2").setnameValuePair("param3", "value3")....
     */
    public DownloadTask setnameValuePair(String name, String value) {
        try {
            if (name.length() != 0 && value.length() != 0) {

                // if 1st pair that include ? otherwise use the joiner char &
                if (myQuery.length() == 0)
                    myQuery += "?";
                else
                    myQuery += "&";

                myQuery += name + "=" + URLEncoder.encode(value, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return this;
    }

    /*
    Runs when execute() is called
    Takes a URL for the JSON file and downloads it as a String
    @param: String... params, The URL(s) for the JSON file
    @return: String, The JSON file as a String
     */
    @Override
    protected String doInBackground(String... params) {
        // site we want to connect to
        myURL = params[0];

        try {
            URL url = new URL(myURL + myQuery);

            // this does no network IO
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // can further configure connection before getting data
            // cannot do this after connected
            connection.setRequestMethod("GET");
            connection.setReadTimeout(TIMEOUT);
            connection.setConnectTimeout(TIMEOUT);
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            // this opens a connection, then sends GET & headers

            // wrap in finally so that stream bis is sure to close
            // and we disconnect the HttpURLConnection
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()), BUFFER_SIZE);
            try {
                connection.connect();

                // lets see what we got make sure its one of
                // the 200 codes (there can be 100 of them
                // http_status / 100 != 2 does integer div any 200 code will = 2
                statusCode = connection.getResponseCode();
                if (statusCode / 100 != 2) {
                    Log.e(TAG, "Error-connection.getResponseCode returned "
                            + Integer.toString(statusCode));
                    return null;
                }

                // the following buffer will grow as needed
                String myData;
                StringBuffer sb = new StringBuffer();

                while ((myData = in.readLine()) != null) {
                    sb.append(myData);
                }
                return sb.toString();

            } finally {
                // close resource no matter what exception occurs
                if(in != null) {
                    in.close();
                    connection.disconnect();
                }
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            return null;
        }
    }

    /*
    Called when doInBackground() is finished
    Takes a String of JSON data and passes it to bindData()
    @param: String result, The JSON data downloaded
     */
    @Override
    protected void onPostExecute(String result) {
        this.myActivity.bindData(result);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.os.AsyncTask#onCancelled(java.lang.Object)
     */
    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    /**
     * important do not hold a reference so garbage collector can grab old
     * defunct dying activity
     */
    void detach() {
        myActivity = null;
    }

    /**
     * @param activity
     *            grab a reference to this activity, mindful of leaks
     */
    void attach(Activity_ListView activity) {
        this.myActivity = activity;
    }

};

