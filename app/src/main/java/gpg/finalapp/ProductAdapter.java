package gpg.finalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
    Context context;
    ArrayList<ProductList> arrayList;

    public ProductAdapter(Context context, ArrayList<ProductList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_layout,parent,false);
        return new ProductAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView productName, vendorName, originalPrice, discountedPrice, discount;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.custom_product_layout_name);
            vendorName = itemView.findViewById(R.id.custom_product_layout_vendor_name);
            originalPrice = itemView.findViewById(R.id.custom_product_layout_original_price);
            discountedPrice = itemView.findViewById(R.id.custom_product_layout_discount_price);
            discount = itemView.findViewById(R.id.custom_product_layout_discount);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
        holder.productName.setText(arrayList.get(position).getProductName());
        holder.vendorName.setText(arrayList.get(position).getVendorName());
        holder.originalPrice.setText(arrayList.get(position).getOriginalPrice());
        holder.discountedPrice.setText(arrayList.get(position).getDiscountedPrice());
        holder.discount.setText(arrayList.get(position).getDiscount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}
