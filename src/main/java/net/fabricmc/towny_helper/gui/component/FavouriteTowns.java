package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.annotation.GUIUpdate;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

@GUIUpdate
public class FavouriteTowns extends WPlainPanel implements ComponentListMethodsInterface {
    private WLabel infoFavTown;
    public static WListPanel<Town, TownModel> whiteList;

    private static FavouriteTowns instance = null;

    public static FavouriteTowns getInstance(){
        if (instance == null) instance = new FavouriteTowns();
        return instance;
    }

    private FavouriteTowns() {

//        text = new WLabel("");
//        this.add(text,0,0);
        initializeVariables();
        registerWidgets();
        setEvents();
        refreshList();
    }

    @Override
    public void initializeVariables() {
        infoFavTown = new WLabel("");
    }

    @Override
    public void registerWidgets() {
        this.add(infoFavTown, 4, 4, 150, 30);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public void refreshList() {
        if (MainMod.getTowns() != null && Storage.getInstance().getWhiteTowns() != null){

            BiConsumer<Town, TownModel> whiteListedTownsConfigurator =

                    (Town storedTown, TownModel favouriteTownModel) ->{
                        boolean flag = false;
                        for (Town town : MainMod.getTowns()) {
                            if (storedTown.getName().equals(town.getName())){
                                flag = true;
                                favouriteTownModel.setTown(town, 3);
                                break;
                            }
                        }
                        if (!flag){
                            favouriteTownModel.setTown(storedTown, 3,1);
                        }

                    };
            this.children.clear();
            whiteList = new WListPanel<>(Storage.getInstance().getWhiteTowns(), TownModel::new, whiteListedTownsConfigurator);
            whiteList.setListItemHeight(30);
            whiteList.layout();
            this.add(whiteList,0,25,360,140);
            infoFavTown.setText(Text.of("all liked towns: " + Storage.getInstance().getWhiteTowns().size()));
        }
    }
}
