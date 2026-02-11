package gpg.finalapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    String[] imageArray = {
            "https://rukminim2.flixcart.com/image/612/612/xif0q/t-shirt/p/s/r/m-6002-never-royal-blue-m-gm-trends-original-imahj6jgp3ywwwgh.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/612/612/xif0q/t-shirt/y/c/3/l-tblbghn-d213-tripr-original-imahj8x8nae4aazn.jpeg?q=70",
            "https://rukminim2.flixcart.com/image/612/612/xif0q/track-pant/v/b/6/xl-spider-mk-brothers-original-imahj6xuj2ryrver.jpeg?q=70"
    };

    SharedPreferences sp;

    ArrayList<ProductList> arrayList;

    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        db = openOrCreateDatabase("GpgApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(50),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTable = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYNAME VARCHAR(50),CATEGORYIMAGE VARCHAR(200))";
        db.execSQL(categoryTable);

        String subcategoryTable = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID VARCHAR(10), SUBCATEGORYNAME VARCHAR(50))";
        db.execSQL(subcategoryTable);

        String productTable = "CREATE TABLE IF NOT EXISTS PRODUCT(PRODUCTID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "SUBCATEGORYID VARCHAR(10), VENDORNAME VARCHAR(50), PRODUCTNAME VARCHAR(50), " +
                "ORIGINALPRICE VARCHAR(20), DISCOUNTEDPRICE VARCHAR(20), DISCOUNT VARCHAR(10), " +
                "IMAGE VARCHAR(200))";
        db.execSQL(productTable);




        sp = getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
        productRecycler = findViewById(R.id.product_recyclerview);

        productRecycler.setLayoutManager(new LinearLayoutManager(ProductActivity.this));

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
                list.setImage(imageArray[i]);
                arrayList.add(list);
            }
        }

        for(int i=0; i<productNameArray.length;i++){
            String checkProduct = "SELECT * FROM PRODUCT WHERE PRODUCTNAME = '"+productNameArray[i]+"' AND VENDORNAME = '"+vendorNameArray[i]+"' ";
            Cursor cursor = db.rawQuery(checkProduct, null);

            if(cursor.getCount()==0){
                String insertProduct = "INSERT INTO PRODUCT VALUES (NULL, '"+subcategoryIdArray[i]+"'," +
                        " '"+vendorNameArray[i]+"', '"+productNameArray[i]+"'," +
                        " '"+originalPriceArray[i]+"', '"+discountedPriceArray[i]+"', " +
                        "'"+discountArray[i]+"', '"+imageArray[i]+"' )";

                db.execSQL(insertProduct);
            }
        }

        ProductAdapter adapter = new ProductAdapter(ProductActivity.this, arrayList);
        productRecycler.setAdapter(adapter);






    }
}