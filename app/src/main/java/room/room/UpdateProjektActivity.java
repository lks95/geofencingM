package room.room;

import android.arch.persistence.room.Room;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;

import room.room.db.AppDatabase;
import room.room.db.ProjektDao;
import room.room.model.Projekt;

public class UpdateProjektActivity extends AppCompatActivity {

    public static String EXTRA_PROJEKT_ID = "projekt_id";

    private TextView mCreatedTimeTextView;

    private EditText mProjektEditText;
    private EditText mNameEditText;
    private EditText mStandortEditText;

    private Button mUpdateButton;
    private Toolbar mToolbar;
    private ProjektDao mDao;
    private Projekt PROJEKT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_update_activity);

        mDao = Room.databaseBuilder(this, AppDatabase.class, "db-projekte")
                .allowMainThreadQueries()
                .build()
                .getDao();

        mProjektEditText = findViewById(R.id.projektupdate);
        mNameEditText = findViewById(R.id.nameupdate);
        mStandortEditText = findViewById(R.id.standortupdate);

        mUpdateButton = findViewById(R.id.updateButton);
        mCreatedTimeTextView = findViewById(R.id.createdTimeTextView);
       /* mToolbar = findViewById(R.id.toolbar);*/

        PROJEKT = mDao.getContactWithId(getIntent().getStringExtra(EXTRA_PROJEKT_ID));


        //class not created yet
        initViews();

    }

    private void initViews(){
        setSupportActionBar(mToolbar);

        mProjektEditText.setText(PROJEKT.getProjektname());
        mNameEditText.setText(PROJEKT.getName());
        mStandortEditText.setText(PROJEKT.getStandort());
        mCreatedTimeTextView.setText(PROJEKT.getCreatedDate().toString());

        mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String projektname = mProjektEditText.getText().toString();
                String name = mNameEditText.getText().toString();
                String standort = mStandortEditText.getText().toString();

                if(projektname.length() == 0 || name.length() == 0 || standort.length() == 0){
                    Toast.makeText(UpdateProjektActivity.this, "Be sure all content is correct!", Toast.LENGTH_SHORT).show();
                    return;
                }

                PROJEKT.setProjektname(projektname);
                PROJEKT.setName(name);
                PROJEKT.setStandort(standort);

                mDao.update(PROJEKT);
                setResult(RESULT_OK);
                finish();

            }
        });

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu_update);
    }*/

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case R.id.delete: {
                mDao.delete(PROJEKT);
                setResult(RESULT_OK);
                finish();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }





}
