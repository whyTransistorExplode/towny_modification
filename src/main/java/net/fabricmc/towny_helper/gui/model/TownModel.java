package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.component.AllTowns;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TownModel extends WPlainPanel implements ComponentListMethodsInterface {
    private WSprite sprite;
    private WLabel textTownName;
    private WLabel townCoords;
    private WButton spawnButton;
    private WButton trashButton;
    private WButton favouriteButton;
    private Town town;
    private String flagType;
    private String nearValue;


    public TownModel() {

    }

    public void setTown(Town town) {
        this.town = town;

        initializeVariables();
        registerWidgets();
        setEvents();
    }

    public void setNearValue(String inValue) {
        this.nearValue = inValue;
        textTownName.setText(Text.of(textTownName.getText().getString() + " ->  " + nearValue + "b"));
        textTownName.addTooltip(new TooltipBuilder().add(Text.of("distance ->" + nearValue + " Blocks")));
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

        registerWidgets();
        favouriteButton.setEnabled(!isWhite);
        trashButton.setEnabled(!isBlacked);
        spawnButton.setEnabled(!isBlacked);
    }


    @Override
    public void initializeVariables() {

        this.textTownName = new WLabel(new LiteralText(town.getName()).setStyle(Text.of("").getStyle().withColor(5)));
        this.townCoords = new WLabel(Text.of("X: " + town.getX() + " Y: " + town.getY() + " Z: " + town.getZ()));
        spawnButton = new WButton(new ItemIcon(new ItemStack(Items.GRASS_BLOCK))).setLabel(Text.of("Spawn"));
        favouriteButton = new WButton(new TextureIcon(new Identifier("towny_helper", "heart.png")));
        trashButton = new WButton(new TextureIcon(new Identifier("towny_helper", "trash.png")));
        sprite = new WSprite(new Identifier("towny_helper", "green_flag.png"));
    }

    public void registerWidgets() {
        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(10);
        this.setBackgroundPainter(backgroundPainter);

        this.add(sprite, 0, 0, 40, 30);
        this.add(textTownName, 50, 5);
        this.add(townCoords, 50, 15);
        this.add(spawnButton, 200, 5, 90, 0);
        this.add(favouriteButton, 295, 5, 18, 0);
        this.add(trashButton, 316, 5, 18, 0);
    }

    @Override
    public void setEvents() {
        spawnButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (MinecraftClient.getInstance().player != null)
                    MinecraftClient.getInstance().player.sendChatMessage("/t spawn " + town.getName());

                System.out.println("teleporting!");
            }
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
        trashButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                new Storage().addTownBlackListed(town.getName());
                sprite.setImage(new Identifier("towny_helper:black_flag.png"));
            }
        });
    }

    @Override
    public void refreshList() {

    }


}
