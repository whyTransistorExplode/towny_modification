package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import net.fabricmc.supplycrate.MainMod;
import net.fabricmc.supplycrate.entity.MarkerTown;
import net.fabricmc.supplycrate.entity.Town;
import net.minecraft.text.Text;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

import static net.fabricmc.supplycrate.MainMod.computeClosePathTowns;

public class ModGui extends LightweightGuiDescription {

    public ModGui() {

        WGridPanel root = new WGridPanel();
        setRootPanel(root);
        root.setSize(150, 150);
        WButton isCrateHunterActiveButtonForGaia = new WButton(Text.of("Gaia crate hunter is " + (MainMod.isIsSupplyCrateForGaia() ? "on": "off")));
        WButton isCrateHunterActiveButtonForTerra = new WButton(Text.of("Terra crate hunter is " + (MainMod.isIsSupplyCrateForTerra() ? "on" : "off")));
        WButton checkGui = new WButton(Text.of("checkGui"));

        isCrateHunterActiveButtonForGaia.setOnClick(new Runnable() {
            @Override
            public void run() {

                MainMod.setIsSupplyCrateForGaia(!MainMod.isIsSupplyCrateForGaia());
                MainMod.setIsSupplyCrateForTerra(false);

                isCrateHunterActiveButtonForGaia.setLabel(Text.of("Gaia crate hunter is " + (MainMod.isIsSupplyCrateForGaia() ? "on" : "off")));
                isCrateHunterActiveButtonForTerra.setLabel(Text.of("Terra crate hunter is " + (MainMod.isIsSupplyCrateForTerra() ? "on" : "off")));
            }
        });
        isCrateHunterActiveButtonForTerra.setOnClick(new Runnable() {
            @Override
            public void run() {

                MainMod.setIsSupplyCrateForGaia(false);
                MainMod.setIsSupplyCrateForTerra(!MainMod.isIsSupplyCrateForTerra());

                isCrateHunterActiveButtonForGaia.setLabel(Text.of("Gaia crate hunter is " + (MainMod.isIsSupplyCrateForGaia() ? "on" : "off")));
                isCrateHunterActiveButtonForTerra.setLabel(Text.of("Terra crate hunter is " + (MainMod.isIsSupplyCrateForTerra() ? "on" : "off")));
            }
        });
        checkGui.setOnClick(new Runnable() {
            @Override
            public void run() {
                checkGuiMethod();
            }
        });

        root.add(isCrateHunterActiveButtonForGaia, 0, 0, 8, 3);
        root.add(isCrateHunterActiveButtonForTerra, 0, 2, 8, 3);
        root.add(checkGui, 0, 6, 8, 3);

    }

    public void checkGuiMethod() {
        Integer supplyX, supplyY, supplyZ;
        MarkerTown markerTown = new MarkerTown();

        supplyX = 100;
        supplyY = 100;
        supplyZ = 100;
        if (MainMod.getTowns() == null) {
            MainMod.setTowns(markerTown.getTowns("gaia"));
        }
        ArrayList<Town> towns = MainMod.getTowns();
        SortedMap<Double, String> bin = new TreeMap<>();

        computeClosePathTowns(supplyX, supplyY, supplyZ, towns, bin);
        LocalTime crateTime = LocalTime.parse((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
        MainMod.setLastSupplyCrate(crateTime);
    }

}
