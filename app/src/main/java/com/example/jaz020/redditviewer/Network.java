package com.example.jaz020.redditviewer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import android.util.Log;

/**
 * This class shall serve as a utility class that handles network
 * connections.
 *
 * @author Hathy
 */
public class Network {

    /**
     * This methods returns a Connection to the specified URL,
     * with necessary properties like timeout and user-agent
     * set to your requirements.
     *
     * @param url
     * @return
     */
    public static HttpURLConnection getConnection(String url){
        Log.d("HTTP Connectoin", "URL: " + url);
        HttpURLConnection connection = null;
        try {
            connection=(HttpURLConnection)new URL(url).openConnection();
            connection.setReadTimeout(30000); // Timeout at 30 seconds
            connection.setRequestProperty("User-Agent", "Reddit Viewer");
        } catch (MalformedURLException e) {
            Log.e("getConnection()",
                    "Invalid URL: "+e.toString());
        } catch (IOException e) {
            Log.e("getConnection()",
                    "Could not connect: "+e.toString());
        }
        return connection;
    }


    //TODO rewrite
    /**
     * A very handy utility method that reads the contents of a URL
     * and returns them as a String.
     *
     * @param url
     * @return
     */
    public static String readContents(String url){
        HttpURLConnection  hcon = getConnection(url);
        if(hcon==null) return null;
        try{
            StringBuffer sb=new StringBuffer(8192);
            String tmp="";
            BufferedReader br=new BufferedReader(
                    new InputStreamReader(
                            hcon.getInputStream()
                    )
            );
            while((tmp=br.readLine())!=null)
                sb.append(tmp).append("\n");
            br.close();
            return sb.toString();
        }catch(IOException e){
            Log.d("READ FAILED", e.toString());
            return null;
        }
    }

    // This method lets you POST data to the URL.
    public static boolean writeToConnection(HttpURLConnection con, String data){
        try{

            //set connection to POST (required by Reddit)
            con.setRequestMethod("POST");

            PrintWriter pw = new PrintWriter(
                    new OutputStreamWriter(
                            con.getOutputStream()
                    )
            );
            pw.write(data);
            pw.close();
            return true;
        }catch(IOException e){
            Log.d("Unable to write", e.toString());
            return false;
        }
    }
}
