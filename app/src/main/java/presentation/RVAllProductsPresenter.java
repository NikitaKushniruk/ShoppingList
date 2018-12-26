package presentation;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import domain.BitmapUtil;
import domain.ProductsInteractor;
import domain.RVAllProductsView;
import entity.Product;

public class RVAllProductsPresenter {
    private RVAllProductsView rvAllProductsView;
    private boolean longTouchMode;
    private List<Product> list;

    public RVAllProductsPresenter(RVAllProductsView rvAllProductsView, List<Product> list) {
        this.rvAllProductsView = rvAllProductsView;
        this.list = list;
    }

    public void bindEventRow(int position, RVAllProductsView.ViewHolder holder) {//беру экземпляр holder с интерфейса RVAllProductsView
        Product item = list.get(position);
        BitmapUtil bitmapUtil = new BitmapUtil();
        String name = list.get(position).getName();
        String description = list.get(position).getDescription();
        String price = list.get(position).getPrice();
        Bitmap image = bitmapUtil.getImage(list.get(position).getFoto());

        holder.setName(name);
        holder.setDescription(description);
        holder.setPrice(price);
        holder.setImage(image);

        if (item.isChecked()) {
            holder.setChecked(true);
        } else {
            holder.setChecked(false);
        }

        if (longTouchMode) {
            holder.setCheckBoxVisibility(View.VISIBLE);
        }
        else {
            holder.setCheckBoxVisibility(View.INVISIBLE);
        }
    }

    public void onClickCheckbox(int position) {
        list.get(position).setChecked(!list.get(position).isChecked());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isChecked()) {
                rvAllProductsView.notifyDataSetChanged();
                return;
            }
        }
        if (!list.get(list.size() - 1).isChecked()) {
            longTouchMode = false;
        }
        rvAllProductsView.notifyDataSetChanged();
        rvAllProductsView.notifyLongTouchModeDisabled();
    }

    public void onLongClick(int position) {
        list.get(position).setChecked(true);
        longTouchMode = true;
        rvAllProductsView.notifyDataSetChanged();
        rvAllProductsView.notifyLongTouchModeEnabled();
    }
    public void onBackButtonClicked(){
        longTouchMode = false;
        rvAllProductsView.notifyLongTouchModeDisabled();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
        rvAllProductsView.notifyDataSetChanged();
    }
    public void onSetAllButtonClicked(){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(true);
        }
        rvAllProductsView.notifyDataSetChanged();
    }
    public void onMoveButtonClicked(){
        for (int i=0;i<list.size();i++){
            if (list.get(i).isChecked()){
            ProductsInteractor.changeBuyStatus(true, list.get(i).getId());
            }
        }
        onBackButtonClicked();
    }
}


