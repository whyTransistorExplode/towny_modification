package net.fabricmc.towny_helper.gui.model;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.api.ApiPayload;
import net.fabricmc.towny_helper.entity.InfoTown;
import net.fabricmc.towny_helper.gui.StorageGUI;
import net.fabricmc.towny_helper.gui.component.StorageComponent;
import net.fabricmc.towny_helper.superiors.ModelMethod;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;

public class StorageModel extends WPlainPanel implements ModelMethod {
    private WLabel infoField;
    private WLabel infoFieldSize;
    private WButton loadButton;
    private WButton deleteButton;

    private InfoTown infoTown;

    /**
     * sets the {@link StorageModel} and main entry point of this model
     * @param infoTown model's data
     * @param args for additional use in the future
     */
    public void SetState(InfoTown infoTown, int... args){
        this.addPainters();
        this.infoTown = infoTown;
        initializeVariables();
        registerWidgets();
        setEvents();
        refresh();

        BackgroundPainter backgroundPainter = BackgroundPainter.createColorful(7);
        this.setBackgroundPainter(backgroundPainter);
    }

    @Override
    public void initializeVariables() {

        infoField = new WLabel("");
        infoFieldSize = new WLabel("");
        loadButton = new WButton(Text.of("load"));
        deleteButton  = new WButton(Text.of("delete"));
    }

    @Override
    public void registerWidgets() {
        this.add(infoField, 5, 2, 150, 0);
        this.add(infoFieldSize, 5, 10, 150, 0);
        this.add(loadButton, 200, 5,80, 15);
        this.add(deleteButton, 285, 5, 60, 15);
    }

    @Override
    public void setEvents() {
        loadButton.setOnClick(() ->{
            if (infoTown != null) {
                ApiPayload apiPayload = Storage.getInstance().loadLocalDataToMain(infoTown.getName());
                if (apiPayload.isSuccess()) {
                    loadButton.setEnabled(false);
                }
            } else System.out.println("info town is null");
        });
        deleteButton.setOnClick(() ->{
            if (infoTown != null)
                Storage.getInstance().removeData(infoTown.getName());
        });
    }

    @Override
    public void refresh() {
        if (infoTown != null){
            infoField.setText(new LiteralText("Name -" + infoTown.getName()).setStyle(Style.EMPTY.withColor(0)));
            double fileSize = 0;
            String ds;
            if (infoTown.getKbSize() > 1024) {
                fileSize = infoTown.getKbSize() / 1024d;
                ds = fileSize + "mb";
            }
            else ds = infoTown.getKbSize() + "kb";
            infoFieldSize.setText(Text.of("size: " + ds));
        }
    }
}
