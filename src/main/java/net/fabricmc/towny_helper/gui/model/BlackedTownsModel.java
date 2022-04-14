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
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class BlackedTownsModel extends WPlainPanel {
    private WSprite sprite;
    private WLabel textTownName;
    private WLabel townCoords;
//    private WButton spawnButton;
    private WButton UnBlackButton;
    private Town town;
    private WPlainPanel he = this;

    public BlackedTownsModel() {
        sprite = new WSprite(new Identifier("towny_helper", "/green_flag.png"));

        this.add(sprite, 0, 0, 20, 30);

        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(7);
        this.setBackgroundPainter(backgroundPainter);
        textTownName = new WLabel("Unkown");
        this.add(textTownName, 50, 5);
        townCoords = new WLabel("x:0 y:0 z:0");
        this.add(townCoords, 50, 15);

        UnBlackButton = new WButton(new ItemIcon(new ItemStack(Items.BEDROCK))).setLabel(Text.of("remove"));
        this.add(UnBlackButton, 235, 5, 90, 0);
    }
    public void setUnBlackOnClick() {
        UnBlackButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                new Storage().removeTownBlackListed(town.getName());
                BlackedTowns.blackedList.remove(he);
                BlackedTowns.blackedList.layout();

            }
        });
    }

    public void refreshData() {
        if (town != null) {
            textTownName.setText(new LiteralText(town.getName()).setStyle(Text.of("").getStyle().withColor(5)));
            townCoords.setText(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));

            if (town.getIcon().equals("blueflag")) {
                sprite.setImage(new Identifier("towny_helper", "blue_flag.png"));
            }
        }
    }

    public void setTown(Town town) {
        this.town = town;
        refreshData();
        setUnBlackOnClick();
    }


}
