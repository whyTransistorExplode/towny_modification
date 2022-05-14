package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.gui.annotation.GUIUpdate;
import net.fabricmc.towny_helper.gui.model.PlayerModel;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;

import java.util.function.BiConsumer;

public class FavouritePlayers extends WPlainPanel implements ComponentListMethodsInterface {
    private WListPanel<String, PlayerModel> listPlayers;
    private WLabel wLabel;

    public static FavouritePlayers instance = null;

    public static FavouritePlayers getInstance(){
        if (instance == null) instance = new FavouritePlayers();
        return instance;
    }

    public FavouritePlayers() {
        initializeVariables();
        refreshList();
        registerWidgets();
    }

    public void initializeVariables() {
    }


    public void refreshList(){

        if (Storage.getFavouritePlayersList() == null || MainMod.getPlayers() ==null) return;
     BiConsumer<String, PlayerModel> favPlayerConfigurator = (playerName, model)->{
         for (Player player : MainMod.getPlayers()) {
             if (player.getName().equals(playerName)){
             model.setPlayer(player);
             return;
             }
         }
         model.notOnline(playerName);
     };
        listPlayers = new WListPanel<>(Storage.getFavouritePlayersList(),PlayerModel::new,favPlayerConfigurator);
        listPlayers.layout();
        listPlayers.setListItemHeight(30);
    }


    public void registerWidgets() {
        if (listPlayers != null)
        this.add(listPlayers,2,5,360,140);

    }

    @Override
    public void setEvents() {

    }
}
