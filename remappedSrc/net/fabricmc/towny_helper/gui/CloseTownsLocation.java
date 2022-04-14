package net.fabricmc.towny_helper.gui;


import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WScrollPanel;
import net.fabricmc.supplycrate.MainMod;
import net.fabricmc.supplycrate.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.SortedMap;

public class CloseTownsLocation extends LightweightGuiDescription {
    public CloseTownsLocation(SortedMap<Double, String> cs) {
        WPlainPanel plainPanelRoot = new WPlainPanel();
        WScrollPanel root = new WScrollPanel(plainPanelRoot);
        setRootPanel(root);
        root.setSize(200, 190);
        WPlainPanel timepanel = new WPlainPanel();
        WPlainPanel plainPanel = new WPlainPanel();
        WScrollPanel buttonsPanel = new WScrollPanel(plainPanel);

        WLabel wLabel = new WLabel(Text.of("time to the next crate : 00:00:00"), Color.OPAQUE);
        wLabel.setColor(5);

        if (MainMod.getLastSupplyCrate() != null) {
            LocalTime nowTime = LocalTime.parse((new SimpleDateFormat("HH:mm:ss")).format(new Date()));
            LocalTime diffTime = nowTime.minusNanos(MainMod.getLastSupplyCrate().toNanoOfDay());
            wLabel.setText(Text.of("time to the next crate : " + diffTime));
        }

        timepanel.add(wLabel, 2, 2);

        plainPanelRoot.add(timepanel, 3, 5, 180, 30);
        plainPanelRoot.add(buttonsPanel, 3, 35, 180, 145);
//        StringBuilder s = new StringBuilder();
        int index = 0;
        int b = 0;
        WButton wButton;
        WButton wTownToBlackList;
        boolean be;
        if (cs != null)
            for (String value : cs.values()) {
                if (index >= 15) break;
                index++;
//            s.append("\n ").append(++index).append(" ").append(value);
                wButton = new WButton(Text.of(value));

                be = Storage.blacklistedTowns.contains(value);
                wButton.setEnabled(!be);
                wTownToBlackList = new WButton(Text.of(be ? "BlackListed" : "White"));


                String name = wTownToBlackList.getLabel().getString();
                WButton finalWTownToBlackList = wTownToBlackList;

                wTownToBlackList.setOnClick(new Runnable() {
                    @Override
                    public void run() {
                        Storage storage = new Storage();
                        if (name.equals("White")) {
                            storage.addAndSaveBlackListedTown(value);
                            finalWTownToBlackList.setLabel(Text.of("BlackListed"));
                        } else {
                            storage.removeBlacklistedTown(value);
                            finalWTownToBlackList.setLabel(Text.of("White"));
                        }
                    }
                });
                wButton.setOnClick(new Runnable() {
                    @Override
                    public void run() {
                        if (MinecraftClient.getInstance().player != null) {
//                        MinecraftClient.getInstance().player.sendMessage(Text.of("/t spawn "+value),false);

                            MinecraftClient.getInstance().player.sendChatMessage("/t spawn " + value);

                            MinecraftClient.getInstance().player.sendChatMessage("#goal " + MainMod.getSupplyX() +
                                    " " + MainMod.getSupplyY() + " " + MainMod.getSupplyZ());

//                        MinecraftClient.getInstance().player.sendSystemMessage(Text.of("/t spawn "+value),
                            //                              MinecraftClient.getInstance().player.getUuid());

                        }
                    }
                });

                plainPanel.add(wButton, 3, 5 + b, 100, 40);
                plainPanel.add(wTownToBlackList, 105, 5 + b, 60, 40);
                b += 40;
            }

    }
}
