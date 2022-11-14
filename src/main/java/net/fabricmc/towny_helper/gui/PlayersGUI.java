package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.fabricmc.towny_helper.gui.component.AllPlayers;
import net.fabricmc.towny_helper.gui.component.FavouritePlayers;
import net.fabricmc.towny_helper.gui.component.FindPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;

public class PlayersGUI extends LightweightGuiDescription {
    WTabPanel tabs;

    private static final int MAIN_PANE_WIDTH = 380;
    private static final int MAIN_PANE_HEIGHT = 200;
    public AllPlayers allPlayers;
    public FavouritePlayers favouritePlayers;
    public FindPlayer findPlayer;

    private static PlayersGUI instance = null;

    public static PlayersGUI getInstance(){
        if (instance == null) instance = new PlayersGUI();
        return  instance;
    }

    private PlayersGUI() {
        tabs = new WTabPanel();

        setRootPanel(tabs);
        tabs.setSize(MAIN_PANE_WIDTH, MAIN_PANE_HEIGHT);

        allPlayers = new AllPlayers();
        favouritePlayers = new FavouritePlayers();
        findPlayer = new FindPlayer();

        tabs.add(allPlayers, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.APPLE))).title(new LiteralText("Players Online"))
        );

        tabs.add(favouritePlayers, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.GOLDEN_APPLE))).title(new LiteralText("Favourite Players"))
        );
        tabs.add(findPlayer, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.COMPASS))).title(new LiteralText("Find CTown")).tooltip(Text.of("finds close town to a player"))
        );
    }

}
