package net.fabricmc.towny_helper.superiors;

public interface ModelMethod extends ModelMethodConstruct {
    void refresh();

    int ALL_TOWNS = 0,
            SEARCHED_TOWNS = 1,
            BLACKED_TOWNS = 2,
            WHITED_TOWNS = 3;

}
