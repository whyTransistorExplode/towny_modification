package net.fabricmc.towny_helper.gui.component;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.fabricmc.towny_helper.MainMod;
import net.fabricmc.towny_helper.entity.Player;
import net.fabricmc.towny_helper.gui.annotation.runner.AnnotationRunner;
import net.fabricmc.towny_helper.gui.model.PlayerModel;
import net.fabricmc.towny_helper.service.Service;
import net.fabricmc.towny_helper.superiors.ComponentListMethodsInterface;
import net.fabricmc.towny_helper.utils.Storage;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.BiConsumer;


public class AllPlayers extends WPlainPanel implements ComponentListMethodsInterface {

    private WButton refreshButton;
    WListPanel<Player, PlayerModel> playerWList;
    private WLabel stats;
    private ArrayList<Player> searchedPlayers;
    private WTextField searchNameInput;

    public static AllPlayers instance = null;

    public static AllPlayers getInstance(){
        if (instance == null) instance = new AllPlayers();
        return instance;
    }

    private AllPlayers(){
        setSize(380,200);
        initializeVariables();
        setEvents();
        registerWidgets();
        refreshList();
    }

    public void refreshList(){
        if (MainMod.getPlayers() != null) {
            stats.setText(new LiteralText("Online: " + MainMod.getPlayers().size() + " players").setStyle(Style.EMPTY.withBold(true).withColor(0x0009f0)));

            BiConsumer<Player, PlayerModel> playerModelConfigurator = (Player player, PlayerModel playerModel) ->{
                for (String favPl : Storage.getInstance().getFavouritePlayersList()) {
                    if (favPl.equals(player.getName()))
                    {
                    player.setFav(true);
                        break;
                    }
                }

                playerModel.setPlayer(player);
            };
            this.remove(playerWList);
            if (searchedPlayers!=null)
            playerWList = new WListPanel<>(searchedPlayers, PlayerModel::new, playerModelConfigurator);
            else playerWList = new WListPanel<>(MainMod.getPlayers(), PlayerModel::new, playerModelConfigurator);
            playerWList.setListItemHeight(30);
            playerWList.layout();

            this.add(playerWList, 0, 25, 360,140);
        }
    }

    public void setEvents(){
        refreshButton.setOnClick(() -> {

            Thread thread = new Thread(() -> {
                Service.getInstance().setPlayers("0");
                AnnotationRunner.runUpdateGUIAnnotation();
            });
            thread.start();

//                MinecraftClient.getInstance().setScreen(new ScreenManager(new PlayersGUI()));
        });
        searchNameInput.setChangedListener(inputName ->{
            if (MainMod.getPlayers() !=null) {
                if (inputName.length() < 1) searchedPlayers =null;
                else searchedPlayers = Service.searchPlayersByName(MainMod.getPlayers(), inputName);
                refreshList();
            }
        });
    }

    public void registerWidgets(){
        this.add(refreshButton,0,0, 90, 30);
        this.add(stats, 100, 4);
        this.add(searchNameInput,275,4,100,20);
    }

    public void initializeVariables(){
        refreshButton = new WButton();
        refreshButton.setLabel(Text.of("refresh")).setIcon(new TextureIcon(new Identifier("towny_helper","/refresh_player.png")));
        stats = new WLabel("", 0x0000c0);
        searchNameInput = new WTextField(Text.of("search"));
    }
}
