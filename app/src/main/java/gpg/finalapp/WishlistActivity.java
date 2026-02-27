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

import java.util.ArrayList;

public class WishlistActivity extends AppCompatActivity {
    RecyclerView wishlist_recycler;
    SQLiteDatabase db;
    SharedPreferences sp;

    ArrayList<WishlistList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlist);

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

        wishlist_recycler = findViewById(R.id.wishlist_recyclerview);
        wishlist_recycler.setLayoutManager(new LinearLayoutManager(WishlistActivity.this));

        arrayList = new ArrayList<WishlistList>();

        String checkWishlist = "SELECT * FROM wishlist WHERE userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(checkWishlist, null);
        if(cursor.getCount()>0){
            while(cursor.moveToNext()){
                WishlistList list = new WishlistList();
                list.setWishlistId(cursor.getInt(0));
                String checkProduct = "SELECT * FROM PRODUCT WHERE PRODUCTID = '"+cursor.getString(1)+"'";
                Cursor productCursor = db.rawQuery(checkProduct, null);
                while(productCursor.moveToNext()){
                    list.setProductName(productCursor.getString(3));
                    list.setProductVendorName(productCursor.getString(2));
                    list.setProductOriginalPrice(productCursor.getString(4));
                    list.setProductDiscountedPrice(productCursor.getString(5));
                    list.setDiscount(productCursor.getString(6));
                    list.setProductImage(productCursor.getString(7));
                }
                arrayList.add(list);
            }
        }

        WishlistAdapter adapter = new WishlistAdapter(WishlistActivity.this, arrayList, db);
        wishlist_recycler.setAdapter(adapter);


    }
}