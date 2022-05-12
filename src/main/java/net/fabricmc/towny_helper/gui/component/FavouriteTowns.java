package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.model.FavouritedTownModel;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;

import java.util.function.BiConsumer;

public class FavouriteTowns extends WPlainPanel {
    private WLabel text;
    public static WListPanel<String, FavouritedTownModel> whiteList;

    private static FavouriteTowns instance = null;

    public static FavouriteTowns getInstance(){
        if (instance == null) instance = new FavouriteTowns();
        instance.listRefresh();
        return instance;
    }

    private FavouriteTowns() {

//        text = new WLabel("");
//        this.add(text,0,0);
        listRefresh();
    }

    public void listRefresh(){
        if (MainMod.getTowns() != null && Storage.getWhiteListedTowns() != null){

            BiConsumer<String, FavouritedTownModel> whiteListedTownsConfigurator =

                    (String townName, FavouritedTownModel favouriteTownModel) ->{
                    boolean flag = false;
                        for (Town town : MainMod.getTowns()) {
                            if (townName.equals(town.getName())){
                                flag = true;
                                favouriteTownModel.setTown(town);
                                favouriteTownModel.setSpawnOnClick(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (MinecraftClient.getInstance().player != null)
                                            MinecraftClient.getInstance().player.sendChatMessage("/t spawn " + town.getName());

                                        System.out.println("teleporting!");
                                    }
                                });

                                break;
                            }
                        }
                        if (!flag){
                            favouriteTownModel.setDeadTown(townName);
                        }

                    };
            this.children.clear();
            whiteList = new WListPanel<String, FavouritedTownModel>(Storage.getWhiteListedTowns(),FavouritedTownModel::new,whiteListedTownsConfigurator);
            whiteList.setListItemHeight(30);
            whiteList.layout();
            this.add(whiteList,0,26,360,150);
            whiteList.setSize(360,150);
        }
    }
}
