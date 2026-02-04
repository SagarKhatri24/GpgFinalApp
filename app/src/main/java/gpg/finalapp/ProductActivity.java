package gpg.finalapp;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {
    RecyclerView productRecycler;

    int[] productIdArrary = {1,2,3};
    int[] subcategoryIdArray = {1,1,2};
    String[] vendorNameArray ={"GM Traders", "Max", "Pantaloon"};
    String[] productNameArray ={"Printed Polo T-Shirt", "Cotton Hoodie", "Solid Balck Jeans"};
    String[] originalPriceArray ={"599", "899", "999"};
    String[] discountedPriceArray ={"399", "649", "799"};
    String[] discountArray = {"18","16","20"};

    SharedPreferences sp;

    ArrayList<ProductList> arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
        productRecycler = findViewById(R.id.product_recyclerview);

        productRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        arrayList = new ArrayList<ProductList>();

        for(int i=0;i<productNameArray.length;i++){
            if(sp.getInt(ConstantSp.SUBCATEGORY_ID,0) == subcategoryIdArray[i]) {
                ProductList list = new ProductList();
                list.setProductId(productIdArrary[i]);
                list.setSubcategoryId(subcategoryIdArray[i]);
                list.setVendorName(vendorNameArray[i]);
                list.setProductName(productNameArray[i]);
                list.setOriginalPrice(originalPriceArray[i]);
                list.setDiscountedPrice(discountedPriceArray[i]);
                list.setDiscount(discountArray[i]);
                arrayList.add(list);
            }
        }

        ProductAdapter adapter = new ProductAdapter(ProductActivity.this, arrayList);
        productRecycler.setAdapter(adapter);






    }
}