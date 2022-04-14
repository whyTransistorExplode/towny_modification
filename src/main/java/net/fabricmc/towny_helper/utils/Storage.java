package net.fabricmc.towny_helper.utils;

import net.fabricmc.towny_helper.MainMod;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final String BASE_PATH = MinecraftClient.getInstance().runDirectory.getPath() + "/T_Settings/";
    private static final String BLACK_LISTED_TOWNS_SUFFIX = "_BTowns.dat";
    private static final String WHITE_LISTED_TOWNS_SUFFIX = "_WTowns.dat";
    private static final String FAVOURITE_PLAYERS_SUFFIX = "_FavPl.dat";
    private static ArrayList<String> blackListedTowns = null;
    private static ArrayList<String> whiteListedTowns = null;
    private static ArrayList<String> favouritePlayers = null;
    public static FileWriter fileWriter;


    private void getBlacklistedTownsFromFile(String serverName) {
        try {
            blackListedTowns = getArrayStringData(BASE_PATH + serverName + BLACK_LISTED_TOWNS_SUFFIX);
        } catch (FileNotFoundException e) {
            createFolderAndFiles(serverName,BLACK_LISTED_TOWNS_SUFFIX);
            blackListedTowns = new ArrayList<>();
        }
    }

    private void getWhitelistedTownsFromFile(String serverName) {
        try {
            whiteListedTowns = getArrayStringData(BASE_PATH + serverName + WHITE_LISTED_TOWNS_SUFFIX);
        } catch (FileNotFoundException e) {
            createFolderAndFiles(serverName,WHITE_LISTED_TOWNS_SUFFIX);
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
            createFolderAndFiles(serverName,FAVOURITE_PLAYERS_SUFFIX);
            favouritePlayers = new ArrayList<>();
        }
    }

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


    public void reloadBlackAndWhiteListTowns(String serverName) {
        if (serverName != null) {
            getWhitelistedTownsFromFile(MainMod.getServerName());
            getBlacklistedTownsFromFile(MainMod.getServerName());
        }else System.err.println("Server name is not set");
    }

    public void addTownBlackListed(String e) {
        addAndSaveBlackListedTown(e);
    }

    public void addTownWhiteListed(String e) {
        addAndSaveWhiteListedTown(e);
    }

    public void removeTownBlackListed(String e) {
        int i = blackListedTowns.indexOf(e);
        if (i != -1) {
            blackListedTowns.remove(i);
            try {
                listWriteToFile(blackListedTowns, filePathCombiner(MainMod.getServerName(),BLACK_LISTED_TOWNS_SUFFIX));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    private String filePathCombiner(String serverName,String suffix){
    return BASE_PATH + serverName+ suffix;
    }

    public void removeTownWhiteListed(String e) {
        int i = whiteListedTowns.indexOf(e);
        if (i != -1) {
            whiteListedTowns.remove(i);
            try {
                listWriteToFile(whiteListedTowns, filePathCombiner(MainMod.getServerName(),WHITE_LISTED_TOWNS_SUFFIX));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

    public void addFavPlayer(String e) {

        for (String favouritePlayer : favouritePlayers) {
            if (e.equals(favouritePlayer)) return;
        }
        favouritePlayers.add(e);
        try {
            listWriteToFile(favouritePlayers, filePathCombiner(MainMod.getServerName(),FAVOURITE_PLAYERS_SUFFIX));
        } catch (IOException ioException) {
            createFolderAndFiles(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX);
        }

    }

    public void removeFavPlayer(String e) {

        int i = favouritePlayers.indexOf(e);
        if (i != -1) {
            favouritePlayers.remove(i);
            try {
                listWriteToFile(favouritePlayers, FAVOURITE_PLAYERS_SUFFIX);
            } catch (IOException ioException) {
                createFolderAndFiles(MainMod.getServerName(), FAVOURITE_PLAYERS_SUFFIX);
            }
        }
    }


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
                listWriteToFile(blackListedTowns, filePathCombiner(MainMod.getServerName(),BLACK_LISTED_TOWNS_SUFFIX));
            }
        } catch (IOException e1) {
            createFolderAndFiles(MainMod.getServerName(), BLACK_LISTED_TOWNS_SUFFIX);
        }

    }

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
                listWriteToFile(whiteListedTowns, filePathCombiner(MainMod.getServerName(),WHITE_LISTED_TOWNS_SUFFIX));
            }
        } catch (IOException e1) {
            createFolderAndFiles(MainMod.getServerName(), WHITE_LISTED_TOWNS_SUFFIX);
        }
    }

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
        fileWriter.close();
    }

    public void removeBlacklistedTown(String e) {
        for (int i = 0; i < blackListedTowns.size(); i++) {
            if (blackListedTowns.get(i).equals(e)) {
                blackListedTowns.remove(i);
                addAndSaveBlackListedTown(null);
                break;
            }
        }
    }

    public void createFolderAndFiles(String serverName, String suffix) {

        File mkfolder = new File(BASE_PATH);
        if (mkfolder.mkdir()) {

            File FILE = new File(filePathCombiner(serverName,suffix));

            try {
                if (FILE.createNewFile()) {
                    System.out.println("created " + serverName + suffix + "  file");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("failed to create folder!");
        }
    }


    public static ArrayList<String> getBlackListedTowns() {
        return blackListedTowns;
    }

    public static ArrayList<String> getWhiteListedTowns() {
        return whiteListedTowns;
    }

    public static ArrayList<String> getFavouritePlayersList() {
        return favouritePlayers;
    }
}
