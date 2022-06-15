package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.fabric.mixin.gametest.MinecraftServerMixin;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.ModifiedWList;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.fabricmc.towny_helper.gui.annotation.runner.AnnotationRunner;
import net.fabricmc.towny_helper.gui.manager.ScreenManager;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.MinecraftClientGame;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

public class AllTowns extends WPlainPanel implements ComponentListMethodsInterface {
    WButton reloadTowns;
    ModifiedWList<Town, TownModel> listTownPanel;
    WButton searchButton;
    WTextField searchByName;
    ArrayList<Town> searchTowns;
    WLabel stats;

    public static AllTowns instance;

    public static AllTowns getInstance(){
        if (instance == null) instance = new AllTowns();
        return instance;
    }

    private AllTowns() {
        initializeVariables();
        setEvents();
        registerWidgets();
        refreshList();
        setSize(380, 200);
    }

    public void refreshList() {

        if (MainMod.getTowns() != null) {

            BiConsumer<Town, TownModel> townModelConfigurator = (Town town, TownModel townModel) -> {
               boolean bFlag = false;
                for (Town blackedTown : Storage.getInstance().getBlackedTowns()) {
                    if (blackedTown.getName().equals(town.getName())){
                        Town cloneTown = new Town(blackedTown.getName(), town.getX(), town.getY(), town.getZ(), town.getIcon());
                        cloneTown.setFav(blackedTown.getFav());
                        cloneTown.setDescription(blackedTown.getDescription());
                        bFlag = true;
                        townModel.setTown(cloneTown,0);
                        break;
                    }
                }
                boolean wFlag = false;
                if (!bFlag)
                    for (Town whiteTown : Storage.getInstance().getWhiteTowns()){
                    if (whiteTown.getName().equals(town.getName())){
                        Town mixedTown = new Town(whiteTown.getName(), town.getX(), town.getY(), town.getZ(), town.getIcon());
                        mixedTown.setFav(whiteTown.getFav());
                        mixedTown.setDescription(whiteTown.getDescription());
                        wFlag = true;
                        townModel.setTown(mixedTown, 0);
                        break;
                    }
                }
                if (!bFlag && !wFlag)
                    townModel.setTown(town,0);
            };

            this.remove(listTownPanel);
            if (searchTowns != null)
                listTownPanel = new ModifiedWList<>(searchTowns, TownModel::new, townModelConfigurator);
            else
                listTownPanel = new ModifiedWList<>(MainMod.getTowns(), TownModel::new, townModelConfigurator);

            listTownPanel.layout();
            listTownPanel.setListItemHeight(30);
            this.add(listTownPanel, 0, 26, 370, 140);
            stats.setText(new LiteralText(MainMod.getTowns().size() + " towns in this server").setStyle(Style.EMPTY.withExclusiveFormatting(Formatting.DARK_GRAY)));
        }
    }

    public void initializeVariables() {
        reloadTowns = new WButton(new TextureIcon(new Identifier("towny_helper", "/refresh.png"))).setLabel(Text.of("reload"));
        searchButton = new WButton(new TextureIcon(new Identifier("towny_helper:refresh.png")));
        searchByName = new WTextField().setSuggestion(new LiteralText("search").setStyle(Style.EMPTY.withItalic(true)));
        stats = new WLabel("");
    }

    @Override
    public void registerWidgets() {
        this.add(reloadTowns, 1, 5, 90, 30);
        this.add(searchByName, 275, 5, 100, 18);
        this.add(searchButton, 245, 5);
        this.add(stats,95,7);
    }

    public void setEvents() {
        reloadTowns.setOnClick(() -> {
            if (MainMod.getOnlineData() == null || !MainMod.getOnlineData())
                Service.getInstance().setTowns("0");
            AnnotationRunner.runUpdateGUIAnnotation();
        });

        searchByName.setChangedListener(innerText ->{

            if (MainMod.getTowns() != null) {
                if (searchByName.getText().length() < 1) searchTowns = null;
                else
                    searchTowns = Service.searchTownyByName(MainMod.getTowns(),searchByName.getText());
                refreshList();
            }
        });

    }


}
