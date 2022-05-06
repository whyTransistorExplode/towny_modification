package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.entity.Town;
import net.fabricmc.towny_helper.gui.model.PlayerModel;
import net.fabricmc.towny_helper.gui.model.TownModel;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.function.BiConsumer;

public class FindPlayer extends WPlainPanel {
    private WLabel showMessage;
    private WLabel targetPlayer;
    private WListPanel<Map.Entry<Town, Double>, TownModel> nearbyTownsListRender;
    public FindPlayer(){
        initializeVariables();
        refreshList();
        registerWidgets();
    }

    public void refreshList(){
        if (MainMod.getPlayerCloseTownsList()!=null && MainMod.getPlayerCloseTownsList().size()>0){

            BiConsumer<Map.Entry<Town, Double>, TownModel> modelConfigurator = (nearbyTowns, model)->{
                model.setTown(nearbyTowns.getKey(), 0);
                model.deleteFavAndTrashButton();

            };
            targetPlayer.setText(new LiteralText("player - " + MainMod.getLookingPlayer().getName()).setStyle(Style.EMPTY.withBold(true).withColor(Formatting.AQUA)));
            nearbyTownsListRender = new WListPanel<>(MainMod.getPlayerCloseTownsList(),TownModel::new,modelConfigurator);
            nearbyTownsListRender.layout();
            nearbyTownsListRender.setListItemHeight(30);
        }
    }

    private void initializeVariables(){
//        showMessage = new WLabel(Text.of(""));
        targetPlayer = new WLabel(Text.of(""));
    }
    private void registerWidgets(){
        this.add(targetPlayer,5,10);
        if (nearbyTownsListRender!=null)
        this.add(nearbyTownsListRender,10,20,350,130);
    }
}
