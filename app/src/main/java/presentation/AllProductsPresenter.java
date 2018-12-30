package presentation;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import domain.AllProductsView;
import domain.BitmapUtil;
import domain.IntentServiceResult;
import domain.ProductsInteractor;
import entity.Product;
import solutions.nix.shoppinglist.R;

import static android.app.Activity.RESULT_OK;

public class AllProductsPresenter {
    public static final int DB_UPDATED = 201;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_PERMISSIONS_READ_EXTERNAL_STORAGE = 2;
    private static final int RESULT_LOAD_IMAGE = 3;
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
        if (ContextCompat.checkSelfPermission(allProductsView.getFragmentContext(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionCamera();
            }else takePhoto();
        }else takePhoto();
    }
    public void onClickGalleryButton() {
        if (ContextCompat.checkSelfPermission(allProductsView.getFragmentContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPermissionGallery();
            }else openGallery();
        }else openGallery();
    }
    private void getPermissionCamera() {
        allProductsView.getViewActivity().requestPermissions(
                new String[]{Manifest.permission.CAMERA},
                MY_PERMISSIONS_REQUEST_CAMERA);
    }

    private void getPermissionGallery() {
        allProductsView.getViewActivity().requestPermissions(
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                MY_PERMISSIONS_READ_EXTERNAL_STORAGE);
    }

    public void onPermissionResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();

                } else {
                    Toast.makeText(allProductsView.getFragmentContext(), R.string.access_denied, Toast.LENGTH_LONG).show();
                }
                return;
            }
            case MY_PERMISSIONS_READ_EXTERNAL_STORAGE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery();

                } else {
                    Toast.makeText(allProductsView.getFragmentContext(), R.string.access_denied, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        Log.i("onActivityResult",String.valueOf(resultCode));
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photoBitmap = Bitmap.createScaledBitmap((Bitmap) extras.get("data"), 150, 150, false);
            allProductsView.setImageBitmap(photoBitmap);
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            Bitmap extras = null;
            try {
                extras = MediaStore.Images.Media.getBitmap(allProductsView.getFragmentContext().getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Bitmap photoBitmap = Bitmap.createScaledBitmap((Bitmap) extras, 150, 150, false);
            allProductsView.setImageBitmap(photoBitmap);
        }}
    private void takePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(allProductsView.getFragmentContext().getPackageManager()) != null) {
            allProductsView.getViewActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        allProductsView.getViewActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), RESULT_LOAD_IMAGE);
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
