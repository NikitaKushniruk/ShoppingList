package domain;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import entity.Product;
import presentation.BoughtProductsPresenter;
import presentation.RVAllProductsPresenter;
import presentation.RVBoughtProductsPresenter;
import solutions.nix.shoppinglist.R;

public class BoughtProductsRVAdaper extends RecyclerView.Adapter<BoughtProductsRVAdaper.ViewHolder> implements RVBoughtProductsView{

    private List <Product> list;
    private RVBoughtProductsPresenter presenter;
    BoughtProductsView boughtProductsView;
    public BoughtProductsRVAdaper(List<Product> list, BoughtProductsView boughtProductsView) {
        this.list = list;
        this.boughtProductsView = boughtProductsView;
        presenter = new RVBoughtProductsPresenter(this,list);
    }
    @NonNull

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        presenter.bindEventRow(position, holder);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onClickCheckbox(position);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                presenter.onLongClick(position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void notifyLongTouchModeEnabled() {
        boughtProductsView.notifyLongTouchModeEnabled();
    }

    @Override
    public void notifyLongTouchModeDisabled() {
        boughtProductsView.notifyLongTouchModeDisabled();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements RVAllProductsView.ViewHolder {

        private TextView name;
        private TextView description;
        private TextView price;
        private ImageView image;
        private CheckBox checkBox;

        public ViewHolder(final View itemView)  {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            image = itemView.findViewById(R.id.imageView);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
        @Override
        public void setName(String name) {
           this.name.setText(name);
        }
        @Override
        public void setDescription(String description) {
            this.description.setText(description);
        }
        @Override
        public void setPrice(String price) {
            this.price.setText(price);
        }
        @Override
        public void setImage(Bitmap image) {
            this.image.setImageBitmap(image);
        }
        @Override
        public void setChecked(boolean value) {
            this.checkBox.setChecked(value);
        }

        @Override
        public void setCheckBoxVisibility(int value) {
            this.checkBox.setVisibility(value);
        }

    }
    public void notifyBackButtonClicked(){
        presenter.onBackButtonClicked();
    }
    public void notifySetAllButtonClicked(){
        presenter.onSetAllButtonClicked();
    }
    public void notifyMoveButtonClicked(){
        presenter.onMoveButtonClicked();
    }
}
