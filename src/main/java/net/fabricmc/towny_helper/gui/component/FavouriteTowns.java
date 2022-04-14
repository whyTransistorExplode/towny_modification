package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
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
    public FavouriteTowns() {

//        text = new WLabel("");
//        this.add(text,0,0);
        listRefresh();
    }
    public void listRefresh(){
        if (MainMod.getTowns() != null && Storage.getWhiteListedTowns() != null){
            BiConsumer<String, FavouritedTownModel> whiteListedTownsConfigurator =
                    (String townName, FavouritedTownModel favouriteTownModel) ->{
                        for (Town town : MainMod.getTowns()) {
                            if (townName.equals(town.getName())){
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
                    };
            this.children.clear();
            whiteList = new WListPanel<String, FavouritedTownModel>(Storage.getWhiteListedTowns(),FavouritedTownModel::new,whiteListedTownsConfigurator);
            whiteList.setListItemHeight(30);
            whiteList.layout();
            this.add(whiteList,0,26,360,130);
            whiteList.setSize(360,130);
        }
    }
}
