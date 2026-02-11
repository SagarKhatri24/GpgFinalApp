package gpg.finalapp;

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

public class CategoryRecyclerActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    SQLiteDatabase db;

    int[] idArray = {1,2,3,4,5,6,7,8,9};

    String[] categoryArray = {
            "Minutes",
            "Mobiles & Tablets",
            "Fashion",
            "Electronics",
            "TVs & Appliances",
            "Home & Furniture",
            "Flight Bookings",
            "Beauty, Food..",
            "Grocery"
    };

    String[] categoryImageArray = {
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/e00302d428f5c7be.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/5f2ee7f883cdb774.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/ff559cb9d803d424.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/af646c36d74c4be9.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/e90944802d996756.jpg?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/1788f177649e6991.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/3c647c2e0d937dc5.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/b3020c99672953b9.png?q=100",
            "https://rukminim2.flixcart.com/fk-p-flap/64/64/image/e730a834ad950bae.png?q=100"
    };

    ArrayList<CategoryList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_recycler);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        db = openOrCreateDatabase("GpgApp.db",MODE_PRIVATE,null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY AUTOINCREMENT,NAME VARCHAR(100),EMAIL VARCHAR(50),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(10),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTable = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYNAME VARCHAR(50),CATEGORYIMAGE VARCHAR(200))";
        db.execSQL(categoryTable);


        recyclerView = findViewById(R.id.category_recyclerview);

        //recyclerView.setLayoutManager(new LinearLayoutManager(CategoryRecyclerActivity.this));

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));

        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.HORIZONTAL));

        arrayList = new ArrayList<>();
        for(int i=0;i< categoryArray.length;i++){
            CategoryList list = new CategoryList();
            list.setId(idArray[i]);
            list.setName(categoryArray[i]);
            list.setImage(categoryImageArray[i]);
            arrayList.add(list);
        }


        for(int i=0; i<categoryArray.length; i++){
            String checkCategory = "SELECT * FROM CATEGORY WHERE CATEGORYNAME = '"+categoryArray[i]+"'";
            Cursor cursor= db.rawQuery(checkCategory, null);

            if(cursor.getCount()>0){
                // category already exists
            }
            else{
                String insertCategory = "INSERT INTO CATEGORY VALUES(NULL, '"+categoryArray[i]+"', '"+categoryImageArray[i]+"')";
                db.execSQL(insertCategory);
            }
        }


        CategoryRecyclerAdapter adapter = new CategoryRecyclerAdapter(CategoryRecyclerActivity.this,arrayList);
        recyclerView.setAdapter(adapter);
    }
}