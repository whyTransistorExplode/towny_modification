package net.fabricmc.towny_helper;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class HttpsClient{

    public String retrievePlayers(String serverName){
        String https_url = "https://www.herobrine.org/map/"+serverName+"/standalone/dynmap_world.json";
        return getDataFromWeb(https_url);
    }

    public String retrieveTowns(String serverName){
        String https_url = "https://www.herobrine.org/map/"+serverName+"/tiles/_markers_/marker_world.json";
        return getDataFromWeb(https_url);
    }



    public String getDataFromWeb(String https_url){
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

                return content.toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;

    }

}
