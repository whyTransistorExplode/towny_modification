package net.fabricmc.towny_helper.entity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.towny_helper.HttpsClient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;

public class MarkerTown {
    public ArrayList<Town> getTowns(String serverName){
        HttpsClient httpsClient = new HttpsClient();
        String markets = httpsClient.getMarkets(serverName);

        Gson gson = new Gson();
        Type type = new TypeToken<Map<String, Map<String, Object>>>() {}.getType();
        Map<String, Map<String, Object>> map = gson.fromJson(markets, type);
        ArrayList<Town> towns = new ArrayList<>();

        for (String key : map.keySet()) {
            towns.add(new Town(String.valueOf(map.get(key).get("label")),
                    Integer.valueOf(String.valueOf(map.get(key).get("x")).substring(0,String.valueOf(map.get(key).get("x")).indexOf("."))),
                    Integer.valueOf(String.valueOf(map.get(key).get("y")).substring(0,String.valueOf(map.get(key).get("y")).indexOf("."))),
                    Integer.valueOf(String.valueOf(map.get(key).get("z")).substring(0,String.valueOf(map.get(key).get("z")).indexOf(".")))
                    ));
        }
        return towns;
    }
}
