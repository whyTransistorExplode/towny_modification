package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import static net.fabricmc.towny_helper.service.Service.updateTracker;

// model
public class PlayerModel extends WPlainPanel {
    private Player player;
    private WLabel name;
    private WLabel position;
    private WButton findButton;
    private WButton favButton;
    private Formatting color = Formatting.BLACK;
    private final Formatting blackModeColor = Formatting.WHITE;

    public PlayerModel(){
        initializeVariables();
        setEvents();
        registerWidgets();
    }


    private void refresh(){
        if (player!=null) {
            String s = "";
            if (!player.getWorld().equals("world"))
                s = " -> Hidden";

            for (Player onePlayer : Storage.getInstance().getFavouritePlayersList()) {
                if (onePlayer.getName().equals(player.getName()))
                {
                    color = Formatting.BOLD;
                    break;
                }
            }
            if (!LibGui.isDarkMode())
                name.setText(new LiteralText(player.getName() + s).setStyle(Style.EMPTY.withExclusiveFormatting(color)));
            else
                name.setText(new LiteralText(player.getName() + s).setStyle(Style.EMPTY.withExclusiveFormatting(blackModeColor)));
            position.setText(Text.of(player.getX() + " " + player.getY() + " " + player.getZ()));
            if (player.getFav()!= null && player.getFav())
                favButton.setIcon(new TextureIcon(new Identifier("towny_helper:broken_heart.png")));
        }
    }

    private void setEvents(){
        findButton.setOnClick(() -> {
            if (player != null) {
                updateTracker(player);
                MainMod.isDynamicTrackerFirst = true;
                MainMod.dynamicTracker = true;
            }
        });
        favButton.setOnClick(() -> {
            if (player.getFav() != null && player.getFav())
                Storage.getInstance().removeFavPlayer(player);
            else
                Storage.getInstance().addFavPlayer(player);
            refresh();
        });
    }


    public void setPlayer(Player player, boolean isOnline) {
        this.player = player;
        refresh();
        if (!isOnline){
            name.setText(Text.of(player.getName() + "  -> offline "));
            findButton.setEnabled(false);
        }
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
