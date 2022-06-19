package net.fabricmc.towny_helper.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.fabricmc.towny_helper.HttpsClient;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.api.ApiPayload;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.entity.Town;
import net.minecraft.util.math.Vec3d;


import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import static net.fabricmc.towny_helper.MainMod.lastMillisTime;
import static net.fabricmc.towny_helper.MainMod.leftMillis;

public class Service {
    Gson gson = new Gson();

    public static Service service;

    public static Service getInstance() {
        if (service == null) service = new Service();
        return service;
    }
// both players and towns will be updated and no idea why i added apidata as an argument
//    public void setPlayerAndTowns(String apiData){
//        HttpsClient httpsClient = new HttpsClient();
//        if (MainMod.getServerName().length() < 1) return;
//        String playerData = httpsClient.retrievePlayers(MainMod.getServerName());
//        String townsData = httpsClient.retrieveTowns(MainMod.getServerName());
//        ArrayList<Player> players = collectPlayer(playerData);
//        ArrayList<Town> towns = collectTowns(townsData);
//        MainMod.setTowns(towns);
//        MainMod.setPlayers(players);
//    }

    public void setPlayers(String apiData) {
        if (MainMod.getServerName().length() < 1) return;

        String playerData = (String) HttpsClient.getInstance().retrievePlayers(MainMod.getServerName()).getContent();
        ArrayList<Player> players = collectPlayer(playerData);
        MainMod.setPlayers(players);
    }

    public void setTowns(String apiData) {
        if (MainMod.getServerName().length() < 1) return;
        ApiPayload<String> response = HttpsClient.getInstance().retrieveTowns(MainMod.getServerName());
        if (response.isSuccess()) {
            String townsData = response.getContent();
            ArrayList<Town> towns = collectTownsNewMethod(townsData);
//        Collections.sort(towns, (town1,town2) ->{ town1.getName()>town2.getName() });

            if (towns != null) {
                towns.sort(Comparator.comparing(Town::getName));
                MainMod.setTowns(towns);
                MainMod.setOnlineData(true);
            }
        }
    }

    // collects and folds players data into java objects and returns string argument
    // accepts data from the url
    private ArrayList<Player> collectPlayer(String data) {
        String onlyListOfData = data.substring(data.indexOf("\"players\": ") + "\"players\": ".length(),
                data.indexOf(",\"isThundering\""));

        Player[] players = gson.fromJson(onlyListOfData, Player[].class);

        return new ArrayList<>(Arrays.asList(players));
    }

    // collects and folds towns data into java objects and returns string argument
    // accepts data from the url
    @Deprecated
    private ArrayList<Town> collectTowns(String data) {
        if (data == null) return null;

        data = "{" + data.substring(data.indexOf("\"markers\": {") + "\"markers\": {".length(),
                data.indexOf("},\"lines\":")) + "}";
        Type type = new TypeToken<Map<String, Map<String, Object>>>() {
        }.getType();
        Map<String, Map<String, Object>> map = gson.fromJson(data, type);
        ArrayList<Town> towns = new ArrayList<>();


        for (String key : map.keySet()) {
            towns.add(new Town(
                    String.valueOf(map.get(key).get("label")),
                    (int) (double)(map.get(key).get("x")),
                    (int) (double)(map.get(key).get("y")),
                    (int) (double)(map.get(key).get("z")),
                    String.valueOf(map.get(key).get("icon"))
            ));
        }

        return towns;
    }

    private ArrayList<Town> collectTownsNewMethod(String data) {
        if (data == null) return null;

        data = data.substring(data.indexOf("\"updates\": ") + "\"updates\": ".length(),
                data.lastIndexOf(",\"timestamp\":")
        );
        Type type = new TypeToken<ArrayList<Map<String, Object>>>() {
        }.getType();
        ArrayList<Map<String, Object>> maps = gson.fromJson(data, type);

        ArrayList<Town> towns = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            if (String.valueOf(map.get("msg")).equals("markerupdated"))
                towns.add(new Town(
                        String.valueOf(map.get("label")),
                        (int) (double)(map.get("x")),
                        (int) (double)map.get("y"),
                        (int) (double)map.get("z"),
                        String.valueOf(map.get("icon"))
                ));
        }
        return towns;
    }

    public static List<Entry<Town, Double>> computeClosePathTowns(Integer x, Integer y, Integer z, ArrayList<Town> towns, Integer limit) {
        HashMap<Town, Double> closeTownsList = new HashMap<>();

        for (Town town : towns) {
            Double cob = distanceCompute(x, z, town.getX(), town.getZ());
            closeTownsList.put(town, cob);
        }
        List<Entry<Town, Double>> entries = sortHashmapTown(closeTownsList);
        if (limit != null && entries.size() > limit)
            return entries.subList(0, limit);
        return entries.subList(0, 10);
    }

    public static double distanceCompute(Integer x1, Integer z1, Integer x2, Integer z2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(z1 - z2, 2));
    }


    private static List<Entry<Town, Double>> sortHashmapTown(HashMap<Town, Double> sortByValue) {
        Set<Entry<Town, Double>> entries = sortByValue.entrySet();

        Comparator<Entry<Town, Double>> valueComparator
                = Entry.comparingByValue();

        List<Entry<Town, Double>> listOfEntries
                = new ArrayList<>(entries);
        listOfEntries.sort(valueComparator);
        return listOfEntries;
    }


    public static int degreeToCompassTextureConverter(float degrees) {
        float direction = 360 - degrees;
        int shiftingNumber = 0;

        if (direction > 360 - 11.25 || direction < 11.25) {
            shiftingNumber = 1;
        } else if (direction > 11.25 && direction < 33.75) {
            shiftingNumber = 2;
        } else if (direction > 33.75 && direction < 56.25) {
            shiftingNumber = 3;
        } else if (direction > 56.25 && direction < 78.75) {
            shiftingNumber = 4;
        } else if (direction > 78.75 && direction < 101.25) {
            shiftingNumber = 5;
        } else if (direction > 101.25 && direction < 123.75) {
            shiftingNumber = 6;
        } else if (direction > 123.75 && direction < 146.25) {
            shiftingNumber = 7;
        } else if (direction > 146.25 && direction < 168.75) {
            shiftingNumber = 8;
        } else if (direction > 168.75 && direction < 191.25) {
            shiftingNumber = 9;
        } else if (direction > 191.25 && direction < 213.75) {
            shiftingNumber = 10;
        } else if (direction > 213.75 && direction < 236.25) {
            shiftingNumber = 11;
        } else if (direction > 236.25 && direction < 258.75) {
            shiftingNumber = 12;
        } else if (direction > 258.75 && direction < 281.25) {
            shiftingNumber = 13;
        } else if (direction > 281.25 && direction < 303.75) {
            shiftingNumber = 14;
        } else if (direction > 303.75 && direction < 326.25) {
            shiftingNumber = 15;
        } else if (direction > 326.25 && direction < 348.75) {
            shiftingNumber = 16;
        }

        return shiftingNumber;
    }

    public static double getQuarterDegrees(Vec3d pos, double degrees, float nYaw) {

        if (pos.z < MainMod.getLookingZ() && pos.x < MainMod.getLookingX()) {     // + +
            degrees = nYaw - (360 - degrees);
        } else if (pos.z > MainMod.getLookingZ() && pos.x < MainMod.getLookingX()) { // + -
            degrees = nYaw - (180 + degrees);
        } else if (pos.z > MainMod.getLookingZ() && pos.x > MainMod.getLookingX()) { // - -
            degrees = nYaw - (180 - degrees);
        } else if (pos.z < MainMod.getLookingZ() && pos.x > MainMod.getLookingX()) { // - +
            degrees = nYaw - (degrees);
        }
        return degrees;
    }

    public static ArrayList<Town> searchTownyByName(ArrayList<Town> townsList, String nameText) {
        ArrayList<Town> searchedTownsRank1 = new ArrayList<>();
        ArrayList<Town> searchedTownsRank2 = new ArrayList<>();
        ArrayList<Town> searchedTownsRank3 = new ArrayList<>();
        for (Town oneTown : townsList) {
            if (oneTown.getName().toLowerCase().equals(nameText.toLowerCase()))
                searchedTownsRank1.add(oneTown);
            else if (oneTown.getName().toLowerCase().startsWith(nameText.toLowerCase()))
                searchedTownsRank2.add(oneTown);
            else if (oneTown.getName().toLowerCase().contains(nameText.toLowerCase()))
                searchedTownsRank3.add(oneTown);
        }
        ArrayList<Town> searchTowns = new ArrayList<>();
        searchTowns.addAll(searchedTownsRank1);
        searchTowns.addAll(searchedTownsRank2);
        searchTowns.addAll(searchedTownsRank3);
        return searchTowns;
    }

    public static ArrayList<Player> searchPlayersByName(ArrayList<Player> playerList, String nameText) {
        ArrayList<Player> searchedPlayersRank1 = new ArrayList<>();
        ArrayList<Player> searchedPlayersRank2 = new ArrayList<>();
        ArrayList<Player> searchedPlayersRank3 = new ArrayList<>();
        for (Player onePlayer : playerList) {
            if (onePlayer.getName().toLowerCase().equals(nameText.toLowerCase()))
                searchedPlayersRank1.add(onePlayer);
            else if (onePlayer.getName().toLowerCase().startsWith(nameText.toLowerCase()))
                searchedPlayersRank2.add(onePlayer);
            else if (onePlayer.getName().toLowerCase().contains(nameText.toLowerCase()))
                searchedPlayersRank3.add(onePlayer);
        }
        ArrayList<Player> searchPlayers = new ArrayList<>();
        searchPlayers.addAll(searchedPlayersRank1);
        searchPlayers.addAll(searchedPlayersRank2);
        searchPlayers.addAll(searchedPlayersRank3);
        return searchPlayers;
    }

    public static int updateTracker(Player player) {
        if (!player.getWorld().equals("world")) return 1;
        if (MainMod.getTowns() == null) new Service().setTowns("0");
        List<Map.Entry<Town, Double>> entries = Service.computeClosePathTowns(player.getX(), player.getY(), player.getZ(), MainMod.getTowns(), 20);
        MainMod.setPlayerCloseTownsList(entries);
        MainMod.setXYZ(player.getX(), player.getY(), player.getZ());
        MainMod.setIsLooking(true);
        MainMod.setLookingPlayer(player);
        return 0;
    }

    public static void ticker() {
        if (!MainMod.dynamicTracker || !MainMod.isIsLooking()) return;
        if (MainMod.isDynamicTrackerFirst) {
            lastMillisTime = System.currentTimeMillis();
            MainMod.isDynamicTrackerFirst = false;
        }

        leftMillis = System.currentTimeMillis() - lastMillisTime;
        if (leftMillis > 10000) {
            if (leftMillis < 30000) {
                lastMillisTime = System.currentTimeMillis();
                refreshPlayerDataAndUpdateTracker();
//                System.out.println("800 tick counted in " + (++counter));
            } else {
                MainMod.dynamicTracker = false;
            }
        }
    }

    public static void refreshPlayerDataAndUpdateTracker() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                Service service = new Service();
                service.setPlayers("0");
                for (Player player : MainMod.getPlayers()) {
                    if (player.getName().equals(MainMod.getLookingPlayer().getName())) {
                        MainMod.setPlayerHidden(updateTracker(player) == 1);
                        return;
                    }
                }
                MainMod.setIsLooking(false);
            }
        }).start();
    }

}
