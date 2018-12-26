package presentation;

import android.graphics.Bitmap;
import android.view.View;

import java.util.List;

import domain.BitmapUtil;
import domain.ProductsInteractor;
import domain.RVAllProductsView;
import domain.RVBoughtProductsView;
import entity.Product;

public class RVBoughtProductsPresenter {
    private RVBoughtProductsView rvBoughtProductsView;
    private boolean longTouchMode;
    private List<Product> list;

    public RVBoughtProductsPresenter(RVBoughtProductsView rvBoughtProductsView, List<Product> list) {
        this.rvBoughtProductsView = rvBoughtProductsView;
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
                rvBoughtProductsView.notifyDataSetChanged();
                return;
            }
        }
        if (!list.get(list.size() - 1).isChecked()) {
            longTouchMode = false;
        }
        rvBoughtProductsView.notifyDataSetChanged();
        rvBoughtProductsView.notifyLongTouchModeDisabled();
    }

    public void onLongClick(int position) {
        list.get(position).setChecked(true);
        longTouchMode = true;
        rvBoughtProductsView.notifyDataSetChanged();
        rvBoughtProductsView.notifyLongTouchModeEnabled();
    }
    public void onBackButtonClicked(){
        longTouchMode = false;
        rvBoughtProductsView.notifyLongTouchModeDisabled();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(false);
        }
        rvBoughtProductsView.notifyDataSetChanged();
    }
    public void onSetAllButtonClicked(){
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setChecked(true);
        }
        rvBoughtProductsView.notifyDataSetChanged();
    }
    public void onMoveButtonClicked(){
        for (int i=0;i<list.size();i++){
            if (list.get(i).isChecked()){
            ProductsInteractor.changeBuyStatus(false, list.get(i).getId());
            }
        }
        onBackButtonClicked();
    }
}


