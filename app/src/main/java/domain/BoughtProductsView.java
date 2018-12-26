package domain;

import java.util.ArrayList;

import entity.Product;

public interface BoughtProductsView {
    void initProductsList(ArrayList<Product> arrayList);
    void showBottomMenu();
    void hideBottomMenu();
    void notifyLongTouchModeDisabled();
    void notifyLongTouchModeEnabled();
    void notifyBackButtonClicked();
    void notifySetAllButtonClicked();
    void notifyMoveButtonClicked();
}
