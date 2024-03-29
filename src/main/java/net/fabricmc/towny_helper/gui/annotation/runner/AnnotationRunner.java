package net.fabricmc.towny_helper.gui.annotation.runner;

import net.fabricmc.towny_helper.gui.PlayersGUI;
import net.fabricmc.towny_helper.gui.component.*;

public class AnnotationRunner {
    public static void runUpdateGUIAnnotation(){

    //        Reflections reflections = new Reflections("net.fabricmc.towny_helper.gui.component");
    //        for (Class<?> cl : reflections.getTypesAnnotatedWith(GUIUpdate.class)){
    //            if (ComponentListMethodsInterface.class.isAssignableFrom(cl)){
    ////               ComponentListMethodsInterface.class.cast(cl).
    //            }
    //        }

        AllTowns.getInstance().refreshList();
        FavouriteTowns.getInstance().refreshList();
        BlackedTowns.getInstance().refreshList();
        FindTown.getInstance().refreshList();

        PlayersGUI.getInstance().allPlayers.refreshList();
        PlayersGUI.getInstance().favouritePlayers.refreshList();
        PlayersGUI.getInstance().findPlayer.refreshList();
    }
}
