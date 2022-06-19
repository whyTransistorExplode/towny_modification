package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.api.ApiPayload;
import net.fabricmc.towny_helper.entity.InfoTown;
import net.fabricmc.towny_helper.gui.model.StorageModel;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class StorageComponent extends WPlainPanel implements ComponentListMethodsInterface {
    public WListPanel<InfoTown, StorageModel> infoList;
    public WLabel infoLabel;
    public WButton reloadButton;

    public WButton saveCurrentDataButton;

    public StorageComponent() {
        initializeVariables();
        registerWidgets();
        setEvents();
        refreshList();
    }

    @Override
    public void refreshList() {
        ApiPayload<ArrayList<InfoTown>> content = Storage.getInstance().getTownsDataInfosLocal();
        if (content.isSuccess()) {
            ArrayList<InfoTown> infoTowns = content.getContent(); // (ArrayList<InfoTown>) Arrays.asList((InfoTown[]) content.getContent());

            BiConsumer<InfoTown, StorageModel> storageModelConfigurator = (infoTown, storageModel) -> storageModel.SetState(infoTown);
            this.remove(infoList);
            infoList = new WListPanel<>(infoTowns, StorageModel::new, storageModelConfigurator);
            infoList.setListItemHeight(30);
            infoList.layout();
            infoList.addPainters();
            infoLabel.setText(Text.of("saved: " + infoTowns.size()));
            this.add(infoList, 5, 30, 370, 140);
        }
    }

    public void update() {
        if (MainMod.getOnlineData() != null && !MainMod.getOnlineData())
            saveCurrentDataButton.setEnabled(false);
        else saveCurrentDataButton.setEnabled(true);
    }

    @Override
    public void initializeVariables() {
        infoLabel = new WLabel("");
        reloadButton = new WButton(new LiteralText("reload").setStyle(Style.EMPTY.withItalic(true)));
        saveCurrentDataButton = new WButton(Text.of("save current"));
    }

    @Override
    public void registerWidgets() {
        this.add(infoLabel, 5, 5, 150, 40);
        this.add(reloadButton, 155, 3, 100, 40);

        this.add(saveCurrentDataButton, 257, 3, 70, 40);
    }

    @Override
    public void setEvents() {
        this.reloadButton.setOnClick(() -> {
            Storage.getInstance().loadTownsDataInfos();
            refreshList();
        });
        this.saveCurrentDataButton.setOnClick(() -> {
            Storage.getInstance().saveOnlineData();
            Storage.getInstance().loadTownsDataInfos();
            refreshList();
        });
    }
}
