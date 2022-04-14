package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.component.BlackedTowns;
import net.fabricmc.towny_helper.gui.component.FavouriteTowns;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FavouritedTownModel extends WPlainPanel {
    private WSprite sprite;
    private WLabel textTownName;
    private WLabel townCoords;
    private WButton spawnButton;
    private WButton unFavouriteButton;
    private Town town;
    private WPlainPanel he = this;

    public FavouritedTownModel() {
        sprite = new WSprite(new Identifier("towny_helper", "/green_flag.png"));

        this.add(sprite, 0, 0, 40, 30);

        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(7);
        this.setBackgroundPainter(backgroundPainter);
        textTownName = new WLabel("Unkown");
        this.add(textTownName, 50, 5);
        townCoords = new WLabel("x:0 y:0 z:0").setColor(3);
        this.add(townCoords, 50, 15);


        spawnButton = new WButton(new ItemIcon(new ItemStack(Items.GRASS_BLOCK))).setLabel(Text.of("Spawn "));
        this.add(spawnButton, 200, 5, 90, 0);
        unFavouriteButton = new WButton(new TextureIcon(new Identifier("towny_helper", "broken_heart.png")));
        this.add(unFavouriteButton, 295, 5, 18, 0);
      }

    public void setSpawnOnClick(Runnable onClick) {
        spawnButton.setOnClick(onClick);
    }
    public void setUnFavouriteOnClick() {
        unFavouriteButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                new Storage().removeTownWhiteListed(town.getName());
                FavouriteTowns.whiteList.remove(he);
            }
        });
    }

    public void refreshData() {

        if (town != null){

            if (town.getIcon().equals("blueflag"))
                sprite.setImage(new Identifier("towny_helper", "/green_flag.png"));
            textTownName.setText(Text.of(town.getName()));
            townCoords.setText(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));
        }
    }

    public void setTown(Town town) {
        this.town = town;
        refreshData();
        setUnFavouriteOnClick();
    }


}
