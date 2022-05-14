package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.fabricmc.towny_helper.gui.model.BlackedTownsModel;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.Text;
import org.apache.logging.log4j.core.pattern.AbstractStyleNameConverter;

import java.util.function.BiConsumer;

public class BlackedTowns extends WPlainPanel implements ComponentListMethodsInterface {
    private WLabel infoBlackListedTowns;

    private static int counts = 0;

    private WListPanel<String, TownModel> blackedList;

    private static BlackedTowns instance = null;

    public static BlackedTowns getInstance(){
        if (instance == null) instance = new BlackedTowns();
        return instance;
    }

    private BlackedTowns() {
        initializeVariables();
        registerWidgets();
        setEvents();
    }

    @Override
    public void initializeVariables() {
        this.infoBlackListedTowns = new WLabel("");
        this.infoBlackListedTowns.setColor(0);
    }

    @Override
    public void registerWidgets() {
        this.add(infoBlackListedTowns, 4, 4, 200, 30);
    }

    @Override
    public void setEvents() {

    }

    @Override
    public void refreshList() {
        if (MainMod.getTowns() != null && Storage.getBlackListedTowns() != null){
            counts= 0;
            BiConsumer<String, TownModel> blackListedTownsConfigurator =
                    (String townName, TownModel townModel) ->{
                boolean flag = false;
                        for (Town town : MainMod.getTowns()) {
                            if (townName.equals(town.getName())){
                                flag = true;
                                townModel.setTown(town, 2);
                                counts++;
                                break;
                            }
                        }
                        if (!flag){ townModel.setTown(new Town(townName + "  - deleted",0,0,0,""), 2);}

                    };
            this.children.remove(blackedList);
            blackedList = new WListPanel<>(Storage.getBlackListedTowns(), TownModel::new, blackListedTownsConfigurator);
            blackedList.validate(TownsGUI.getInstance());
            blackedList.layout();
            blackedList.setListItemHeight(30);
            this.add(blackedList,0,26,360,140);

            int size = Storage.getBlackListedTowns().size();
            this.infoBlackListedTowns.setText(Text.of("all - " + size + " Towns"));
            counts = 0;
        }
    }
}
