package gpg.finalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.MyHolder> {
    Context context;
    ArrayList<WishlistList> arrayList;
    SQLiteDatabase db;

    public WishlistAdapter(Context context, ArrayList<WishlistList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public WishlistAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_wishlist,parent,false);
        return new WishlistAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        ImageView image, wishlist;
        TextView productName, vendorName, originalPrice, discountedPrice, discount;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.custom_product_image);
            wishlist = itemView.findViewById(R.id.custom_wishlist_fill);
            productName = itemView.findViewById(R.id.custom_wishlist_name);
            vendorName = itemView.findViewById(R.id.custom_wishlist_vendor_name);
            originalPrice = itemView.findViewById(R.id.custom_wishlist_original_price);
            discountedPrice = itemView.findViewById(R.id.custom_wishlist_discount_price);
            discount = itemView.findViewById(R.id.custom_wishlist_discount);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistAdapter.MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.vendorName.setText(arrayList.get(position).getProductVendorName());
        holder.originalPrice.setText("₹"+arrayList.get(position).getProductOriginalPrice());
        holder.discountedPrice.setText("₹"+arrayList.get(position).getProductDiscountedPrice());
        holder.discount.setText(arrayList.get(position).getDiscount()+"%");

        Glide.with(context).load(arrayList.get(position).getProductImage()).placeholder(R.mipmap.ic_launcher).into(holder.image);

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteItem = "DELETE FROM wishlist WHERE wishlistId = '"+arrayList.get(position).getWishlistId()+"'";
                db.execSQL(deleteItem);
                arrayList.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Removed form Wishlist", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


}
