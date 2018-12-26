package domain;

import java.util.ArrayList;

import entity.Product;

public interface AllProductsView {
   void initProductsList(ArrayList<Product> arrayList);
   void takePhoto();
   void openImage();
   void creatAlertDialog();
   void creatEmptyLineAlertToast();
   void creatEmptyBitmapAlertToast();
   void dismissAlertDialog();
   void showBottomMenu();
   void hideBottomMenu();
   void notifyLongTouchModeDisabled();
   void notifyLongTouchModeEnabled();
   void hideFloatingButton();
   void showFloatingButton();
   void notifyBackButtonClicked();
   void notifySetAllButtonClicked();
   void notifyMoveButtonClicked();
}
