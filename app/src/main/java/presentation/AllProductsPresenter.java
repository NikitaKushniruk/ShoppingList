package presentation;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import domain.AllProductsView;
import domain.BitmapUtil;
import domain.IntentServiceResult;
import domain.ProductsInteractor;
import entity.Product;

public class AllProductsPresenter {
    public static final int DB_UPDATED = 48;
    private AllProductsView allProductsView;
    public void onAttach(@NonNull AllProductsView allProductsView){
        this.allProductsView = allProductsView;
        initProductsList();
        EventBus.getDefault().register(this);
    }
    public void onDetach(){allProductsView = null;
        EventBus.getDefault().unregister(this);
    }
    private void initProductsList(){
        ArrayList<Product> arrayList = (ArrayList<Product>) getProducts(false);
        if (arrayList != null) {
               allProductsView.initProductsList(arrayList);
        }
    }
    private List<Product> getProducts(boolean isBought){
        return ProductsInteractor.getProducts(isBought);
    }
    public void onClickFab(){
        allProductsView.creatAlertDialog();
    }
    public void checkProduct(String name,String description,String price,Bitmap bitmap){
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(description)||TextUtils.isEmpty(price)) {
            allProductsView.creatEmptyLineAlertToast();
            return;
        }
        if (bitmap==null) {
            allProductsView.creatEmptyBitmapAlertToast();
            return;
        }
        BitmapUtil bitmapUtil = new BitmapUtil();
        byte[] byteBitmap = bitmapUtil.getBytes(bitmap);
        ProductsInteractor.creatProduct(name,description,price,false,false,byteBitmap,false);
        initProductsList();
        allProductsView.dismissAlertDialog();
    }
    public void onClickBack(){
        allProductsView.notifyBackButtonClicked();
        notifyLongTouchModeDisabled();
    }
    public void onClicSelectAll(){
        allProductsView.notifySetAllButtonClicked();
    }
    public void onMoveButtonClicked(){
        allProductsView.notifyMoveButtonClicked();
        initProductsList();
        notifyListChanged();
    }
    public void onClickCameraButton(){
        allProductsView.takePhoto();
    }
    public void onClickGalleryButton(){
        allProductsView.openImage();
    }

    public void notifyLongTouchModeEnabled(){
        allProductsView.showBottomMenu();
        allProductsView.hideFloatingButton();
    }
    public void notifyLongTouchModeDisabled(){
        allProductsView.hideBottomMenu();
        allProductsView.showFloatingButton();
    }

    public void notifyListChanged(){
        EventBus.getDefault().post(new IntentServiceResult(DB_UPDATED));
    }
    @Subscribe
    public void busListener(IntentServiceResult intentServiceResult) {
        if (intentServiceResult.getResultValue()==DB_UPDATED)
            initProductsList();
    }

}
