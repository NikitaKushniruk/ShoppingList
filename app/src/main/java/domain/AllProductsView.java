package domain;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import entity.Product;

public interface AllProductsView {
   void initProductsList(ArrayList<Product> arrayList);
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
   Fragment getViewActivity();
   Context getFragmentContext();
   void setImageBitmap(Bitmap bitmap);
}
