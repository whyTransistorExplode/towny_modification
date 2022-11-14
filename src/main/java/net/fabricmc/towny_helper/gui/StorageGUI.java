package net.fabricmc.towny_helper.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import net.fabricmc.towny_helper.gui.component.StorageComponent;
import net.fabricmc.towny_helper.gui.model.StorageModel;

public class StorageGUI extends LightweightGuiDescription{

    private StorageComponent storageComponent = null;
    public static StorageGUI instance = null;
    private static StorageModel currentSelectedModel = null;

    public static StorageGUI getInstance() {
        if (instance == null) instance = new StorageGUI();
        return instance;
    }

    private StorageGUI() {
        storageComponent = new StorageComponent();
        setRootPanel(storageComponent);
        setTitleColor(4);
        storageComponent.setSize(380,200);
    }
    public void setCurrentSelectedModel(StorageModel model){
        currentSelectedModel = model;
        storageComponent.update();
    }
    public StorageModel currentLoadedModel(){
        return currentSelectedModel;
    }

}
