package solutions.nix.shoppinglist.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Objects;
import domain.AllProductsView;
import domain.AllProductsRVAdaper;
import entity.Product;
import presentation.AllProductsPresenter;
import solutions.nix.shoppinglist.R;
public class FragmentAllProducts extends Fragment implements AllProductsView {

    private ImageView imageView;
    private AllProductsPresenter allProductsPresenter;
    private AllProductsRVAdaper adapter;
    private Bitmap photoBitmap;
    private AlertDialog alertDialog;
    private LinearLayoutCompat bottomMenu;
    private ImageButton back;
    private ImageButton selectAll;
    private ImageButton move;
    private FloatingActionButton fab;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.all_products_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        allProductsPresenter = new AllProductsPresenter();
        allProductsPresenter.onAttach(this);
        bottomMenu = getView().findViewById(R.id.bottom_menu);
        move = getView().findViewById(R.id.move);
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onMoveButtonClicked();
            }
        });
        back = getView().findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onClickBack();
            }
        });
        selectAll = getView().findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allProductsPresenter.onClicSelectAll();
            }
        });
        move = getView().findViewById(R.id.move);
        fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allProductsPresenter.onClickFab();
            }
        });
    }
    @Override
    public void creatAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Add new item");
        final View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.add_item_layout, null);

        final EditText nameInput = (EditText) viewInflated.findViewById(R.id.name);
        final EditText descriptionInput = (EditText) viewInflated.findViewById(R.id.description);
        final EditText priceInput = (EditText) viewInflated.findViewById(R.id.price);
        Button cameraButton = (Button) viewInflated.findViewById(R.id.cameraButton);
        Button galleryButton = (Button) viewInflated.findViewById(R.id.galleryButton);
        imageView = (ImageView) viewInflated.findViewById(R.id.imageView);
        photoBitmap = null;

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allProductsPresenter.onClickCameraButton();
            }
        });
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               allProductsPresenter.onClickGalleryButton();
            }
        });

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.setNegativeButton(android.R.string.cancel,null);
        alertDialog = builder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameInput.getText().toString();
                String description = descriptionInput.getText().toString();
                String price = priceInput.getText().toString();
                Bitmap photo = photoBitmap;

                allProductsPresenter.checkProduct(name, description, price, photo);
                }
            });
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    @Override
    public void creatEmptyLineAlertToast() {
        Snackbar.make(Objects.requireNonNull(getView()), getResources().getString(R.string.fill_all), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void creatEmptyBitmapAlertToast() {
        Snackbar.make(Objects.requireNonNull(getView()), getResources().getString(R.string.choose_photo), Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
    @Override
    public void dismissAlertDialog(){
        alertDialog.dismiss();
    }

    @Override
    public void showBottomMenu() {
        bottomMenu.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBottomMenu() {
        bottomMenu.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onDestroyView() {
        allProductsPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void initProductsList(ArrayList<Product> arrayList) {
        RecyclerView recyclerView = getView().findViewById(R.id.all_products_rv);
        adapter = new AllProductsRVAdaper(arrayList,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        allProductsPresenter.onPermissionResult( requestCode,  permissions,  grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        allProductsPresenter.onActivityResult(requestCode,resultCode,data);
    }
    @Override
    public void notifyLongTouchModeEnabled(){
        allProductsPresenter.notifyLongTouchModeEnabled();
    }

    @Override
    public void hideFloatingButton() {
        fab.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showFloatingButton() {
        fab.setVisibility(View.VISIBLE);
    }

    @Override
    public void notifyBackButtonClicked() {
        adapter.notifyBackButtonClicked();
    }

    @Override
    public void notifySetAllButtonClicked() {
        adapter.notifySetAllButtonClicked();
    }

    @Override
    public void notifyMoveButtonClicked() {
        adapter.notifyMoveButtonClicked();
    }

    @Override
    public Fragment getViewActivity() {
        return this;
    }

    @Override
    public Context getFragmentContext() {
        return getContext();
    }
    @Override
    public void setImageBitmap(Bitmap photoBitmap) {
        imageView.setImageBitmap(photoBitmap);
        this.photoBitmap = photoBitmap;
    }

    @Override
    public void notifyLongTouchModeDisabled(){
        bottomMenu.setVisibility(View.INVISIBLE);
        allProductsPresenter.notifyLongTouchModeDisabled();
    }
}
