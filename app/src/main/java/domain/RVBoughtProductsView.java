package domain;

import android.graphics.Bitmap;

public interface RVBoughtProductsView {
    void notifyDataSetChanged();
    void notifyLongTouchModeDisabled();
    void notifyLongTouchModeEnabled();

        interface ViewHolder{
            void setName(String name);
            void setDescription(String description);
            void setPrice(String price);
            void setImage(Bitmap image);
            void setChecked(boolean value);
            void setCheckBoxVisibility(int value);

        }
}
