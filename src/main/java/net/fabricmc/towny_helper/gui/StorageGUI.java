package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import net.fabricmc.towny_helper.gui.component.StorageComponent;

public class StorageGUI extends LightweightGuiDescription{

    private StorageComponent storageComponent = null;
    public static StorageGUI instance = null;

    public static StorageGUI getInstance() {
        if (instance == null) instance = new StorageGUI();
        instance.storageComponent.update();
        return instance;
    }

    public StorageGUI() {
        storageComponent = new StorageComponent();
        setRootPanel(storageComponent);
        setTitleColor(4);
        storageComponent.setSize(380,200);

    }

}
