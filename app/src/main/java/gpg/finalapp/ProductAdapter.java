package gpg.finalapp;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
    Context context;
    ArrayList<ProductList> arrayList;
    SharedPreferences sp;

    public ProductAdapter(Context context, ArrayList<ProductList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        sp = context.getSharedPreferences(ConstantSp.PREF,MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_layout,parent,false);
        return new ProductAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView productName, vendorName, originalPrice, discountedPrice, discount;
        ImageView image;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.custom_product_layout_name);
            vendorName = itemView.findViewById(R.id.custom_product_layout_vendor_name);
            originalPrice = itemView.findViewById(R.id.custom_product_layout_original_price);
            discountedPrice = itemView.findViewById(R.id.custom_product_layout_discount_price);
            discount = itemView.findViewById(R.id.custom_product_layout_discount);
            image = itemView.findViewById(R.id.custom_product_image);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.vendorName.setText(arrayList.get(position).getVendorName());
        holder.originalPrice.setText(arrayList.get(position).getOriginalPrice());
        holder.discountedPrice.setText(arrayList.get(position).getDiscountedPrice());
        holder.discount.setText(arrayList.get(position).getDiscount());
        holder.image.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putInt(ConstantSp.PRODUCT_ID, arrayList.get(position).getProductId()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_NAME, arrayList.get(position).getProductName()).commit();
                sp.edit().putString(ConstantSp.PRODUCT_VENDOR_NAME, arrayList.get(position).getVendorName()).commit();
                sp.edit().putString(ConstantSp.originalPrice, arrayList.get(position).getOriginalPrice()).commit();
                sp.edit().putString(ConstantSp.discountedPrice, arrayList.get(position).getDiscountedPrice()).commit();
                sp.edit().putString(ConstantSp.discount, arrayList.get(position).getDiscount()).commit();
                sp.edit().putString(ConstantSp.product_image, arrayList.get(position).getImage()).commit();

                Intent intent = new Intent(context, ProductDetailActivity.class);
                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
