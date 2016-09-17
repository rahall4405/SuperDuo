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
    public static final int PRIMIER_LEAGUE = 426;
    public static final int LEAGUE_ONE = 428;
    public static final int BUNDESLIGA_1 = 430;
    public static final int BUNDESLIGA_2 = 431;
    public static final int DFB = 432;
    public static final int DED = 433;
    public static final int LIGUE_ONE = 434;
    public static final int LIGUE_TWO = 435;
    public static final int PRIMERA_DIVISION = 436;
    public static final int SD = 437;
    public static final int SERIE_A = 438;
    public static final int PRIMEIRA_LIGA= 439;
    public static final int CHAMPIONS_LEAGUE = 440;






    public static String getLeague(int league_num)
    {
        switch (league_num)
        {
            case PRIMIER_LEAGUE : return "Premier League";
            case SERIE_A : return "Seria A";
            case PRIMEIRA_LIGA: return "Premeira Liga";
            case CHAMPIONS_LEAGUE : return "UEFA Champions League";
            case PRIMERA_DIVISION : return "Primera Division";
            case LIGUE_ONE : return "Ligue 1";
            case LIGUE_TWO : return "Ligue 2";
            case BUNDESLIGA_1 : return "Bundesliga 1";
            case BUNDESLIGA_2 : return "Bundesliga 2";
            case LEAGUE_ONE : return "League One";
            case DFB : return "DFB-Pokal";
            case DED : return "Eredivisie 2016/17";
            case SD : return "Liga Adelante";


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
