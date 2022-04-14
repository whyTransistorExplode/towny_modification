package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static net.fabricmc.towny_helper.service.Service.updateTracker;

// model
public class PlayerModel extends WPlainPanel {
    private Player player;
    private WLabel name;
    private WLabel position;
    private WButton findButton;
    private WButton favButton;
    private Formatting color = Formatting.BLACK;
    private boolean isFav;

    public PlayerModel(){
        initializeVariables();
        setEvents();
        registerWidgets();
    }
    public void notOnline(String offlineName){
        name.setText(Text.of(offlineName + "  -> offline "));
        findButton.setEnabled(false);
    }

    public void setFav(boolean fav) {
        isFav = fav;
        if (fav)
            favButton.setIcon(new TextureIcon(new Identifier("towny_helper:broken_heart.png")));
    }

    private void refresh(){
        if (player!=null) {
            String s = "";
            if (!player.getWorld().equals("world"))
                s = " -> Hidden";
            for (String favList : Storage.getFavouritePlayersList()) {
                if (player.getName().equals(favList)) {
                    color = Formatting.GOLD;
                    break;
                }
            }

            name.setText(new LiteralText(player.getName() + s).setStyle(Style.EMPTY.withExclusiveFormatting(color)));

            position.setText(Text.of(player.getX() + " " + player.getY() + " " + player.getZ()));
        }
    }

    private void setEvents(){
        findButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                if (player != null) {
                    updateTracker(player);
                    MainMod.isDynamicTrackerFirst = true;
                    MainMod.dynamicTracker = true;
                }
            }
        });
        favButton.setOnClick(new Runnable() {
            @Override
            public void run() {
                Storage storage = new Storage();
                if (isFav) storage.removeFavPlayer(player.getName());
                else
                storage.addFavPlayer(player.getName());
            }
        });
    }


    public void setPlayer(Player player) {
        this.player = player;
        refresh();
    }

    public void initializeVariables(){
        findButton = new WButton();
        findButton.setLabel(Text.of("Find Town"));
        findButton.addTooltip(new TooltipBuilder().add(Text.of("finds close towns to a player")));
        favButton = new WButton(new TextureIcon(new Identifier("towny_helper:heart.png")));

        position = new WLabel("");
        name = new WLabel("not Found");
        name.setColor(0x011010);
    }

    public void registerWidgets(){
        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(10);
        this.setBackgroundPainter(backgroundPainter);

        this.add(findButton,200,3,90,30);
        this.add(name, 3, 5);
        this.add(position, 3, 20);
        this.add(favButton, 295,3,18,0);
    }

}
