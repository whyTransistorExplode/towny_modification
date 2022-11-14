package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.TownsGUI;
import net.fabricmc.towny_helper.gui.component.BlackedTowns;
import net.fabricmc.towny_helper.gui.component.FavouriteTowns;
import net.fabricmc.towny_helper.gui.component.FindTown;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.superiors.ModelMethod;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TownModel extends WPlainPanel implements ModelMethod {
    private int[] args;
    private WSprite sprite;
    private WLabel textTownName;
    private WLabel townCoords;
    private WButton spawnButton;
    private WButton trashButton;
    private WButton removeTrashStatusButton;
    private WButton favouriteButton;
    private WButton unFavouriteButton;
    /**
     * for mode 1 FindTown WPlain Tab
     */
    private WButton searchButton;

    /**
     * for mode 2 blacklistedTowns WPlain Tab
     */
    private Town town;
    //    private String flagType;
    private int mode;  // modes: 0 none,  1 search mode

    public TownModel() {

    }

    public void setTown(Town town, int mode, int... args) {
        this.town = town;
        this.mode = mode;
        initializeVariables();
        registerWidgets();
        setEvents();
        refresh();
        this.args = args;
    }

    public void setNearValue(String inValue) {
        textTownName.setText(Text.of(textTownName.getText().getString() + " ->  " + inValue + "b"));
        textTownName.addTooltip(new TooltipBuilder().add(Text.of("distance ->" + inValue + " Blocks")));
    }

    public void deleteFavAndTrashButton() {
        this.remove(this.trashButton);
        this.remove(this.favouriteButton);
    }

    public void disableButton() {
        spawnButton.setLabel(Text.of("BlackListed"));
        spawnButton.setEnabled(false);
    }


    private void setFlagType() {
        switch (town.getIcon()) { // neutral towns flag set
            case "blueflag":
                sprite.setImage(new Identifier("towny_helper", "blue_flag.png"));
                break;
            case "greenflag":
                sprite.setImage(new Identifier("towny_helper", "green_flag.png"));
                break;
        }

        if (mode != 2) {
            if (town.getFav() != null && town.getFav()) { // favourite towns flag
                switch (town.getIcon()) {
                    case "blueflag":
                        sprite.setImage(new Identifier("towny_helper", "blue_liked_flag.png"));
                        break;
                    case "greenflag":
                        sprite.setImage(new Identifier("towny_helper", "green_liked_flag.png"));
                        break;
                }
            } else if (town.getFav() != null) { // blacked towns flag
                sprite.setImage(new Identifier("towny_helper", "black_flag.png"));
            }
        }
    }


    @Override
    public void initializeVariables() {
        if (LibGui.isDarkMode())
            this.textTownName = new WLabel(town.getName());
        else
            this.textTownName = new WLabel(new LiteralText(town.getName()).setStyle(Text.of("").getStyle().withColor(5)));
        this.townCoords = new WLabel(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));

        switch (mode) {
            case ALL_TOWNS: //allTowns
                spawnButton = new WButton(new ItemIcon(new ItemStack(Items.GRASS_BLOCK))).setLabel(Text.of("Spawn"));
                favouriteButton = new WButton(new TextureIcon(new Identifier("towny_helper", "heart.png")));
                trashButton = new WButton(new TextureIcon(new Identifier("towny_helper", "trash.png")));
                sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
                break;
            case SEARCHED_TOWNS: // FindTown, found towns by given input name in search input field
                searchButton = new WButton(Text.of("search"));
                sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
                break;
            case BLACKED_TOWNS: // BlackListedTowns
                // args[0] == 1 means the town is not present online
                if (args != null && args[0] == 1)
                    textTownName.setText(Text.of(town.getName() + " - not present"));
                removeTrashStatusButton = new WButton(Text.of("remove"));
                sprite = new WSprite(new Identifier("towny_helper", "black_flag.png"));
                break;
            case WHITED_TOWNS: // FavouriteTowns
                if (args != null && args[0] == 1)
                    textTownName.setText(Text.of(town.getName() + " - not present"));
                sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
                spawnButton = new WButton(new ItemIcon(new ItemStack(Items.GRASS_BLOCK))).setLabel(Text.of("Spawn"));
                unFavouriteButton = new WButton(new TextureIcon(new Identifier("towny_helper", "broken_heart.png")));
                break;
        }
    }

    public void registerWidgets() {
        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(10);
        this.setBackgroundPainter(backgroundPainter);

        switch (mode) {
            case ALL_TOWNS: // when in component AllTowns
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                this.add(spawnButton, 200, 5, 90, 0);
                this.add(favouriteButton, 295, 5, 18, 0);
                this.add(trashButton, 316, 5, 18, 0);
                break;
            case SEARCHED_TOWNS: // FindTown, found towns by entered input name
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                this.add(searchButton, 290, 5, 50, 0);
                break;
            case BLACKED_TOWNS:  // Blacked towns component
                this.add(removeTrashStatusButton, 290, 5, 50, 0);
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                break;
            case WHITED_TOWNS:
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                this.add(spawnButton, 200, 5, 90, 0);
                this.add(unFavouriteButton, 295, 5, 18, 0);
                break;
        }
//        this.add(sprite, 0, 0, 40, 30);
//        this.add(textTownName, 50, 5);
//        this.add(townCoords, 50, 15);
//        this.add(spawnButton, 200, 5, 90, 0);
//        this.add(favouriteButton, 295, 5, 18, 0);
//        this.add(trashButton, 316, 5, 18, 0);
    }

    @Override
    public void setEvents() {
        switch (mode) {
            case ALL_TOWNS: // all towns
                spawnButton.setOnClick(() -> {
                    if (MinecraftClient.getInstance().player != null)
                        MinecraftClient.getInstance().player.sendChatMessage("/town spawn " + town.getName());

                    System.out.println("teleporting!");
                });
                favouriteButton.setOnClick(() -> {
                    boolean b = Storage.getInstance().addWhiteTown(this.town);
                    this.town.setFav(b ? true : null);

                    refresh();
                });
                trashButton.setOnClick(() -> {
                    boolean b = Storage.getInstance().addBlackedTown(town);
                    this.town.setFav(b ? false : null);
                    refresh();
//                    sprite.setImage(new Identifier("towny_helper:black_flag.png"));
                });
                break;
            case SEARCHED_TOWNS: //find town, towns by given input
                searchButton.setOnClick(() -> {
                    ArrayList<Town> towns = (ArrayList<Town>) MainMod.getTowns().clone();
                    towns.remove(town);
                    MainMod.setXYZ(town.getX(), town.getY(), town.getZ());
                    MainMod.setLookingTown(town);
                    MainMod.setTownCloseTownsList(Service.computeClosePathTowns(town.getX(), town.getY(), town.getZ(), towns, 20));
                    MainMod.setIsLooking(true);
                    MainMod.setLookingStatus(MainMod.LookingStatus.TRACK_BY_TOWN);
                    FindTown.getInstance().cleanInput();
                    FindTown.getInstance().refreshList();
                });
                break;
            case BLACKED_TOWNS:
                removeTrashStatusButton.setOnClick(() -> {
                    Storage.getInstance().removeTownBlacked(this.town);
                    BlackedTowns.getInstance().refreshList();
                });

                break;
            case WHITED_TOWNS:
                spawnButton.setOnClick(() -> {
                    if (MinecraftClient.getInstance().player != null)
                        MinecraftClient.getInstance().player.sendChatMessage("/town spawn " + town.getName());
                });
                unFavouriteButton.setOnClick(() -> {
                    Storage.getInstance().removeTownWhited(this.town);
                    FavouriteTowns.getInstance().refreshList();
                });
                break;
        }
    }

    @Override
    public void refresh() {
        setFlagType();
        if (town.getFav() != null) {
            if (favouriteButton != null)
                favouriteButton.setEnabled(false);
            if (trashButton != null)
                trashButton.setEnabled(false);
            if (spawnButton != null)
                spawnButton.setEnabled(town.getFav());
        }
    }
}
