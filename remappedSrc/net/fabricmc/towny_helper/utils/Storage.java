package net.fabricmc.towny_helper.utils;
import net.minecraft.client.MinecraftClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private static final String BLACK_LISTED_TOWNS_PATH = MinecraftClient.getInstance().runDirectory.getPath() + "/SupplyCrate/";
    private static final String BLACK_LISTED_TOWNS_FILE_LOCATION = MinecraftClient.getInstance().runDirectory.getPath() + "/SupplyCrate/BTowns.dat";
    public static ArrayList<String> blacklistedTowns = null;
    public static FileWriter fileWriter;


    public void getBlacklistedTownsFromFile() {
        try {
        File file = new File(BLACK_LISTED_TOWNS_FILE_LOCATION);
        Scanner scanner = null;// = null;
                scanner = new Scanner(file);

        ArrayList<String> towns = new ArrayList<>();
        while(scanner.hasNext()){
            towns.add(scanner.nextLine());
        }
        scanner.close();
        blacklistedTowns = towns;
        } catch (FileNotFoundException e) {
            createFolderAndFiles();
            getBlacklistedTownsFromFile();
        }
    }



    public void addAndSaveBlackListedTown(String e){
        if (blacklistedTowns == null) {
                getBlacklistedTownsFromFile();
        }
        try {

            if (fileWriter == null){
                fileWriter = new FileWriter(BLACK_LISTED_TOWNS_FILE_LOCATION);
            }

        ArrayList<String> newTown;
        newTown = blacklistedTowns;

        if (e != null)
        newTown.add(e);

        StringBuilder s = new StringBuilder();
            for (String townName : newTown) {
                if (newTown.get(newTown.size() - 1).equals(townName))
                    s.append(townName);
                else
                s.append(townName).append("\n");
            }
            fileWriter.write(s.toString());
            fileWriter.close();
            blacklistedTowns = newTown;
        } catch (IOException e1) {
            createFolderAndFiles();
        }

    }

    public void removeBlacklistedTown(String e){
        for (int i = 0; i < blacklistedTowns.size(); i++) {
            if (blacklistedTowns.get(i).equals(e)){
                blacklistedTowns.remove(i);
                addAndSaveBlackListedTown(null);
                break;
            }
        }
    }

    public void createFolderAndFiles() {
        File mkfolder = new File(BLACK_LISTED_TOWNS_PATH);
        if (mkfolder.mkdir()) {
            File file = new File(BLACK_LISTED_TOWNS_FILE_LOCATION);
            try {
                if (file.createNewFile()){
                    System.out.println("done");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("failed to create folder!");
        }
    }



}
