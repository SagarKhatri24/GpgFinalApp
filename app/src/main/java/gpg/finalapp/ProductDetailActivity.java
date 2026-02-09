package gpg.finalapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    ImageView productImage, cart;
    TextView productName, vendorName, originalPrice, discountedPrice, discount;
    LinearLayout cart_layout;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        productImage = findViewById(R.id.product_detail_image);
        productName = findViewById(R.id.product_detail_name);
        vendorName = findViewById(R.id.product_detail_vendor_name);
        originalPrice = findViewById(R.id.product_detail_price);
        discountedPrice = findViewById(R.id.product_detail_after_discount_price);
        discount = findViewById(R.id.product_detail_discount);

        cart = findViewById(R.id.product_detail_cart);
        cart_layout = findViewById(R.id.product_detail_cart_layout);



        Glide.with(ProductDetailActivity.this).load(sp.getString(ConstantSp.product_image,""))
                .placeholder(R.mipmap.ic_launcher).into(productImage);

        productName.setText(sp.getString(ConstantSp.PRODUCT_NAME, ""));
        vendorName.setText(sp.getString(ConstantSp.PRODUCT_VENDOR_NAME, ""));
        originalPrice.setText(sp.getString(ConstantSp.originalPrice,""));
        discountedPrice.setText(sp.getString(ConstantSp.discountedPrice,""));
        discount.setText(sp.getString(ConstantSp.discount,""));



        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cart.setVisibility(GONE);
                cart_layout.setVisibility(VISIBLE);
            }
        });









    }
}