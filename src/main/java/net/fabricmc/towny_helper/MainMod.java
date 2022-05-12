package net.fabricmc.towny_helper;

import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public class MainMod {

    private static Integer lookingX, lookingY, lookingZ;
    private static SortedMap<Double, String> closeTowns;
    private static ArrayList<Town> towns;
    private static ArrayList<Player> players;
    private static boolean isSupplyCrateForGaia = false;
    private static boolean isSupplyCrateForTerra = false;
    private static LocalTime lastSupplyCrate;
    private static String serverName = "";
    private static List<Map.Entry<Town, Double>> playerCloseTownsList;
    private static List<Map.Entry<Town, Double>> townCloseTownsList;
    private static Player lookingPlayer;
    private static Town lookingTown;
    private static int distance;
    public static boolean dynamicTracker;
    public static boolean isDynamicTrackerFirst;
    private static boolean isLooking;
    private static Identifier compassTexture;
    private static int compassTextureUID;
    public static MatrixStack matrices;
    public static float degrees;
    private static boolean playerHidden;

    public static long lastMillisTime;
    public static  long leftMillis;

    public static int getDistance() {
        return distance;
    }

    public static void setDistance(int distance) {
        MainMod.distance = distance;
    }

    public static Town getLookingTown() {
        return lookingTown;
    }

    public static boolean isPlayerHidden() {
        return playerHidden;
    }

    public static void setPlayerHidden(boolean playerHidden) {
        MainMod.playerHidden = playerHidden;
    }

    public static Identifier getCompassTexture() {
        return compassTexture;
    }

    public static void setCompassTexture(Identifier compassTexture) {
        MainMod.compassTexture = compassTexture;
    }

    public static int getCompassTextureUID() {
        return compassTextureUID;
    }

    public static void setCompassTextureUID(int compassTextureUID) {
        MainMod.compassTextureUID = compassTextureUID;
    }

    public static List<Map.Entry<Town, Double>> getTownCloseTownsList() {
        return townCloseTownsList;
    }

    public static void setTownCloseTownsList(List<Map.Entry<Town, Double>> townCloseTownsList) {
        MainMod.townCloseTownsList = townCloseTownsList;
    }

    public static void setLookingTown(Town lookingTown) {
        MainMod.lookingTown = lookingTown;
    }

    public static boolean isIsLooking() {
        return isLooking;
    }

    public static void setIsLooking(boolean isLooking) {
        MainMod.isLooking = isLooking;
    }

    public static Player getLookingPlayer() {
        return lookingPlayer;
    }

    public static void setLookingPlayer(Player lookingPlayer) {
        MainMod.lookingPlayer = lookingPlayer;
    }

    public static ArrayList<Player> getPlayers() {
        return players;
    }

    public static List<Map.Entry<Town, Double>> getPlayerCloseTownsList() {
        return playerCloseTownsList;
    }

    public static void setPlayerCloseTownsList(List<Map.Entry<Town, Double>> playerCloseTownsList) {
        MainMod.playerCloseTownsList = playerCloseTownsList;
    }

    public static void setPlayers(ArrayList<Player> players) {
        MainMod.players = players;
    }

    public static String getServerName() {
        return serverName;
    }

    public static void setServerName(String serverName) {
        MainMod.serverName = serverName;
    }

    public static LocalTime getLastSupplyCrate() {
        return lastSupplyCrate;
    }

    public static void setLastSupplyCrate(LocalTime lastSupplyCrate) {
        MainMod.lastSupplyCrate = lastSupplyCrate;
    }

    public static boolean isIsSupplyCrateForGaia() {
        return isSupplyCrateForGaia;
    }

    public static void setIsSupplyCrateForGaia(boolean isSupplyCrateForGaia) {
        MainMod.isSupplyCrateForGaia = isSupplyCrateForGaia;
    }

    public static boolean isIsSupplyCrateForTerra() {
        return isSupplyCrateForTerra;
    }

    public static void setIsSupplyCrateForTerra(boolean isSupplyCrateForTerra) {
        MainMod.isSupplyCrateForTerra = isSupplyCrateForTerra;
    }

    public static ArrayList<Town> getTowns() {
        return towns;
    }

    public static void setTowns(ArrayList<Town> towns) {
        MainMod.towns = towns;
    }


    public static Integer getLookingX() {
        return lookingX;
    }

    public static void setLookingX(Integer lookingX) {
        MainMod.lookingX = lookingX;
    }

    public static Integer getLookingY() {
        return lookingY;
    }

    public static void setLookingY(Integer lookingY) {
        MainMod.lookingY = lookingY;
    }

    public static void setXYZ(int lookingX,int lookingY, int lookingZ){
        MainMod.lookingY = lookingY;
        MainMod.lookingX = lookingX;
        MainMod.lookingZ = lookingZ;
    }

    public static Integer getLookingZ() {
        return lookingZ;
    }

    public static void setLookingZ(Integer lookingZ) {
        MainMod.lookingZ = lookingZ;
    }

    public static SortedMap<Double, String> getCloseTowns() {
        return closeTowns;
    }

    public static void setCloseTowns(SortedMap<Double, String> closeTowns) {
        MainMod.closeTowns = closeTowns;
    }
}
