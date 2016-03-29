package com.example.rahall.footballscores;

import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.text.format.Time;

import com.example.rahall.footballscores.service.myFetchService;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by rahall4405 on 3/13/16.
 */
public class Utilities {
    /*public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;*/
    public static final int SERIE_A = 401;
    public static final int PREMIER_LEGAUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int CHAMPIONS_LEAGUE = 405;
    public static final int BUNDESLIGA = 394;
    public static final int BUNDESLIGA_1 = 395;
    public static final int LIGUE_ONE = 396;
    public static final int LIGUE_TWO = 397;
    public static final int SEGUNDA_DIVISION= 400;
    public static final int PRIMEIRA_LIGA= 402;
    public static final int BUNDESLIGA_2 = 403;
    public static final int EREDIVISIE = 404;
    public static final int LEAGUE_ONE = 425;
    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return "Seria A";
            case PREMIER_LEGAUE : return "Premier League";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case BUNDESLIGA : return "Bundesliga";
            case LIGUE_ONE : return "Ligue 1";
            case LIGUE_TWO : return "Ligue 2";
            case SEGUNDA_DIVISION : return "Segunda Division";
            case PRIMEIRA_LIGA : return "Primeira Liga";
            case BUNDESLIGA_1 : return "Bundesliga";
            case BUNDESLIGA_2 : return "Bundesliga";
            case EREDIVISIE : return "Eredivisie";
            case LEAGUE_ONE : return "League One";

            default: return "Not known League Please report";
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return "Group Stages, Matchday : 6";
            }
            else if(match_day == 7 || match_day == 8)
            {
                return "First Knockout round";
            }
            else if(match_day == 9 || match_day == 10)
            {
                return "QuarterFinal";
            }
            else if(match_day == 11 || match_day == 12)
            {
                return "SemiFinal";
            }
            else
            {
                return "Final";
            }
        }
        else
        {
            return "Matchday : " + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "RCD Espanyol" : return R.drawable.rcd_espanyol_de_barcelona;
            case "SC Freiburg" : return R.drawable.sc_freiburg_logo_neu;
            case "Karlsruher SC" : return R.drawable.karlsruher_sc_logo;
            case "Sheffield United FC" : return R.drawable.sheffield_united_fc_logo;
            case "Crewe Alexandra FC" : return R.drawable.crewe_alexandra;
            case "Rochdale AFC" : return R.drawable.rochdale_badge;
            case "Southend United FC" : return R.drawable.southend_united;
            case "Colchester United FC" : return R.drawable.colchester_united_fc_logo;
            case "Doncaster Rovers FC" : return R.drawable.doncaster_rovers;
            case "Blackpool FC" : return R.drawable.blackpool_fc_logo;
            case "Bury FC" : return R.drawable.bury_fc;
            case "Barnsley FC" : return R.drawable.barnsley_fc;
            case "Scunthorpe United FC" : return R.drawable.scunthorpe_united_fc_logo;
            case "Shrewsbury Town FC" : return R.drawable.no_icon;
            case "Port Vale FC" : return R.drawable.port_vale_logo;
            case "Swindon Town FC" : return R.drawable.swindon_town_fc;
            case "Wigan Athletic FC" : return R.drawable.wigan_athletic;
            case "Peterborough United FC" : return R.drawable.peterborough_united;
            case "Coventry City FC" : return R.drawable.coventry_city_fc_logo;

            default: return R.drawable.no_icon;
        }
    }
    public static void showAlertDialog(String message, Context context) {

        new AlertDialog.Builder(context)
                .setTitle(context.getResources().getString(R.string.app_name))
                .setCancelable(false)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .show();
    }
    public static boolean isTablet(Context context
    ) {
        return ( context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) return false;
        NetworkInfo.State network = info.getState();
        return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
    }
    public static CharSequence getTabName(Context context, int position)
    {
        return getDayName(context,System.currentTimeMillis()+((position-2)*86400000));
    }

    public static String getDayName(Context context, long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        }
        else if ( julianDay == currentJulianDay -1)
        {
            return context.getString(R.string.yesterday);
        }
        else
        {
            Time time = new Time();
            time.setToNow();
            // Otherwise, the format is just the day of the week (e.g "Wednesday".
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }

    public static void adjustUrlToGetPngFile(String urlString) {

    }

    public static String downloadImageFile(Context context, String urlString, String teamName) {


        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlString));
        if (getApplicationDirectory() != null) {
            File pathDir = new File(getApplicationDirectory() + "/image_files");
            pathDir.mkdirs();
            int postionLastSlash = urlString.lastIndexOf('/');
            int postitionfileType = urlString.length() - 3;
            String fileExtension = urlString.substring(postitionfileType, urlString.length());
            String fileName = urlString.substring(postionLastSlash, urlString.length());
            fileName = fileName.replace(".svg","");
            String outputFile = "/" + "/image_files" + "/" + teamName;
            request.setDestinationInExternalFilesDir(context, "", outputFile);
            long enqueue = dm.enqueue(request);
            return getApplicationDirectory() + outputFile;
        }
        return "";
    }
    public static String getApplicationDirectory() {
        boolean isSdPresent = android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
        if(isSdPresent) {

            return FootballApplication.getInstance().getAppContext().getExternalFilesDir(null).toString();
        } else {
            return null;
        }
    }

    public static boolean isExternalStorageAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
    public static boolean doesFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }
// conversion documented at https://meta.wikimedia.org/wiki/SVG_image_support
    public static String fixUrlIfSvg(String UrlString) {
        String svgName = UrlString.substring(UrlString.lastIndexOf("/")+ 1, UrlString.length());
        int toEndWikipediaInt = UrlString.indexOf("wikipedia/") + 10;

        String toEndWikipedia = UrlString.substring(0,toEndWikipediaInt);
        String fromEndWikipedia = UrlString.substring(toEndWikipediaInt);

        String toStartThumb = fromEndWikipedia.substring(0, fromEndWikipedia.indexOf("/"));
        String partAfterThumb = fromEndWikipedia.substring(fromEndWikipedia.indexOf("/"), fromEndWikipedia.length());
        // 144px was the max size in our  samples,  we will use this
        String lastPart = "/144px-" + svgName + ".png";
        return toEndWikipedia + toStartThumb + "/thumb" + partAfterThumb + lastPart;

    }
    public static void update_scores(Context context) {
        Intent service_start = new Intent(context, myFetchService.class);
        context.startService(service_start);
    }

}