package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.component.FindTown;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;

public class TownModel extends WPlainPanel implements ComponentListMethodsInterface {
    private WSprite sprite;
    private WLabel textTownName;
    private WLabel townCoords;
    private WButton spawnButton;
    private WButton trashButton;
    private WButton favouriteButton;
    private WButton searchButton;
    private Town town;
//    private String flagType;
    private int mode;  // modes: 0 none,  1 search mode

    public TownModel() {

    }

    public void setTown(Town town, int mode) {
        this.town = town;
        this.mode = mode;
        initializeVariables();
        registerWidgets();
        setEvents();
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


    public void setFlagType(boolean isBlacked, boolean isWhite) {
        if (isBlacked)
            sprite.setImage(new Identifier("towny_helper", "black_flag.png"));
        else if (town.getIcon().equals("blueflag")) {
            if (isWhite)
                sprite.setImage(new Identifier("towny_helper", "blue_liked_flag.png"));
            else
                sprite.setImage(new Identifier("towny_helper", "blue_flag.png"));
        } else if (isWhite)
            sprite.setImage(new Identifier("towny_helper", "green_liked_flag.png"));
        else
            sprite.setImage(new Identifier("towny_helper", "green_flag.png"));


        if (favouriteButton != null)
        favouriteButton.setEnabled(!isWhite);
        if (trashButton != null)
        trashButton.setEnabled(!isBlacked);
        if (spawnButton != null)
        spawnButton.setEnabled(!isBlacked);
    }


    @Override
    public void initializeVariables() {
        this.textTownName = new WLabel(new LiteralText(town.getName()).setStyle(Text.of("").getStyle().withColor(5)));
        this.townCoords = new WLabel(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));

        if (mode == 0) {
            spawnButton = new WButton(new ItemIcon(new ItemStack(Items.GRASS_BLOCK))).setLabel(Text.of("Spawn"));
            favouriteButton = new WButton(new TextureIcon(new Identifier("towny_helper", "heart.png")));
            trashButton = new WButton(new TextureIcon(new Identifier("towny_helper", "trash.png")));
            sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
        } else if (mode == 1) {
            searchButton = new WButton(Text.of("search"));
            sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
        }

    }

    public void registerWidgets() {
        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(10);
        this.setBackgroundPainter(backgroundPainter);

        switch (mode) {
            case 0: // when in component AllTowns
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                this.add(spawnButton, 200, 5, 90, 0);
                this.add(favouriteButton, 295, 5, 18, 0);
                this.add(trashButton, 316, 5, 18, 0);
                break;
            case 1: // when in component FindTown
                this.add(sprite, 0, 0, 40, 30);
                this.add(textTownName, 50, 5);
                this.add(townCoords, 50, 15);
                this.add(searchButton, 290, 5, 50, 0);
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
        switch(mode) {
            case 0:
            spawnButton.setOnClick(() -> {
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendChatMessage("/t spawn " + town.getName());

                System.out.println("teleporting!");
            });
            favouriteButton.setOnClick(new Runnable() {
                @Override
                public void run() {
                    new Storage().addTownWhiteListed(town.getName());
                    if (town.getIcon().equals("blueflag"))
                        sprite.setImage(new Identifier("towny_helper:blue_liked_flag.png"));
                    else
                        sprite.setImage(new Identifier("towny_helper:green_liked_flag.png"));
                }
            });
            trashButton.setOnClick(() -> {
                new Storage().addTownBlackListed(town.getName());
                sprite.setImage(new Identifier("towny_helper:black_flag.png"));
            });
        break;
            case 1:
                searchButton.setOnClick(() ->{

                    ArrayList<Town> towns = (ArrayList<Town>) MainMod.getTowns().clone();
                    towns.remove(town);
                    MainMod.setXYZ(town.getX(), town.getY(), town.getZ());
                    MainMod.setLookingTown(town);
                    MainMod.setTownCloseTownsList(Service.computeClosePathTowns(town.getX(), town.getY(), town.getZ(), towns, 20));
                    MainMod.setIsLooking(true);
                    FindTown.getInstance().cleanInput();
                    FindTown.getInstance().renderList();

                });
                break;
        }
    }

    @Override
    public void refreshList() {

    }


}
