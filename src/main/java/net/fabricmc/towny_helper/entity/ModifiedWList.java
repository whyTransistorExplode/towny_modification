package net.fabricmc.towny_helper.entity;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ModifiedWList<D, W extends WWidget> extends WListPanel {
    /**
     * Constructs a list panel.
     *
     * @param data         the list data
     * @param supplier     the widget supplier that creates unconfigured widgets
     * @param configurator the widget configurator that configures widgets to display the passed data
     */
    public ModifiedWList(List data, Supplier supplier, BiConsumer configurator) {
        super(data, supplier, configurator);
    }

    @Override
    public void setHost(@Nullable GuiDescription host) {
        super.setHost(host);
        scrollBar.setHost(this.host);

    }
    public void setScroll(int value){
        this.scrollBar.setValue(value);
    }
}
