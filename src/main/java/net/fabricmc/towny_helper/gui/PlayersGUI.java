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
    public PlayersGUI()
    {
        tabs = new WTabPanel();

        setRootPanel(tabs);
        tabs.setSize(380,200);


        AllPlayers allPlayers = new AllPlayers();
        FavouritePlayers favouritePlayers = new FavouritePlayers();
        FindPlayer findPlayer = new FindPlayer();

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
