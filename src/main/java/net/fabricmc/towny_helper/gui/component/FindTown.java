package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.*;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class FindTown extends WPlainPanel {
    private WButton searchButton;
    private WTextField inputTownName;
    private WListPanel<Map.Entry<Town, Double>, TownModel> closeTownList;
    private WListPanel<Town, TownModel> searchTownList;
    private WTextField inputX;
    private WTextField inputZ;
    private WToggleButton toggleButton;

    /**
     * @Field dynamic list changes depending on written text in inputTownName
     */
    ArrayList<Town> searchTowns;
    private boolean isCloseTown;

    public FindTown() {
        initializeVariables();
        setEvents();
        registerWidgets();
        renderList();
    }

    private void initializeVariables() {

        inputTownName = new WTextField().setSuggestion("Town name");
        searchButton = new WButton(Text.of("search"));
        inputX = new WTextField(Text.of("enter X"));
        inputX.setEditable(false);
        inputZ = new WTextField(Text.of("enter Z"));
        inputZ.setEditable(false);
        toggleButton = new WToggleButton();

    }

    private void setEvents() {

        searchButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (!toggleButton.getToggle()) {
                    String inputValue = inputTownName.getText();
                    if (inputValue.equals("") || MainMod.getTowns() == null) return;

                    for (Town town : MainMod.getTowns()) {
                        if (inputValue.toLowerCase().equals(town.getName().toLowerCase()) || inputValue.toLowerCase().startsWith(town.getName().toLowerCase())) {
                            ArrayList<Town> towns = (ArrayList<Town>) MainMod.getTowns().clone();
                            towns.remove(town);
                            MainMod.setXYZ(town.getX(), town.getY(), town.getZ());
                            MainMod.setTownCloseTownsList(Service.computeClosePathTowns(town.getX(), town.getY(), town.getZ(), towns, 20));
                            MainMod.setIsLooking(true);
                            renderList();
                            break;
                        }
                    }
                } else {
                    if (inputX.getText().length() < 1 || inputZ.getText().length() < 1) return;
                    try {
                        int coordX = Integer.parseInt(inputX.getText());
                        int coordZ = Integer.parseInt(inputZ.getText());
                        MainMod.setXYZ(coordX, 0, coordZ);
                        MainMod.setTownCloseTownsList(Service.computeClosePathTowns(coordX, 64, coordZ, MainMod.getTowns(), 20));
                        MainMod.setIsLooking(true);
                        renderList();
                    } catch (Exception e) {
                        System.out.println("couldn't parse the input values");
                    }
                }
            }
        });

        inputTownName.setChangedListener(inputName -> {
            // if input length in the box, greater than 0 then start searching list
            if (inputName.length() > 0) {
                isCloseTown = false;
                this.remove(closeTownList);
                this.searchTowns = Service.searchTownyByName(MainMod.getTowns(), inputName);
            }
        });

        toggleButton.setOnToggle(event -> {
            if (event) {
                inputTownName.setEditable(false);
                inputX.setEditable(true);
                inputZ.setEditable(true);

            } else {
                inputTownName.setEditable(true);
                inputX.setEditable(false);
                inputZ.setEditable(false);
            }
        });
    }

    private void renderList() {
        if (this.children.contains(closeTownList))
            this.remove(closeTownList);
        if (this.children.contains(searchTownList))
            this.remove(searchTownList);

        if (this.isCloseTown) {
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
                closeTownList = new WListPanel<Map.Entry<Town, Double>, TownModel>(closeTownsEntryList, TownModel::new, listConfigurator);
                closeTownList.setListItemHeight(30);
                closeTownList.setHost(this.getHost());
                closeTownList.layout();
                closeTownList.setSize(360, 150);
                this.add(closeTownList, 0, 35, 360, 140);
            }
        } else {
            if (this.searchTowns != null) {

                BiConsumer<Town, TownModel> townModelConfigurator = (Town town, TownModel townModel) -> {
                    townModel.setTown(town, 1);
                    townModel.setFlagType(Storage.getBlackListedTowns().contains(town.getName()), Storage.getWhiteListedTowns().contains(town.getName()));
                };

                searchTownList = new WListPanel<>(searchTowns, TownModel::new, townModelConfigurator);
            }
        }
    }

    private void registerWidgets() {
        this.add(inputTownName, 0, 0, 100, 30);
        this.add(searchButton, 110, 0, 70, 0);
        this.add(toggleButton, 185, 3);
        this.add(inputX, 210, 2, 50, 15);
        this.add(inputZ, 260, 2, 50, 15);
    }


}
