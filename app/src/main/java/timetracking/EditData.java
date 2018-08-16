package timetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;

public class EditData extends AppCompatActivity {

    private static final String TAG = "EditData";

    private Button btnSave, btnDelte;
    private EditText edititem;

    DataBaseHelper mDatabasehelper;

    private String selectedName;
    private int selectedID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_data_layout);

        btnSave = (Button) findViewById(R.id.btnSave);
        btnDelte = (Button) findViewById(R.id.btnDelete);
        edititem = (EditText) findViewById(R.id.edittexitem);

        mDatabasehelper = new DataBaseHelper(this);

        //von listdata
        Intent receivedIntent = getIntent();
        //intents, id von den extras in listdata (darfnichtnull)
        selectedID = receivedIntent.getIntExtra("id", -1);
        //intents, name von den extras in listdata
        selectedName = receivedIntent.getStringExtra("name");

        edititem.setText(selectedName);


        btnSave.setOnClickListener(new View.OnClickListener() {

            //gleich wie adddata
            @Override
            public void onClick(View v) {
                String item = edititem.getText().toString();
                if(!item.equals("")){
                    mDatabasehelper.updateName(item, selectedID, selectedName);
                    toastMessage("Item has been updated.");
                } else {
                    toastMessage("You didnt enter anything!");
                }
            }
        });


        btnDelte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabasehelper.deleteName(selectedID, selectedName);
                edititem.setText("");
                toastMessage("Item has been removed from database");


            }
        });


    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}
