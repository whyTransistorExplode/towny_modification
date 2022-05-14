package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FindTown extends WPlainPanel implements ComponentListMethodsInterface {

    private static final int LIST_SIZE_WIDTH = 360, LIST_SIZE_HEIGHT = 140, LIST_POS_X = 0, LIST_POS_Y = 40,
    LIST_ITEM_HEIGHT = 30;
    private WButton searchButton;
    private WTextField inputTownName;
    private WListPanel<Map.Entry<Town, Double>, TownModel> closeTownList;
    private WListPanel<Town, TownModel> searchTownList;
    private WTextField inputX;
    private WTextField inputZ;

    private WLabel targetTownName;

    /**
     * @Field dynamic list changes depending on written text in inputTownName
     */
    ArrayList<Town> searchTowns;
    private boolean isCloseTown;

    public static FindTown instance;

    public static FindTown getInstance(){
        if (instance == null) instance = new FindTown();
        return instance;
    }

    private FindTown() {
        initializeVariables();
        setEvents();
        registerWidgets();
        refreshList();
    }

    public void initializeVariables() {

        inputTownName = new WTextField().setSuggestion("search Town");
        inputTownName.setMaxLength(30);
        searchButton = new WButton(new ItemIcon(new ItemStack(Items.COMPASS)));
        inputX = new WTextField(Text.of("enter X"));
        inputZ = new WTextField(Text.of("enter Z"));

        targetTownName = new WLabel("");
        targetTownName.setColor(0);
    }

    public void setEvents() {

        searchButton.setOnClick(() -> {
           /*
            if (!toggleButton.getToggle()) {
                String inputValue = inputTownName.getText();
                if (inputValue.equals("") || MainMod.getTowns() == null) return;

                for (Town town : MainMod.getTowns()) {
                    if (inputValue.equalsIgnoreCase(town.getName()) || inputValue.toLowerCase().startsWith(town.getName().toLowerCase())) {
                        ArrayList<Town> towns = (ArrayList<Town>) MainMod.getTowns().clone();
                        towns.remove(town);
                        MainMod.setXYZ(town.getX(), town.getY(), town.getZ());
                        MainMod.setTownCloseTownsList(Service.computeClosePathTowns(town.getX(), town.getY(), town.getZ(), towns, 20));
                        MainMod.setIsLooking(true);
                        MainMod.setLookingTown(town);
                        renderList();
                        break;
                    }
                }
            }
            else
            */
            {
                if (inputX.getText().length() < 1 || inputZ.getText().length() < 1) return;
                try {
                    int coordX = Integer.parseInt(inputX.getText());
                    int coordZ = Integer.parseInt(inputZ.getText());
                    MainMod.setXYZ(coordX, 0, coordZ);
                    MainMod.setTownCloseTownsList(Service.computeClosePathTowns(coordX, 64, coordZ, MainMod.getTowns(), 20));
                    MainMod.setIsLooking(true);
                    MainMod.setLookingStatus(MainMod.LookingStatus.TRACK_BY_COORDINATES);
                    refreshList();
                } catch (Exception e) {
                    System.out.println("couldn't parse the input values");
                }
            }
        });

        inputTownName.setChangedListener(inputName -> {
            // if input length in the box, greater than 0 then start searching list
            if (inputName.length() > 0) {
                isCloseTown = false;
                this.searchTowns = Service.searchTownyByName(MainMod.getTowns(), inputName);
            }
            refreshList();
        });
    }


    @Override
    public void refreshList() {
        if (this.children.contains(closeTownList))
            this.remove(closeTownList);
        if (this.children.contains(searchTownList))
            this.remove(searchTownList);

        if (this.inputTownName.getText().length() < 1) {
            if (MainMod.getTownCloseTownsList() != null) {
                List<Map.Entry<Town, Double>> closeTownsEntryList = MainMod.getTownCloseTownsList();
                ArrayList<String> blackListedTowns = Storage.getBlackListedTowns();

                BiConsumer<Map.Entry<Town, Double>, TownModel> listConfigurator = (Map.Entry<Town, Double> entryTown, TownModel townModel) -> {
                    townModel.setTown(entryTown.getKey(), 0);

                    townModel.setNearValue(String.valueOf(Math.round(entryTown.getValue())));
                    if (blackListedTowns != null && blackListedTowns.size() > 0) {
                        if (blackListedTowns.contains(entryTown.getKey().getName()))
                            townModel.disableButton();
                    }
                };
                closeTownList = new WListPanel<>(closeTownsEntryList, TownModel::new, listConfigurator);
                closeTownList.setListItemHeight(LIST_ITEM_HEIGHT);
                closeTownList.setHost(this.getHost());
                closeTownList.layout();
                closeTownList.setSize(LIST_SIZE_WIDTH, LIST_SIZE_HEIGHT);
                this.add(closeTownList,LIST_POS_X, LIST_POS_Y, LIST_SIZE_WIDTH, LIST_SIZE_HEIGHT);

            }
        } else {
            if (this.searchTowns != null) {

                BiConsumer<Town, TownModel> townModelConfigurator = (Town town, TownModel townModel) -> {
                    townModel.setTown(town, 1);
                    townModel.setFlagType(Storage.getBlackListedTowns().contains(town.getName()), Storage.getWhiteListedTowns().contains(town.getName()));
                };

                searchTownList = new WListPanel<>(searchTowns, TownModel::new, townModelConfigurator);
                searchTownList.setListItemHeight(LIST_ITEM_HEIGHT);
                searchTownList.setHost(this.getHost());
                searchTownList.setSize(LIST_SIZE_WIDTH, LIST_SIZE_HEIGHT);
                searchTownList.layout();
                this.add(searchTownList,LIST_POS_X, LIST_POS_Y, LIST_SIZE_WIDTH, LIST_SIZE_HEIGHT);
            }
        }

        if (MainMod.isIsLooking()) {
         if (MainMod.getLookingStatus() == MainMod.LookingStatus.TRACK_BY_TOWN)
            this.targetTownName.setText(Text.of(MainMod.getLookingTown().getName() + "  X= " +
                    MainMod.getLookingTown().getX() + ",  Z= " + MainMod.getLookingTown().getZ()));
        else if (MainMod.getLookingStatus() == MainMod.LookingStatus.TRACK_BY_COORDINATES)
             this.targetTownName.setText(Text.of( "Search is By Coordinates X= " +
                     MainMod.getLookingX() + ",  Z= " + MainMod.getLookingZ()));
        }
    }

    public void registerWidgets() {
        this.add(inputTownName, 4, 4, 180, 30);
        this.add(inputX, 210, 4, 60, 15);
        this.add(inputZ, 270, 4, 60, 15);
        this.add(searchButton, 335, 4, 26, 15);
        this.add(targetTownName, 5, 28, 95, 10);

    }
    public void cleanInput(){ inputTownName.setText("");}


}
