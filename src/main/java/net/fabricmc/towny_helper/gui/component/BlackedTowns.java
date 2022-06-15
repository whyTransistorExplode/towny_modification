package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.Text;

import java.util.function.BiConsumer;

public class BlackedTowns extends WPlainPanel implements ComponentListMethodsInterface {
    private WLabel infoBlackListedTowns;
    private WListPanel<Town, TownModel> blackedList;
    private static BlackedTowns instance = null;
    public static BlackedTowns getInstance(){
        if (instance == null) instance = new BlackedTowns();
        return instance;
    }

    private BlackedTowns() {
        initializeVariables();
        registerWidgets();
        setEvents();
        refreshList();
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
        if (MainMod.getTowns() != null && Storage.getInstance().getBlackedTowns() != null){
            BiConsumer<Town, TownModel> blackListedTownsConfigurator =
                    (Town storedTown, TownModel townModel) ->{
                boolean flag = false;
                        for (Town town : MainMod.getTowns()) {
                            if (storedTown.getName().equals(town.getName())){
                                flag = true;
                                townModel.setTown(town, 2);
                                break;
                            }
                        }
                        if (!flag){ townModel.setTown(storedTown, 2, 1);}
                    };
            this.children.remove(blackedList);
            blackedList = new WListPanel<>(Storage.getInstance().getBlackedTowns(), TownModel::new, blackListedTownsConfigurator);
            blackedList.layout();

            blackedList.setListItemHeight(30);
            this.add(blackedList,0,26,360,140);

            int size = Storage.getInstance().getBlackedTowns().size();
            this.infoBlackListedTowns.setText(Text.of("all - " + size + " Towns"));
        }
    }
}
