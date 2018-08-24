package room.room;

import android.arch.persistence.room.Room;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;

import java.util.Date;

import room.room.db.AppDatabase;
import room.room.db.ProjektDao;
import room.room.model.Projekt;

public class CreateProjektActivity extends AppCompatActivity {

    private EditText mProjektEditText;
    private EditText mArbeiterEditText;
    private EditText mStandort;

    private Button mSaveButton;
    private ProjektDao mRoomDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_create_activity);

        mRoomDao = Room.databaseBuilder(this, AppDatabase.class, "db-projekt")
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                .getDao();

        mProjektEditText = findViewById(R.id.ProjektNameEdit);
        mArbeiterEditText = findViewById(R.id.nameedit);
        mStandort = findViewById(R.id.standortedit);
        mSaveButton = findViewById(R.id.saveButton);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String projektname = mProjektEditText.getText().toString();
                String name = mArbeiterEditText.getText().toString();
                String standort = mStandort.getText().toString();

                if(projektname.length() == 0 || name.length() == 0 || standort.length() == 0 ){
                    Toast.makeText(CreateProjektActivity.this, "Seems like something is missing", Toast.LENGTH_SHORT).show();
                }


                Projekt projekt = new Projekt();

                projekt.setProjektname(projektname);
                projekt.setName(name);
                projekt.setStandort(standort);
                projekt.setCreatedDate(new Date());

                try {
                    mRoomDao.insert(projekt);
                    setResult(RESULT_OK);
                    finish();
                } catch (SQLiteConstraintException e) {
                    Toast.makeText(CreateProjektActivity.this, "test", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }




}
