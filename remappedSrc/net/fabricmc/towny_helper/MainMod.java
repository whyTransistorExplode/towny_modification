package net.fabricmc.towny_helper;

import net.fabricmc.towny_helper.entity.Town;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.SortedMap;

public class MainMod {

    private static Integer supplyX, supplyY, supplyZ;
    private static SortedMap<Double, String> closeTowns;
    private static ArrayList<Town> towns;
    private static boolean isSupplyCrateForGaia = false;
    private static boolean isSupplyCrateForTerra = false;
    private static LocalTime lastSupplyCrate;
    private static String serverName = "";

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

    public static Integer getSupplyX() {
        return supplyX;
    }

    public static void setSupplyX(Integer supplyX) {
        MainMod.supplyX = supplyX;
    }

    public static Integer getSupplyY() {
        return supplyY;
    }

    public static void setSupplyY(Integer supplyY) {
        MainMod.supplyY = supplyY;
    }

    public static Integer getSupplyZ() {
        return supplyZ;
    }

    public static void setSupplyZ(Integer supplyZ) {
        MainMod.supplyZ = supplyZ;
    }

    public static SortedMap<Double, String> getCloseTowns() {
        return closeTowns;
    }

    public static void setCloseTowns(SortedMap<Double, String> closeTowns) {
        MainMod.closeTowns = closeTowns;
    }

    public static void computeClosePathTowns(Integer supplyX, Integer supplyY, Integer supplyZ, ArrayList<Town> towns, SortedMap<Double, String> bin) {
        for (Town town : towns) {

            double cob = Math.sqrt(Math.multiplyExact(supplyX - town.getX(), supplyX - town.getX()) + Math.multiplyExact(supplyZ - town.getZ(), supplyZ - town.getZ()));
            bin.put(cob, town.getName());
        }

        MainMod.setCloseTowns(bin);
        MainMod.setSupplyX(supplyX);
        MainMod.setSupplyY(supplyY);
        MainMod.setSupplyZ(supplyZ);
    }

}
