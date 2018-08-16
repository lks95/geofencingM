package timetracking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;


public class Timetracking extends AppCompatActivity {


    private static final String TAG = "Timetracking";

    DataBaseHelper mDatabaseHelper;
    private Button btnAdd, btnViewData;
    private EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.eingabe_timetracking);

        editText = (EditText) findViewById(R.id.editText);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnViewData = (Button) findViewById(R.id.btnView);

        mDatabaseHelper = new DataBaseHelper(this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String clickclack = editText.getText().toString();

                if(editText.length() != 0){
                    AddData(clickclack);
                    editText.setText("");
                } else {
                    toastMessage("you need to type something in");
                }
            }
        });

        btnViewData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Timetracking.this, ListData.class);
                startActivity(intent);
            }
        });

    }


    private void toastMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void AddData(String clickclack) {

        boolean insertData = mDatabaseHelper.addData(clickclack);

        if (insertData){
           toastMessage("Data sucssfully inserted");
        } else {
            toastMessage("Wrong data filled in");
        }
    }




}
