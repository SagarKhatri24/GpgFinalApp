package gpg.finalapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity implements PaymentResultWithDataListener {

    ImageView productImage, cart, minus, plus, wishlistEmpty, wishlistFill, wishlist;
    TextView productName, vendorName, originalPrice, discountedPrice, discount, cartQty;
    LinearLayout cart_layout;
    Button buy_now;

    SharedPreferences sp;

    SQLiteDatabase db;

    int qty = 0;

    boolean isWishlist = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

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

        productImage = findViewById(R.id.product_detail_image);
        productName = findViewById(R.id.product_detail_name);
        vendorName = findViewById(R.id.product_detail_vendor_name);
        originalPrice = findViewById(R.id.product_detail_price);
        discountedPrice = findViewById(R.id.product_detail_after_discount_price);
        discount = findViewById(R.id.product_detail_discount);

        cart = findViewById(R.id.product_detail_cart);
        cart_layout = findViewById(R.id.product_detail_cart_layout);
        minus = findViewById(R.id.product_detail_cart_minus);
        plus = findViewById(R.id.product_detail_cart_add);
        cartQty = findViewById(R.id.product_detail_cart_qty);

//        wishlistEmpty = findViewById(R.id.product_detail_wishlist_empty);
//        wishlistFill = findViewById(R.id.product_detail_wishlist_fill);
        wishlist = findViewById(R.id.product_detail_wishlist);

        buy_now = findViewById(R.id.buy_now);




        Glide.with(ProductDetailActivity.this).load(sp.getString(ConstantSp.product_image,""))
                .placeholder(R.mipmap.ic_launcher).into(productImage);

        productName.setText(sp.getString(ConstantSp.PRODUCT_NAME, ""));
        vendorName.setText(sp.getString(ConstantSp.PRODUCT_VENDOR_NAME, ""));
        originalPrice.setText("₹"+sp.getString(ConstantSp.originalPrice,""));
        discountedPrice.setText("₹"+sp.getString(ConstantSp.discountedPrice,""));
        discount.setText(sp.getString(ConstantSp.discount,"")+"%");

        originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);


        checkItemInCart();
        checkItemInWishlist();



        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String checkItem = "SELECT * FROM cart WHERE " +
                        "productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND " +
                        "userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
                Cursor cursor = db.rawQuery(checkItem,null);

                if(cursor.getCount() == 0){
                    qty=1;
                        String insertItem = "INSERT INTO cart VALUES(NULL,'"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"', '"+sp.getString(ConstantSp.USERID,"")+"', '"+qty+"')";
                    db.execSQL(insertItem);

                    cart.setVisibility(GONE);
                    cart_layout.setVisibility(VISIBLE);
                    Toast.makeText(ProductDetailActivity.this, "Added To Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty--;
                if(qty==0){
                    cart.setVisibility(VISIBLE);
                    cart_layout.setVisibility(GONE);

                    String deleteItem = "DELETE FROM cart WHERE productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND userId - '"+sp.getString(ConstantSp.USERID,"")+"'";
                    db.execSQL(deleteItem);

                    Toast.makeText(ProductDetailActivity.this, "Removed From Cart", Toast.LENGTH_LONG).show();
                }
                else{
                    cartQty.setText(String.valueOf(qty));

                    String updateItem = "UPDATE cart SET qty = '"+qty+"' WHERE productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
                    db.execSQL(updateItem);
                }
            }
        });


        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                qty++;
                cartQty.setText(String.valueOf(qty));

                String updateItem = "UPDATE cart SET qty = '"+qty+"' WHERE productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
                db.execSQL(updateItem);

            }
        });




//        wishlistEmpty.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                wishlistEmpty.setVisibility(GONE);
//                wishlistFill.setVisibility(VISIBLE);
//
//                String insertInWishlist = "INSERT INTO wishlist VALUES(NULL, '"+sp.getInt(ConstantSp.PRODUCT_ID, 0)+"', '"+sp.getString(ConstantSp.USERID, "")+"')";
//                db.execSQL(insertInWishlist);
//
//                Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();;
//            }
//        });
//
//        wishlistFill.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                wishlistEmpty.setVisibility(VISIBLE);
//                wishlistFill.setVisibility(GONE);
//
//                String removeFromWishlist = "DELETE FROM wishlist WHERE productId = '"+sp.getInt(ConstantSp.PRODUCT_ID, 0)+"' AND userId = '"+sp.getString(ConstantSp.USERID, "")+"'";
//                db.execSQL(removeFromWishlist);
//
//                Toast.makeText(ProductDetailActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();;
//
//            }
//        });




        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isWishlist){
                    isWishlist = false;

                    String removeFromWishlist = "DELETE FROM wishlist WHERE productId = '"+sp.getInt(ConstantSp.PRODUCT_ID, 0)+"' AND userId = '"+sp.getString(ConstantSp.USERID, "")+"'";
                    db.execSQL(removeFromWishlist);

                    wishlist.setImageResource(R.drawable.wishlist_empty);
                    Toast.makeText(ProductDetailActivity.this, "Removed from wishlist", Toast.LENGTH_SHORT).show();;
                }
                else{
                    isWishlist = true;

                    String insertInWishlist = "INSERT INTO wishlist VALUES(NULL, '"+sp.getInt(ConstantSp.PRODUCT_ID, 0)+"', '"+sp.getString(ConstantSp.USERID, "")+"')";
                    db.execSQL(insertInWishlist);

                    wishlist.setImageResource(R.drawable.wishlist_fill);
                    Toast.makeText(ProductDetailActivity.this, "Added to wishlist", Toast.LENGTH_SHORT).show();;
                }
            }
        });



        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startpayment();
            }
        });


    }

    private void startpayment() {
        final Activity activity = this;
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_xsiOz9lYtWKHgF");

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.app_name));
            options.put("description", "Purchase Deal From " + getResources().getString(R.string.app_name));
            options.put("send_sms_hash", true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", R.mipmap.ic_launcher);
            options.put("currency", "INR");
            options.put("amount", String.valueOf(Integer.parseInt(sp.getString(ConstantSp.discountedPrice,"")) * 100));

            JSONObject preFill = new JSONObject();
            preFill.put("email", "khatrisagar2@gmail.com");
            preFill.put("contact", "7878232386");
            options.put("prefill", preFill);

            checkout.open(activity, options);

        } catch(Exception e) {
            Log.e("RESPONSE", "Error in starting Razorpay Checkout", e);
        }
    }


    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Sucessfull", Toast.LENGTH_SHORT).show();
        Log.d("RESPONSE_SUCCESS","Transaction Id : "+s);

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
        Log.d("RESPONSE_SUCCESS","Transaction Id : "+s);

    }


//
//    private void checkItemInWishlist() {
//        String checkItem = "SELECT * FROM wishlist WHERE " +
//                "productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND " +
//                "userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
//        Cursor cursor = db.rawQuery(checkItem,null);
//
//        if(cursor.getCount() > 0){
//            wishlistEmpty.setVisibility(GONE);
//            wishlistFill.setVisibility(VISIBLE);
//        }
//        else{
//            wishlistEmpty.setVisibility(VISIBLE);
//            wishlistFill.setVisibility(GONE);
//        }
//    }





    private void checkItemInWishlist() {
        String checkItem = "SELECT * FROM wishlist WHERE " +
                "productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND " +
                "userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(checkItem,null);

        if(cursor.getCount() > 0){
            isWishlist = true;
            wishlist.setImageResource(R.drawable.wishlist_fill);
        }
        else{
            isWishlist = false;
            wishlist.setImageResource(R.drawable.wishlist_empty);
        }
    }




    private void checkItemInCart() {
        String checkItem = "SELECT * FROM cart WHERE " +
                "productId = '"+sp.getInt(ConstantSp.PRODUCT_ID,0)+"' AND " +
                "userId = '"+sp.getString(ConstantSp.USERID,"")+"'";
        Cursor cursor = db.rawQuery(checkItem,null);

        if(cursor.getCount()>0){
            while (cursor.moveToNext()){
                qty = Integer.parseInt(cursor.getString(3));
                cartQty.setText(String.valueOf(qty));
            }

            cart.setVisibility(GONE);
            cart_layout.setVisibility(VISIBLE);

        }
        else{
            cart.setVisibility(VISIBLE);
            cart_layout.setVisibility(GONE);
        }
    }
}