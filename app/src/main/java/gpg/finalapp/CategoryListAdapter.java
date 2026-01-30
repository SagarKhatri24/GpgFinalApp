package gpg.finalapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CategoryListAdapter extends BaseAdapter {

    Context context;
    /*String[] categoryArray;
    String[] categoryImageArray;

    public CategoryListAdapter(Context context, String[] categoryArray, String[] categoryImageArray) {
        this.context = context;
        this.categoryArray = categoryArray;
        this.categoryImageArray = categoryImageArray;
    }*/

    ArrayList<CategoryList> arrayList;

    public CategoryListAdapter(Context context, ArrayList<CategoryList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        //return categoryArray.length;
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        //return categoryArray[i];
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.custom_category,null);
        TextView name = view.findViewById(R.id.custom_category_name);
        ImageView image = view.findViewById(R.id.custom_category_image);

        /*name.setText(categoryArray[i]);
        Glide.with(context).load(categoryImageArray[i]).placeholder(R.mipmap.ic_launcher).into(image);*/

        name.setText(arrayList.get(i).getName());
        Glide.with(context).load(arrayList.get(i).getImage()).placeholder(R.mipmap.ic_launcher).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Image Click", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
