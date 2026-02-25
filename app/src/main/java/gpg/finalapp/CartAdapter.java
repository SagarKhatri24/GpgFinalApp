package gpg.finalapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {

    Context context;
    ArrayList<CartList> arrayList;
    SQLiteDatabase db;

    public CartAdapter(Context context, ArrayList<CartList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart,parent,false);
        return new CartAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView productImage, delete;
        TextView productName, vendorName, originalPrice, discountedPrice, discount, qty;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.custom_product_image);
            delete = itemView.findViewById(R.id.custom_cart_delete);
            productName = itemView.findViewById(R.id.custom_cart_name);
            vendorName = itemView.findViewById(R.id.custom_cart_vendor_name);
            originalPrice = itemView.findViewById(R.id.custom_cart_original_price);
            discountedPrice = itemView.findViewById(R.id.custom_cart_discount_price);
            discount = itemView.findViewById(R.id.custom_cart_discount);
            qty = itemView.findViewById(R.id.custom_cart_qty);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.vendorName.setText(arrayList.get(position).getVendorName());
        holder.originalPrice.setText("₹"+arrayList.get(position).getOriginalPrice());
        holder.discountedPrice.setText("₹"+arrayList.get(position).getDiscountPrice());
        holder.discount.setText(arrayList.get(position).getDiscount()+"%");

        Log.d("TAG", "onBindViewHolder: "+arrayList.get(position).getQty()+"");

        holder.qty.setText(String.valueOf(arrayList.get(position).getQty()));

        holder.originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
//        holder.image.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        Glide.with(context).load(arrayList.get(position).getImage()).placeholder(R.mipmap.ic_launcher).into(holder.productImage);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deleteItem = "DELETE FROM cart WHERE cartId='"+arrayList.get(position).getCartId()+"'";
                db.execSQL(deleteItem);
                arrayList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
