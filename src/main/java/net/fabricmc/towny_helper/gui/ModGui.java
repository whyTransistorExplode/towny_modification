package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.Text;

public class ModGui extends LightweightGuiDescription implements ComponentListMethodsInterface {
    WGridPanel root;
    WLabel status;
    WButton serverGaiaButton;
    WButton serverTerraButton;
    WButton disableLooking;

    private static ModGui instance = null;

    public static ModGui getInstance(){
        if (instance == null) instance = new ModGui();
        return  instance;
    }

    private ModGui() {
initializeVariables();
setEvents();
registerWidgets();

    }

    @Override
    public void initializeVariables() {
        serverGaiaButton = new WButton(Text.of("Gaia"));
        serverTerraButton = new WButton(Text.of("Terra"));
        if (MainMod.getServerName().length() > 0)
            if (MainMod.getServerName().equals("gaia"))
                serverGaiaButton.setEnabled(false);
            else serverTerraButton.setEnabled(false);

        String s = "status: off";
        if (MainMod.getServerName().length() > 1) s = "status: (" + MainMod.getServerName() + ")";
        status = new WLabel(s);

        root = new WGridPanel(20);
//        root.setSize(120, 150);

        disableLooking = new WButton(Text.of("stop compass"));
        setRootPanel(root);
        if (!MainMod.isIsLooking()) disableLooking.setEnabled(false);
    }

    @Override
    public void registerWidgets() {
        root.add(serverGaiaButton, 0, 0, 6, 2);
        root.add(serverTerraButton, 0, 2, 6, 2);
        root.add(disableLooking,0,4,6,2);
        root.add(status, 0,6,8,3);
    }

    @Override
    public void setEvents() {
        disableLooking.setOnClick(() -> {
            MainMod.setIsLooking(false);
            MainMod.dynamicTracker = false;
            disableLooking.setEnabled(false);
        });
        serverGaiaButton.setOnClick(() -> {
            MainMod.setServerName("gaia");
            reloadFilesInThread();
            serverGaiaButton.setEnabled(false);
            serverTerraButton.setEnabled(true);
            status.setText(Text.of("status: server(GAIA)"));

        });
        serverTerraButton.setOnClick(() -> {
            MainMod.setServerName("terra");
            reloadFilesInThread();
            serverTerraButton.setEnabled(false);
            serverGaiaButton.setEnabled(true);
            status.setText(Text.of("status: server(TERRA)"));
        });
    }

    @Override
    public void refreshList() {

    }

    private void reloadFilesInThread(){
        if (MainMod.getServerName().length()> 0) {
            new Thread(() -> {
                Storage storage = new Storage();
                storage.reloadBlackAndWhiteListTowns(MainMod.getServerName());
                storage.reloadFavPlayer(MainMod.getServerName());
            }).start();
        }
    }
}
