package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.model.BlackedTownsModel;
import net.fabricmc.towny_helper.utils.Storage;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.util.function.BiConsumer;

public class BlackedTowns extends WPlainPanel {
    private WLabel text;
    public static WListPanel<String, BlackedTownsModel> blackedList;
    public static BlackedTowns instance = null;

    public static BlackedTowns getInstance(){
        if (instance == null) instance = new BlackedTowns();
        return instance;
    }

    private BlackedTowns() {
        if (MainMod.getTowns() != null && Storage.getBlackListedTowns() != null){

            BiConsumer<String, BlackedTownsModel> blackListedTownsConfigurator =
                    (String townName, BlackedTownsModel blackedTownsModel) ->{

                        for (Town town : MainMod.getTowns()) {
                            if (townName.equals(town.getName())){
                                blackedTownsModel.setTown(town);
                                break;
                            }
                        }
                    };
            blackedList = new WListPanel<String, BlackedTownsModel>(Storage.getBlackListedTowns(),BlackedTownsModel::new,blackListedTownsConfigurator);
            blackedList.layout();
            blackedList.setListItemHeight(30);
            this.children.clear();
            this.add(blackedList,0,26,360,130);
            blackedList.setSize(360,130);
        }
    }

}
