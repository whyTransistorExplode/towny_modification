package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WTabPanel;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.fabricmc.towny_helper.gui.component.AllTowns;
import net.fabricmc.towny_helper.gui.component.BlackedTowns;
import net.fabricmc.towny_helper.gui.component.FavouriteTowns;
import net.fabricmc.towny_helper.gui.component.FindTown;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;

public class TownsGUI extends LightweightGuiDescription {
    WTabPanel tabs;

    private static TownsGUI instance = null;

    public static TownsGUI getInstance(){
        if (instance == null) instance = new TownsGUI();
        return instance;
    }

    private TownsGUI() {
      refreshGUI();
    }
    public void refreshGUI(){
        tabs = new WTabPanel();

        setRootPanel(tabs);

        tabs.setSize(380,200);
        AllTowns allTowns = AllTowns.getInstance();
        FavouriteTowns favouriteTowns = FavouriteTowns.getInstance();
        favouriteTowns.listRefresh();
        BlackedTowns blackedTowns = BlackedTowns.getInstance();
        FindTown findTown = FindTown.getInstance();

        tabs.add(allTowns, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.WRITABLE_BOOK))).title(new LiteralText("Towns")).build()
        );
        tabs.add(favouriteTowns, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.WRITTEN_BOOK))).title(new LiteralText("saved Towns")).build()
        );
        tabs.add(blackedTowns, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.ROTTEN_FLESH))).title(new LiteralText("Black List")).build()
        );
        tabs.add(findTown, tab ->
                tab.icon(new ItemIcon(new ItemStack(Items.COMPASS))).title(new LiteralText("Find Close Town")).build()
        );

    }
}
