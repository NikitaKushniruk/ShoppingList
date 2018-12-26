package domain;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import entity.Product;
import repository.SQLAdapter;

public class ProductsInteractor {
    private static SQLAdapter dbHelper;
    public ProductsInteractor(SQLAdapter dbHelper) {
        this.dbHelper = dbHelper;
    }
    public static List<Product> getProducts(boolean isBought){
        List<Product> allproducts = new ArrayList<>();
        Cursor cursor = dbHelper.fetchAllProducts();
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            do {
                long id = cursor.getLong(cursor.getColumnIndex("_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String description = cursor.getString(cursor.getColumnIndex("description"));
                String price = cursor.getString(cursor.getColumnIndex("price"));
                boolean liked = cursor.getInt(cursor.getColumnIndex("liked"))>0;
                boolean bought = cursor.getInt(cursor.getColumnIndex("bought"))>0;
                byte[] photo = cursor.getBlob(cursor.getColumnIndex("photo"));
                boolean isChecked = cursor.getInt(cursor.getColumnIndex("isChecked"))>0;
                Product product = new Product(id,name,description,price,liked,bought,photo,isChecked);
                if ( bought == isBought )
                allproducts.add(product);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return allproducts;
    }
    public static void creatProduct(String name, String description, String price, boolean liked, boolean bought, byte[] photo, boolean isChecked){
        dbHelper.createProduct(name,description,price,liked,bought,photo,isChecked);
    }
    public static void changeBuyStatus(boolean bought, long id){
        dbHelper.changeBuyStatus(bought,id);
    }
}
