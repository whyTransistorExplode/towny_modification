package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.gui.manager.ScreenManager;
import net.fabricmc.towny_helper.superiors.ModelMethod;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class ModGui extends LightweightGuiDescription implements ModelMethod {
    WGridPanel root;
    WLabel status;
    WButton serverGaiaButton;
    WButton serverTerraButton;
    WButton disableLooking;

    WButton switchLocalStorage;

    private static ModGui instance = null;

    public static ModGui getInstance(){
        if (instance == null) instance = new ModGui();
        instance.refresh();

        return  instance;
    }

    private ModGui() {refresh();}
    @Override
    public void refresh(){
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
        if (!MainMod.isIsLooking()) disableLooking.setEnabled(false);

        switchLocalStorage = new WButton(Text.of(" to local storage..."));
    }

    @Override
    public void registerWidgets() {
        setRootPanel(root);


        root.add(serverGaiaButton, 1, 0, 6, 1);
        root.add(serverTerraButton, 1, 2, 6, 1);
        root.add(switchLocalStorage, 1, 4, 6, 1);
        root.add(disableLooking,1,6,6,1);
        root.add(status, 0,8,8,1);
    }

    @Override
    public void setEvents() {
        disableLooking.setOnClick(() -> {
            MainMod.setIsLooking(false);
            MainMod.dynamicTracker = false;
            MainMod.setLookingStatus(MainMod.LookingStatus.OFFLINE);
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
        switchLocalStorage.setOnClick(()->{
            MinecraftClient.getInstance().setScreen(new ScreenManager(StorageGUI.getInstance()));
        });
    }


    private void reloadFilesInThread(){
        if (MainMod.getServerName().length()> 0) {
            new Thread(() -> {
                Storage storage = Storage.getInstance();
                storage.reloadBlackAndWhiteTowns(MainMod.getServerName());
                storage.reloadFavPlayer(MainMod.getServerName());
            }).start();
        }
    }
}
