package timetracking;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;

import java.util.ArrayList;

public class ListData extends AppCompatActivity {

    private static final String TAG = "listdata";
    DataBaseHelper mDataBaseHelper;
    private ListView mListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_layout_timetracking);

        mListView = (ListView) findViewById(R.id.timetracking_listview);
        mDataBaseHelper = new DataBaseHelper(this);

        inhaltlistview();
    }

    private void inhaltlistview() {
        Log.d(TAG, "inhaltlistview: display data in listview");
        Cursor data = mDataBaseHelper.getData();

        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){ //wie iter.hasnext
            //werte von der database in zeile 1 und dann in arraylist hinzufuegen
            listData.add(data.getString(1));
        }



        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name =  adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "You clicked on: " +name);

                Cursor data = mDataBaseHelper.getItemId(name);

                int darfnichtnull = -1;

                while(data.moveToNext()){
                    darfnichtnull = data.getInt(0);
                }
                if(darfnichtnull > -1){
                    Log.d(TAG, "You clicked on" +darfnichtnull);
                    Intent editdataintent = new Intent(ListData.this, EditData.class);
                   editdataintent.putExtra("id", darfnichtnull);
                    editdataintent.putExtra("name", name);

                    startActivity(editdataintent);

                } else{
                    toastMsg("no data matches");
                }

            }
        });
    }

    private void toastMsg(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
