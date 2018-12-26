package solutions.nix.shoppinglist;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import domain.BitmapUtil;
import domain.ProductsInteractor;
import domain.MainView;
import domain.ViewPagerAdapter;
import presentation.MainPresenter;
import repository.SQLAdapter;
import solutions.nix.shoppinglist.fragments.FragmentAllProducts;
import solutions.nix.shoppinglist.fragments.FragmentBoughtProducts;

public class MainActivity extends AppCompatActivity implements MainView {

    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setViewPager();
        SQLAdapter dbHelper = new SQLAdapter(this);
        dbHelper.open();
        ProductsInteractor productsInteractor = new ProductsInteractor(dbHelper);

        mainPresenter = new MainPresenter();
        mainPresenter.onAttach(this);

        // insert some standard products for test
        BitmapUtil bitmapUtil = new BitmapUtil();
        dbHelper.deleteAllProducts();
        int width =150;
        int height = 150;
        dbHelper.createProduct("Glasses", "cool glasses","$100",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.glasses), width, height,false)),false);
        dbHelper.createProduct("Iron", "heavy iron","$50",false,true,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.iron), width, height,false)),false);
        dbHelper.createProduct("phone", "white phone","$1000",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.phone), width, height,false)),false);
        dbHelper.createProduct("socks","warm socks","$5",false,true,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.socks), width, height,false)),false);
        dbHelper.createProduct("perforator","super powerfull","$500",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.perforator), width, height,false)),false);
        dbHelper.createProduct("telly","color television","$400",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tv), width, height,false)),false);
        dbHelper.createProduct("car","fast car","$40000",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.car), width, height,false)),false);
        dbHelper.createProduct("umbrella","nice umbrella","$100",false,false,bitmapUtil.getBytes(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.umbrella), width, height,false)),false);

    }
    private void setViewPager(){
        ViewPager viewPager = findViewById(R.id.viewpager_id);
        ViewPagerAdapter viewPagerAdapteradapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapteradapter.addFragment(new FragmentAllProducts(),getResources().getString(R.string.shopping_list));
        viewPagerAdapteradapter.addFragment(new FragmentBoughtProducts(),getResources().getString(R.string.purchased));
        viewPager.setAdapter(viewPagerAdapteradapter);

    }
    @Override
    protected void onDestroy() {
        mainPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
