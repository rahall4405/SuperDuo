package com.example.rahall.footballscores.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import com.example.rahall.footballscores.MainActivity;
import com.example.rahall.footballscores.Utilities;
import com.example.rahall.footballscores.data.DatabaseContract;
import com.example.rahall.footballscores.R;

/**
 * Created by yehya khaled on 3/2/2015.
 */
public class myFetchService extends IntentService
{   public static boolean data_ok;
    public static final String LOG_TAG = "myFetchService";
    public myFetchService()
    {
        super("myFetchService");
    }



    public static final String ACTION_DATA_UPDATED = "com.example.rahall.footballscores.ACTION_DATA_UPDATED";
    @Override
    protected void onHandleIntent(Intent intent)
    {
        data_ok = true;
        getData("n3");
        getData("p3");
        if (data_ok) {
            Intent messageIntent = new Intent(MainActivity.DONE_LOADING);
            messageIntent.putExtra(MainActivity.DONE_KEY, MainActivity.DONE_LOADING);
            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(messageIntent);
            updateWidgets();
        }
        return;
    }

    private void getData (String timeFrame)
    {
        //Creating fetch URL
        final String BASE_URL = "http://api.football-data.org/v1/fixtures"; //Base URL
        final String QUERY_TIME_FRAME = "timeFrame"; //Time Frame parameter to determine days
        //final String QUERY_MATCH_DAY = "matchday";

        Uri fetch_build = Uri.parse(BASE_URL).buildUpon().
                appendQueryParameter(QUERY_TIME_FRAME, timeFrame).build();
        //Log.v(LOG_TAG, "The url we are looking at is: "+fetch_build.toString()); //log spam
        HttpURLConnection m_connection = null;
        BufferedReader reader = null;
        String JSON_data = null;
        //Opening Connection
        try {
            URL fetch = new URL(fetch_build.toString());
            m_connection = (HttpURLConnection) fetch.openConnection();
            m_connection.setRequestMethod("GET");
            m_connection.addRequestProperty("X-Auth-Token",getString(R.string.api_key));
            m_connection.connect();

            // Read the input stream into a String
            InputStream inputStream = m_connection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return;
            }
            JSON_data = buffer.toString();
        }
        catch (Exception e)
        {
            data_ok= false;
            Log.e(LOG_TAG,"Exception here" + e.getMessage());
            return;
        }
        finally {
            if(m_connection != null)
            {
                m_connection.disconnect();
            }
            if (reader != null)
            {
                try {
                    reader.close();
                }
                catch (IOException e)
                {
                    data_ok= false;
                    Log.e(LOG_TAG,"Error Closing Stream");
                }
            }
        }
        try {
            if (JSON_data != null) {
                //This bit is to check if the data contains any matches. If not, we call processJson on the dummy data
                JSONArray matches = new JSONObject(JSON_data).getJSONArray("fixtures");
                // This is to get the link to the team, we will use this to get emblems.


                /*if (matches.length() == 0) {
                    //if there is no data, call the function on dummy data
                    //this is expected behavior during the off season.
                    processJSONdata(getString(R.string.dummy_data), getApplicationContext(), false);
                    return;
                }*/


                processJSONdata(JSON_data, getApplicationContext(), true);
            } else {
                //Could not Connect
                data_ok= false;
                Log.d(LOG_TAG, "Could not connect to server.");
            }
        }
        catch(Exception e)
        {
            Log.e(LOG_TAG,e.getMessage());
            data_ok = false;
            return;
        }
    }
    private void processJSONdata (String JSONdata,Context mContext, boolean isReal)
    {
        //JSON data
        // This set of league codes is for the 2015/2016 season. In fall of 2016, they will need to
        // be updated. Feel free to use the codes

        //more leagues were added for data to be available.
        final String BUNDESLIGA1 = "430";
        final String BUNDESLIGA2 = "431";
        final String LIGUE1 = "434";
        final String LIGUE2 = "435";
        final String PREMIER_LEAGUE = "426";
        final String PRIMERA_DIVISION = "436";
        final String SEGUNDA_DIVISION = "400";
        final String SERIE_A = "438";
        final String PRIMERA_LIGA = "439";
        final String Bundesliga3 = "403";
        final String EREDIVISIE = "404";
        final String CHAMPIONS_LEAGUE = "440";
        final String LEAGUE_ONE = "428";

        final String EURO_CHAMPIONSHIP = "425";


        final String SEASON_LINK = "http://api.football-data.org/v1/competitions/";
        final String MATCH_LINK = "http://api.football-data.org/v1/fixtures/";
        final String FIXTURES = "fixtures";
        final String LINKS = "_links";
        final String COMPETITION = "competition";

        final String SELF = "self";
        final String MATCH_DATE = "date";
        final String HOME_TEAM = "homeTeamName";
        final String AWAY_TEAM = "awayTeamName";
        final String HOME_TEAM_URL = "homeTeam";
        final String AWAY_TEAM_URL = "awayTeam";
        final String RESULT = "result";
        final String HOME_GOALS = "goalsHomeTeam";
        final String AWAY_GOALS = "goalsAwayTeam";
        final String MATCH_DAY = "matchday";

        //Match data
        String League = null;
        String mDate = null;
        String mTime = null;
        String Home = null;
        String Away = null;
        String Home_goals = null;
        String Away_goals = null;
        String match_id = null;
        String match_day = null;
        String HomeTeamUrl = null;
        String AwayTeamUrl = null;
        String LogoUrlHomeTeam;
        String LogoUrlAwayTeam;


        try {
            JSONArray matches = new JSONObject(JSONdata).getJSONArray(FIXTURES);


            //ContentValues to be inserted
            Vector<ContentValues> values = new Vector <ContentValues> (matches.length());
            for(int i = 0;i < matches.length();i++)
            {
                // add data for links to teams

                JSONObject match_data = matches.getJSONObject(i);
                League = match_data.getJSONObject(LINKS).getJSONObject(COMPETITION).
                        getString("href");
                League = League.replace(SEASON_LINK, "");
                HomeTeamUrl = match_data.getJSONObject(LINKS).getJSONObject(HOME_TEAM_URL).
                        getString("href");

                AwayTeamUrl = match_data.getJSONObject(LINKS).getJSONObject(AWAY_TEAM_URL).
                        getString("href");

                //This if statement controls which leagues we're interested in the data from.
                //add leagues here in order to have them be added to the DB.
                // If you are finding no data in the app, check that this contains all the leagues.
                // If it doesn't, that can cause an empty DB, bypassing the dummy data routine.
                if(     League.equals(PREMIER_LEAGUE)      ||
                        League.equals(SERIE_A)             ||
                        League.equals(BUNDESLIGA1)         ||
                        League.equals(BUNDESLIGA2)         ||
                        League.equals(PRIMERA_DIVISION)    ||
                        League.equals(CHAMPIONS_LEAGUE)    ||
                        League.equals(LEAGUE_ONE)) //         ||
                       // League.equals("357"))
                {
                    match_id = match_data.getJSONObject(LINKS).getJSONObject(SELF).
                            getString("href");
                    match_id = match_id.replace(MATCH_LINK, "");
                    if(!isReal){
                        //This if statement changes the match ID of the dummy data so that it all goes into the database
                        match_id=match_id+Integer.toString(i);
                    }

                    mDate = match_data.getString(MATCH_DATE);
                    mTime = mDate.substring(mDate.indexOf("T") + 1, mDate.indexOf("Z"));
                    mDate = mDate.substring(0, mDate.indexOf("T"));
                    SimpleDateFormat match_date = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
                    match_date.setTimeZone(TimeZone.getTimeZone("UTC"));
                    try {
                        Date parseddate = match_date.parse(mDate+mTime);
                        SimpleDateFormat new_date = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                        new_date.setTimeZone(TimeZone.getDefault());
                        mDate = new_date.format(parseddate);
                        mTime = mDate.substring(mDate.indexOf(":") + 1);
                        mDate = mDate.substring(0,mDate.indexOf(":"));

                        if(!isReal){
                            //This if statement changes the dummy data's date to match our current date range.
                            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
                            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
                            mDate=mformat.format(fragmentdate);
                        }
                    }
                    catch (Exception e)
                    {
                        data_ok = false;
                        Log.d(LOG_TAG, "error here!");
                        Log.e(LOG_TAG,e.getMessage());
                        return;
                    }



                    Home = match_data.getString(HOME_TEAM);
                    LogoUrlHomeTeam = getLogoLocation(HomeTeamUrl, Home);
                    Away = match_data.getString(AWAY_TEAM);
                    LogoUrlAwayTeam = getLogoLocation(AwayTeamUrl, Away);
                    Home_goals = match_data.getJSONObject(RESULT).getString(HOME_GOALS);
                    Away_goals = match_data.getJSONObject(RESULT).getString(AWAY_GOALS);
                    match_day = match_data.getString(MATCH_DAY);
                    ContentValues match_values = new ContentValues();
                    match_values.put(DatabaseContract.scores_table.MATCH_ID,match_id);
                    match_values.put(DatabaseContract.scores_table.DATE_COL,mDate);
                    match_values.put(DatabaseContract.scores_table.TIME_COL,mTime);
                    match_values.put(DatabaseContract.scores_table.HOME_COL,Home);
                    match_values.put(DatabaseContract.scores_table.AWAY_COL,Away);
                    match_values.put(DatabaseContract.scores_table.HOME_GOALS_COL,Home_goals);
                    match_values.put(DatabaseContract.scores_table.AWAY_GOALS_COL,Away_goals);
                    match_values.put(DatabaseContract.scores_table.LEAGUE_COL,League);
                    match_values.put(DatabaseContract.scores_table.MATCH_DAY,match_day);
                    // added storage location or URL
                    match_values.put(DatabaseContract.scores_table.HOME_TEAM_LOGO_URL,LogoUrlHomeTeam);
                    match_values.put(DatabaseContract.scores_table.AWAY_TEAM_LOGO_URL,LogoUrlAwayTeam);







                    values.add(match_values);
                }
            }
            int inserted_data = 0;
            ContentValues[] insert_data = new ContentValues[values.size()];
            values.toArray(insert_data);
            inserted_data = mContext.getContentResolver().bulkInsert(
                    DatabaseContract.BASE_CONTENT_URI,insert_data);

            //Log.v(LOG_TAG,"Succesfully Inserted : " + String.valueOf(inserted_data));
        }
        catch (JSONException e)
        {
            data_ok = false;
            Log.e(LOG_TAG,e.getMessage());
            return;

        } catch (Exception e) {
            Log.e(LOG_TAG,e.getMessage());
            data_ok = false;
            return;
        }

    }
    // get the Logo if we do not have it.  If we can't store it we will return a url.
    // This will store the logo locally. If there is an error the user will just see a blank logo.
    // I do not inform the user of errors.  In a few passes all the available logos will be available.
    private String getLogoLocation (String teamUrl, String teamName) {
        String fileName;
        String crestUrlString = "";


            teamName = teamName.replace(" ", "_") + ".png";

        fileName = Utilities.getApplicationDirectory() + "/image_files/" + teamName;
        if (Utilities.doesFileExist(fileName)) {
            return fileName;
        } else {
            HttpURLConnection m_connection = null;
            BufferedReader reader = null;

            String JSON_data = null;
            try {
                ///URL fetch

                URL fetch = new URL(teamUrl);
                m_connection = (HttpURLConnection) fetch.openConnection();
                m_connection.setRequestMethod("GET");
                m_connection.addRequestProperty("X-Auth-Token",getString(R.string.api_key));
                m_connection.connect();
                InputStream inputStream = m_connection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                JSON_data = buffer.toString();

            } catch (Exception e) {
                Log.e(LOG_TAG, "Exception here" + e.getMessage());
                return "";
            }
                if (m_connection != null) {
                    m_connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error Closing Stream");
                    }
                }


            try {
                if (JSON_data != null) {
                    String tempString = new JSONObject(JSON_data).getString("crestUrl");

                    //String tempString = crestUrl.toString();
                    if (tempString.contains(".svg")) {
                        crestUrlString = Utilities.fixUrlIfSvg(tempString);
                    } else {
                        crestUrlString = tempString;
                    }
                }


            } catch(Exception e) {
                Log.e(LOG_TAG,e.getMessage());
                return "";


            }
                if (Utilities.isExternalStorageAvailable() && crestUrlString != null && crestUrlString != "null") {
                    Log.d("A_URL",crestUrlString + "   " + teamName);
                    Utilities.downloadImageFile(this,crestUrlString,teamName);
                    return Utilities.getApplicationDirectory() + "/image_files/" + teamName;
                } else {
                    return crestUrlString;
                }



        }
    }
    private void updateWidgets() {
        Context context = getApplicationContext();
        // Setting the package ensures that only components in our app will receive the broadcast
        Intent dataUpdatedIntent = new Intent(ACTION_DATA_UPDATED)
                .setPackage(context.getPackageName());
        context.sendBroadcast(dataUpdatedIntent);
    }
}

