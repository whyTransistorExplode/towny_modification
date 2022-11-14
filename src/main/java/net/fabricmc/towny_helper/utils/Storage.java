package net.fabricmc.towny_helper.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.api.ApiPayload;
import net.fabricmc.towny_helper.entity.InfoTown;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.component.FavouritePlayers;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Storage {
    public static final String BASE_PATH = MinecraftClient.getInstance().runDirectory.getPath() + "/T_Settings/";
    public static final String DATA_PATH = BASE_PATH + "map_data/";
    public static final String ALL_TOWNS_SUFFIX = "_AllTowns.TDat";
    public static final String BLACKED_TOWNS_SUFFIX = "_BTowns_JS.dat";
    public static final String WHITE_TOWNS_SUFFIX = "_WTowns_JS.dat";
    public static final String FAVOURITE_PLAYERS_SUFFIX = "_FavPl.dat";
    public static final String BLACKED_PLAYERS_SUFFIX = "_BlackPl.dat";

    private ArrayList<Town> blackedTowns = null;
    public boolean blackedTownsStateChange = false;

    private ArrayList<Town> whitedTowns = null;
    public boolean whiteTownStateChange = false;
    private ArrayList<Player> favouritePlayers = null;

    private ArrayList<InfoTown> dataInfo = null;
    private FileWriter fileWriter;


    private static final Storage instance = new Storage();

    public static Storage getInstance() {
        return instance;
    }

    private Storage() {
    }

    private void setBlackListedTownsFromFile(String serverName) {
        try {
            blackedTowns = new ArrayList<>(Arrays.asList(getTownsAsObject(BASE_PATH + serverName + BLACKED_TOWNS_SUFFIX)));
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, BLACKED_TOWNS_SUFFIX));
            blackedTowns = new ArrayList<>();
        }
    }

    private void setWhiteListedTownsFromFile(String serverName) {
        try {
            whitedTowns = new ArrayList<>(Arrays.asList(getTownsAsObject(BASE_PATH + serverName + WHITE_TOWNS_SUFFIX)));
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, WHITE_TOWNS_SUFFIX));
            whitedTowns = new ArrayList<>();
        }
    }

    //file taker
    private String getStringData(String path) throws FileNotFoundException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    private void wipeDataFromFile(String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Town[] getTownsAsObject(String path) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            String hold = getStringData(path);
            return gson.fromJson(hold, Town[].class);
        } catch (JsonSyntaxException e) {
            wipeDataFromFile(path);
            return new Town[10];
        }
    }
    public void reloadBlackAndWhiteTowns(String serverName) {
        if (serverName != null) {
            setWhiteListedTownsFromFile(serverName);
            setBlackListedTownsFromFile(serverName);
        } else System.err.println("server name is not present");
    }

    public void removeTownBlacked(Town town) {
        for (int i = 0; i < blackedTowns.size(); i++) {
            if (town.getName().equals(blackedTowns.get(i).getName())) {
                blackedTowns.remove(i);
                stateChangeBlackTown();
                break;
            }
        }
    }


    public String filePathCombiner(String serverName, String suffix) {
        return BASE_PATH + serverName + suffix;
    }

    public void removeTownWhited(Town town) {
        for (int i = 0; i < whitedTowns.size(); i++) {
            if (town.getName().equals(whitedTowns.get(i).getName())) {
                whitedTowns.remove(i);
                stateChangeWhiteTown();
                break;
            }
        }
    }

    public boolean addBlackedTown(Town town) {
        if (blackedTowns == null) {
            setBlackListedTownsFromFile(MainMod.getServerName());
        }
        if (town != null && town.getName().length() > 0) {
            for (Town blackedTown : blackedTowns) {
                if (blackedTown.getName().equals(town.getName())) return false;
            }
            blackedTowns.add(town);
            stateChangeBlackTown();
            return true;
        }
        return false;
    }

    public boolean addWhiteTown(Town town) {
        if (whitedTowns == null) {
            setWhiteListedTownsFromFile(MainMod.getServerName());
        }
        if (town != null && town.getName().length() > 0) {
            for (Town wTown : whitedTowns) {
                if (wTown.getName().equals(town.getName())) return false;
            }
            whitedTowns.add(town);
            stateChangeWhiteTown();
//                listWriteToFileGSON(whitedTowns, filePathCombiner(MainMod.getServerName(), WHITE_TOWNS_SUFFIX));
            return true;
        }
        return false;
    }

    private void stateChangeWhiteTown() {
        whiteTownStateChange = true;
    }

    private void stateChangeBlackTown() {
        blackedTownsStateChange = true;
    }


    @Deprecated
    private void listWriteToFile(ArrayList<String> listArray, String path) throws IOException {
        fileWriter = new FileWriter(path);
        StringBuilder s = new StringBuilder();
        for (String townName : listArray) {
            if (listArray.get(listArray.size() - 1).equals(townName))
                s.append(townName);
            else
                s.append(townName).append("\n");
        }
        fileWriter.write(s.toString());
        fileWriter.flush();
        fileWriter.close();
    }


    /**
     * main list file writer
     *
     * @param townsList a list to be written to a specific location
     * @param path      a variable for a specific location
     * @throws IOException if any interruption happens when writing to a local file
     */
    private void listWriteToFileGSON(ArrayList<?> townsList, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) createFolderAndFiles(path);

        fileWriter = new FileWriter(path);
        Gson gson = new Gson();
        String objectData = gson.toJson(townsList);
        fileWriter.write(objectData);
        fileWriter.close();
    }

    /**
     * this method only to be used by a thread that is thrown in @InGameHudMixin class
     *
     * @param townsList a list of data that is to be saved
     * @param path      a parameter path in local storage path
     * @throws IOException if any interruption happens when writing to a local file
     */
    public void threadListWriteGSON(ArrayList<Town> townsList, String path) throws IOException {
        listWriteToFileGSON(townsList, path);
    }

    public void createFolderAndFiles(String path) {

        File mkFolder = new File(BASE_PATH);
        if (!mkFolder.mkdir()) System.out.println("failed to create folder!");

        new File(DATA_PATH).mkdir();
        File FILE = new File(path);

        try {
            if (FILE.createNewFile()) {
                System.out.println("created " + path + "  file");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    // save DATA section

    public ApiPayload<?> saveOnlineDataTowns() {
        if (MainMod.getOnlineData() != null && MainMod.getOnlineData()) {
            String dateFormat = new SimpleDateFormat("yyyy_MM_dd_hhmmss").format(new Date());
            try {
                listWriteToFileGSON(MainMod.getTowns(), DATA_PATH + dateFormat + ALL_TOWNS_SUFFIX);
                return ApiPayload.sendSuccessful();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ApiPayload.sendFail();
    }

    /**
     * method is used to load townsData only to show user how many data are stored in local storage
     *
     * @return returns success and data if the action is successful
     */
    public ApiPayload<ArrayList<InfoTown>> loadTownsDataInfos() {
        File file = new File(DATA_PATH);
        File[] files = file.listFiles();
        dataInfo = new ArrayList<>();
        if (files != null) {
            for (File loopFile : files) {

                if (loopFile.isFile() && loopFile.getName().endsWith(ALL_TOWNS_SUFFIX)) {
                    dataInfo.add(new InfoTown(loopFile.getName().substring(0, loopFile.getName().length() - ALL_TOWNS_SUFFIX.length()), (int) (loopFile.length() / 1024)));
                }
            }

            return ApiPayload.sendSuccessWithObject(dataInfo);
        }
        return ApiPayload.sendFail();
    }

    /**
     * uses method above and checks if data info is present it just returns it otherwise calls for local storage check
     *
     * @return list of files that are present in a specific storage path
     */
    public ApiPayload<ArrayList<InfoTown>> getTownsDataInfosLocal() {
        if (this.dataInfo == null)
            return loadTownsDataInfos();
        return ApiPayload.sendSuccessWithObject(dataInfo);
    }

    /**
     * the method is used to load local stored data of Towns into Main Towns and sets the MainMod.online= true
     *
     * @param filePath given path location of the file to be loaded
     * @return returns success if the action is successful
     */
    private ApiPayload loadTownsDataIntoMain(String filePath) {
        try {
            MainMod.setTowns(new ArrayList<Town>(Arrays.asList(getTownsAsObject(filePath))));
            MainMod.setOnlineData(false); // local data
            return ApiPayload.sendSuccessful();
        } catch (FileNotFoundException e) {
            return ApiPayload.sendFail();
        }
    }

    public ApiPayload<?> loadLocalDataToMain(String infoName) {
        return loadTownsDataIntoMain(DATA_PATH + infoName + ALL_TOWNS_SUFFIX);
    }


    public void removeData(String infoName) {
        File file = new File(DATA_PATH + infoName + ALL_TOWNS_SUFFIX);
        if (file.exists() && file.isFile())
            file.delete();
    }

    // end of DATA section


    public ArrayList<Town> getBlackedTowns() {
        if (blackedTowns == null) blackedTowns = new ArrayList<>();
        return blackedTowns;
    }


    public ArrayList<Town> getWhiteTowns() {
        if (whitedTowns == null) whitedTowns = new ArrayList<>();
        return whitedTowns;
    }


    // favourite players space
    public ArrayList<Player> getFavouritePlayersList() {
        if (favouritePlayers == null) loadFavouritePlayersIntoMain();
        return favouritePlayers;
    }

    private Player[] getPlayersAsObjects(String path){
        Gson gson = new Gson();
        try {
            String holder = getStringData(path);
            return gson.fromJson(holder, Player[].class);
        } catch (JsonSyntaxException | FileNotFoundException e) {
            wipeDataFromFile(path);
            return new Player[10];
        }
    }
    public void loadFavouritePlayersIntoMain(){
            favouritePlayers = new ArrayList<>(Arrays.asList(getPlayersAsObjects(filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX))));
    }

    public void addFavPlayer(Player e) {

        if (favouritePlayers.contains(e)) return;

        favouritePlayers.add(e);
        try {
            listWriteToFileGSON(favouritePlayers, filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
        } catch (IOException ioException) {
            createFolderAndFiles(filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
        }
    }
    public void removeFavPlayer(Player e) {

        int i = favouritePlayers.indexOf(e);
        if (i != -1) {
            favouritePlayers.remove(i);
            try {
                listWriteToFileGSON(favouritePlayers, filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
            } catch (IOException ioException) {
                createFolderAndFiles(filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
            }
        }
    }


}