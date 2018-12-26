package presentation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import domain.BoughtProductsView;
import domain.IntentServiceResult;
import domain.ProductsInteractor;
import entity.Product;

import static presentation.AllProductsPresenter.DB_UPDATED;


public class BoughtProductsPresenter {
    BoughtProductsView boughtProductsView;
    public void onAttach(BoughtProductsView boughtProductsView){
        this.boughtProductsView = boughtProductsView;
        initProductsList();
        EventBus.getDefault().register(this);
    }
    public void onDetach(){boughtProductsView = null;
    }
    private void initProductsList(){
        ArrayList<Product> arrayList = (ArrayList<Product>) getProducts(true);
        if (arrayList != null) {
                boughtProductsView.initProductsList(arrayList);
        }
    }
    private List<Product> getProducts(boolean isBought){
        return ProductsInteractor.getProducts(isBought);
    }
    public void onClickBack(){
        boughtProductsView.notifyBackButtonClicked();
        notifyLongTouchModeDisabled();
    }
    public void onClicSelectAll(){
        boughtProductsView.notifySetAllButtonClicked();
    }
    public void onMoveButtonClicked(){
        boughtProductsView.notifyMoveButtonClicked();
        initProductsList();
        notifyListChanged();
    }
    public void notifyLongTouchModeEnabled(){
        boughtProductsView.showBottomMenu();
    }
    public void notifyLongTouchModeDisabled(){
        boughtProductsView.hideBottomMenu();
    }
    @Subscribe
    public void busListenerBP(IntentServiceResult intentServiceResult) {
        if (intentServiceResult.getResultValue()==DB_UPDATED)
            initProductsList();
    }
    public void notifyListChanged(){
        EventBus.getDefault().post(new IntentServiceResult(DB_UPDATED));
    }

}
