package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.component.FavouriteTowns;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class FavouritedTownModel extends WPlainPanel {
    private final WSprite sprite;
    private final WLabel textTownName;
    private final WLabel townCoords;
    private final WButton spawnButton;
    private final WButton unFavouriteButton;
    private Town town;
    private String townName;
    private final WPlainPanel self = this;

    public FavouritedTownModel() {
        sprite = new WSprite(new Identifier("towny_helper", "/green_flag.png"));

        this.add(sprite, 0, 0, 40, 30);

        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(7);
        this.setBackgroundPainter(backgroundPainter);
        textTownName = new WLabel("Unkown");
        this.add(textTownName, 50, 5);
        townCoords = new WLabel("-------").setColor(3);
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
        unFavouriteButton.setOnClick(() -> {
            new Storage().removeTownWhiteListed(townName);
            FavouriteTowns.whiteList.remove(self);
            FavouriteTowns.getInstance().refreshList();
        });
    }

    public void refreshData() {

        if (town != null){

            if (town.getIcon().equals("blueflag"))
                sprite.setImage(new Identifier("towny_helper", "/green_flag.png"));
            textTownName.setText(Text.of(town.getName()));
            townCoords.setText(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));
        }else{
            sprite.setImage(new Identifier("towny_helper", "/black_flag.png"));
            textTownName.setText(Text.of(this.townName));
            townCoords.setText(Text.of("probably deleted"));
        }
    }

    public void setTown(Town town) {
        this.town = town;
        this.townName = town.getName();
        refreshData();
        setUnFavouriteOnClick();
    }

    public void setDeadTown(String townName){
        setUnFavouriteOnClick();
        this.townName = townName;
        refreshData();
    }

}
