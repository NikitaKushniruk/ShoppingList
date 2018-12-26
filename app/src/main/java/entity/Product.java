package entity;

import android.graphics.Bitmap;

import java.lang.ref.SoftReference;

public class Product {

    private long id;
    private String name;
    private String description;
    private String price;
    private boolean liked;
    private boolean bought;
    private byte[] foto;
    private boolean isChecked;

    public Product(long id, String name, String description, String price, boolean liked, boolean bought, byte[] foto, boolean isChecked) {
        this.id= id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.liked = liked;
        this.bought = bought;
        this.foto = foto;
        this.isChecked = isChecked;

    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public boolean isLiked() {
        return liked;
    }

    public boolean isBought() {
        return bought;
    }

    public byte[] getFoto() {
        return foto;
    }
    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
    public long getId() {
        return id;
    }

}
