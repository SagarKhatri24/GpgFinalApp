package gpg.finalapp;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.session.PlaybackState;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    RecyclerView cart_recycler;

    SQLiteDatabase db;
    SharedPreferences sp;

    ArrayList<CartList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

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

        String cartTable = "CREATE TABLE IF NOT EXISTS cart (cartId INTEGER PRIMARY KEY AUTOINCREMENT, productId VARCHAR(10), userId VARCHAR(10), qty VARCHAR(5) )";
        db.execSQL(cartTable);

        String wishlistTable = "CREATE TABLE IF NOT EXISTS wishlist(wishlistId INTEGER PRIMARY KEY AUTOINCREMENT, productId VARCHAR(10), userId VARCHAR(10))";
        db.execSQL(wishlistTable);


        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        cart_recycler = findViewById(R.id.cart_recyclerview);

        cart_recycler.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        arrayList = new ArrayList<CartList>();
        String checkCart = "SELECT * FROM cart WHERE userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(checkCart, null);

        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                CartList list = new CartList();
                list.setQty(cursor.getInt(3));

                String checkProduct = "SELECT * FROM PRODUCT WHERE PRODUCTID = '"+cursor.getString(1)+"'";
                Cursor productCursor = db.rawQuery(checkProduct, null);

                while(productCursor.moveToNext()){
                    list.setProductId(productCursor.getInt(0));
                    list.setProductName(productCursor.getString(3));
                    list.setVendorName(productCursor.getString(2));
                    list.setOriginalPrice(productCursor.getString(4));
                    list.setDiscountPrice(productCursor.getString(5));
                    list.setDiscount(productCursor.getString(6));
                    list.setImage(productCursor.getString(7));
                }
                arrayList.add(list);
            }
        }

        CartAdapter adapter = new CartAdapter(CartActivity.this, arrayList, db);
        cart_recycler.setAdapter(adapter);



    }
}