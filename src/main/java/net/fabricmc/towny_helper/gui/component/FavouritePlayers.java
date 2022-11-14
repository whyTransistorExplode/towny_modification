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
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class FavouritePlayers extends WPlainPanel implements ComponentListMethodsInterface {
    private WListPanel<Player, PlayerModel> listPlayers;
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
        wLabel = new WLabel("");
    }


    public void refreshList(){

        if (Storage.getInstance().getFavouritePlayersList() == null || MainMod.getPlayers() ==null) return;

        wLabel.setText(Text.of("you have " + Storage.getInstance().getFavouritePlayersList().size() + " friends online"));

     BiConsumer<Player, PlayerModel> favPlayerConfigurator = (suppliedPlayer, model)->{
         for (Player player : MainMod.getPlayers()) {
             if (player.getName().equals(suppliedPlayer.getName())){
             model.setPlayer(player, true);
             return;
             }
         }
         model.setPlayer(suppliedPlayer, false);
     };
        listPlayers = new WListPanel<>(Storage.getInstance().getFavouritePlayersList(), PlayerModel::new,favPlayerConfigurator);
        this.add(listPlayers,2,52,360,140);
        listPlayers.setListItemHeight(30);
        listPlayers.layout();
    }


    public void registerWidgets() {
        this.add(wLabel, 2, 5, 100, 50);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public void onFocusGained() {
        super.onFocusGained();
    }

    @Override
    public void onShown() {
        super.onShown();
        refreshList();
    }
}
