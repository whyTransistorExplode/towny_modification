package net.fabricmc.towny_helper.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.api.ApiPayload;
import net.fabricmc.towny_helper.entity.InfoTown;
import net.fabricmc.towny_helper.entity.Town;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class Storage {
    public static final String BASE_PATH = MinecraftClient.getInstance().runDirectory.getPath() + "/T_Settings/";
    public static final String DATA_PATH = BASE_PATH + "map_data/";
    @Deprecated
    public static final String BLACK_LISTED_TOWNS_SUFFIX = "_BTowns.dat";
    public static final String BLACKED_TOWNS_SUFFIX = "_BTowns_JS.dat";
    public static final String ALL_TOWNS_SUFFIX = "_AllTowns.TDat";
    @Deprecated
    public static final String WHITE_LISTED_TOWNS_SUFFIX = "_WTowns.dat";
    public static final String WHITE_TOWNS_SUFFIX = "_WTowns_JS.dat";
    public static final String FAVOURITE_PLAYERS_SUFFIX = "_FavPl.dat";

    @Deprecated
    private ArrayList<String> blackListedTowns = null;
    private ArrayList<Town> blackedTowns = null;
    public boolean blackedTownsStateChange = false;
    @Deprecated
    private ArrayList<String> whiteListedTowns = null;
    private ArrayList<Town> whitedTowns = null;
    public boolean whiteTownStateChange = false;
    private ArrayList<String> favouritePlayers = null;

    private ArrayList<InfoTown> dataInfo = null;
    private FileWriter fileWriter;


    private static final Storage instance = new Storage();

    public static Storage getInstance() {
        return instance;
    }

    private Storage() {

    }

    @Deprecated
    private void getBlacklistedTownsFromFile(String serverName) {
        try {
            blackListedTowns = getArrayStringData(BASE_PATH + serverName + BLACK_LISTED_TOWNS_SUFFIX);
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, BLACK_LISTED_TOWNS_SUFFIX));
            blackListedTowns = new ArrayList<>();
        }
    }

    private void setBlackListedTownsFromFile(String serverName) {
        try {
            blackedTowns = new ArrayList<Town>(Arrays.asList(getTownsAsObject(BASE_PATH + serverName + BLACKED_TOWNS_SUFFIX)));
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, BLACKED_TOWNS_SUFFIX));
            blackedTowns = new ArrayList<>();
        }
    }


    private void setWhiteListedTownsFromFile(String serverName) {
        try {
            whitedTowns = new ArrayList<Town>(Arrays.asList(getTownsAsObject(BASE_PATH + serverName + WHITE_TOWNS_SUFFIX)));
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, WHITE_TOWNS_SUFFIX));
            whitedTowns = new ArrayList<>();
        }
    }

    @Deprecated
    private void getWhitelistedTownsFromFile(String serverName) {
        try {
            whiteListedTowns = getArrayStringData(BASE_PATH + serverName + WHITE_LISTED_TOWNS_SUFFIX);
        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, WHITE_LISTED_TOWNS_SUFFIX));
            whiteListedTowns = new ArrayList<>();
        }
    }

    public void reloadFavPlayer(String serverName) {
        getFavouriteListsFromFile(serverName);
    }

    private void getFavouriteListsFromFile(String serverName) {
        try {
            favouritePlayers = getArrayStringData(BASE_PATH + serverName + FAVOURITE_PLAYERS_SUFFIX);

        } catch (FileNotFoundException e) {
            createFolderAndFiles(filePathCombiner(serverName, FAVOURITE_PLAYERS_SUFFIX));
            favouritePlayers = new ArrayList<>();
        }
    }

    @Deprecated
    private ArrayList<String> getArrayStringData(String path) throws FileNotFoundException {

        File file = new File(path);
        Scanner scanner = new Scanner(file);

        ArrayList<String> towns = new ArrayList<>();
        while (scanner.hasNext()) {
            towns.add(scanner.nextLine());
        }
        scanner.close();
        return towns;
    }

    private String getStringData(String path) throws FileNotFoundException {
        File file = new File(path);
        StringBuilder stringBuilder = new StringBuilder();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return stringBuilder.toString();
    }

    private boolean wipeDataFromFile(String path) {
        try {
            FileWriter fileWriter = new FileWriter(path);
            fileWriter.write("");
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Town[] getTownsAsObject(String path) throws FileNotFoundException, JsonSyntaxException {
        Gson gson = new Gson();
        try {
            String hold = getStringData(path);
            return gson.fromJson(hold, Town[].class);
        } catch (JsonSyntaxException e) {
            wipeDataFromFile(path);
            return new Town[10];
        }
    }

    @Deprecated
    public void reloadBlackAndWhiteListTowns(String serverName) {
        if (serverName != null) {
            getWhitelistedTownsFromFile(MainMod.getServerName());
            getBlacklistedTownsFromFile(MainMod.getServerName());
        } else System.err.println("Server name is not set");
    }

    public void reloadBlackAndWhiteTowns(String serverName) {
        if (serverName != null) {
            setWhiteListedTownsFromFile(serverName);
            setBlackListedTownsFromFile(serverName);
        } else System.err.println("server name is not present");
    }

    @Deprecated
    public void addTownBlackListed(String e) {
        addAndSaveBlackListedTown(e);
    }

    @Deprecated
    public void addTownWhiteListed(String e) {
        addAndSaveWhiteListedTown(e);
    }

    @Deprecated
    public void removeTownBlackListed(String e) {
        int i = blackListedTowns.indexOf(e);
        if (i != -1) {
            blackListedTowns.remove(i);
            try {
                listWriteToFile(blackListedTowns, filePathCombiner(MainMod.getServerName(), BLACK_LISTED_TOWNS_SUFFIX));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void removeTownBlacked(Town town) {
        for (int i = 0; i < blackedTowns.size(); i++) {
            if (town.getName().equals(blackedTowns.get(i).getName())) {
                blackedTowns.remove(i);
                Storage.getInstance().blackedTownsStateChange = true;
//                try {
//                    listWriteToFileGSON(blackedTowns,filePathCombiner(MainMod.getServerName(),BLACKED_TOWNS_SUFFIX));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }
    }


    public String filePathCombiner(String serverName, String suffix) {
        return BASE_PATH + serverName + suffix;
    }

    @Deprecated
    public void removeTownWhiteListed(String e) {
        int i = whiteListedTowns.indexOf(e);
        if (i != -1) {
            whiteListedTowns.remove(i);
            try {
                listWriteToFile(whiteListedTowns, filePathCombiner(MainMod.getServerName(), WHITE_LISTED_TOWNS_SUFFIX));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void removeTownWhited(Town town) {

        for (int i = 0; i < whitedTowns.size(); i++) {
            if (town.getName().equals(whitedTowns.get(i).getName())) {
                whitedTowns.remove(i);
                Storage.getInstance().whiteTownStateChange = true;
//                try {
//                    listWriteToFileGSON(whitedTowns,filePathCombiner(MainMod.getServerName(),WHITE_TOWNS_SUFFIX));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                break;
            }
        }
    }

    public void addFavPlayer(String e) {

        for (String favouritePlayer : favouritePlayers) {
            if (e.equals(favouritePlayer)) return;
        }
        favouritePlayers.add(e);
        try {
            listWriteToFile(favouritePlayers, filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
        } catch (IOException ioException) {
            createFolderAndFiles(filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
        }

    }

    public void removeFavPlayer(String e) {

        int i = favouritePlayers.indexOf(e);
        if (i != -1) {
            favouritePlayers.remove(i);
            try {
                listWriteToFile(favouritePlayers, FAVOURITE_PLAYERS_SUFFIX);
            } catch (IOException ioException) {
                createFolderAndFiles(filePathCombiner(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX));
            }
        }
    }

    @Deprecated
    private void addAndSaveBlackListedTown(String e) {
        if (blackListedTowns == null) {
            getBlacklistedTownsFromFile(MainMod.getServerName());
        }
        try {
            if (e != null && e.length() > 0) {
                for (String blackListedTown : blackListedTowns) {
                    if (blackListedTown.equals(e)) return;
                }
                blackListedTowns.add(e);
                listWriteToFile(blackListedTowns, filePathCombiner(MainMod.getServerName(), BLACK_LISTED_TOWNS_SUFFIX));
            }
        } catch (IOException e1) {
            createFolderAndFiles(filePathCombiner(MainMod.getServerName(), BLACK_LISTED_TOWNS_SUFFIX));
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
            try {
                listWriteToFileGSON(blackedTowns, filePathCombiner(MainMod.getServerName(), BLACKED_TOWNS_SUFFIX));
            } catch (IOException e) {
                createFolderAndFiles(filePathCombiner(MainMod.getServerName(), BLACKED_TOWNS_SUFFIX));
            }
        }
        return true;
    }

    public boolean addWhiteTown(Town town) {
        if (whitedTowns == null) {
            setWhiteListedTownsFromFile(MainMod.getServerName());
        }
        try {
            if (town != null && town.getName().length() > 0) {
                for (Town wTown : whitedTowns) {
                    if (wTown.getName().equals(town.getName())) return false;
                }

                whitedTowns.add(town);
                listWriteToFileGSON(whitedTowns, filePathCombiner(MainMod.getServerName(), WHITE_TOWNS_SUFFIX));
            }
        } catch (IOException e1) {
            createFolderAndFiles(filePathCombiner(MainMod.getServerName(), WHITE_TOWNS_SUFFIX));
        }
        return true;
    }

    @Deprecated
    private void addAndSaveWhiteListedTown(String e) {
        if (whiteListedTowns == null) {
            getWhitelistedTownsFromFile(MainMod.getServerName());
        }
        try {
            if (e != null && e.length() > 0) {
                for (String whiteListedTown : whiteListedTowns) {
                    if (whiteListedTown.equals(e)) return;
                }

                whiteListedTowns.add(e);
                listWriteToFile(whiteListedTowns, filePathCombiner(MainMod.getServerName(), WHITE_LISTED_TOWNS_SUFFIX));
            }
        } catch (IOException e1) {
            createFolderAndFiles(filePathCombiner(MainMod.getServerName(), WHITE_LISTED_TOWNS_SUFFIX));
        }
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
     * @throws IOException
     */
    private void listWriteToFileGSON(ArrayList<Town> townsList, String path) throws IOException {
        File file = new File(path);
        if (!file.exists()) createFolderAndFiles(path);

        fileWriter = new FileWriter(path);
        Gson gson = new Gson();
        String objectData = gson.toJson(townsList);
        fileWriter.write(objectData);
        fileWriter.close();
    }

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

    public ApiPayload<?> saveOnlineData() {
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

    public ApiPayload loadLocalDataToMain(String infoName) {
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

    public ArrayList<String> getFavouritePlayersList() {
        return favouritePlayers;
    }


}