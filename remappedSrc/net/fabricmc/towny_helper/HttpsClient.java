package net.fabricmc.towny_helper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpsClient{

    public String getMarkets(String serverName){

        String https_url = "https://"+serverName+".herobrine.org/tiles/_markers_/marker_world.json";
//        String https_url = "https://www.google.com";
        URL url;
        try {

            url = new URL(https_url);
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("User-Agent", "Mozilla 5.0 (Windows; U; "
                    + "Windows NT 5.1; en-US; rv:1.8.0.11) ");


            //dump all the content
          return return_content((HttpsURLConnection) urlc);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    private String return_content(HttpsURLConnection con){
        if(con!=null){
            StringBuilder content = new StringBuilder(new StringBuilder());
            try {

                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));

                String input;

                while ((input = br.readLine()) != null){
                    content.append(input);
                }
                br.close();

                content = new StringBuilder("{"+content.substring(content.indexOf("\"markers\": {")+"\"markers\": {".length(),
                        content.indexOf("},\"lines\":"))+"}");

                return content.toString().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
