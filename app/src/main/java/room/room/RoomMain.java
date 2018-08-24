package room.room;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adesso.lklein.geofencing.R;

import java.util.ArrayList;

import room.room.db.AppDatabase;
import room.room.db.ProjektDao;
import room.room.model.Projekt;

public class RoomMain extends AppCompatActivity {

    private static final int CREATE_PROJEKT = 1;
    private static final int UPDATE_PROJEKT = 2;

    private RecyclerView mProjektRecylcerView;
    private ProjektRecyclerAdapter mProjektRecylcerAdapter;
    private ProjektDao mProjektDao;

    private FloatingActionButton mAddProjektFloatingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.room_main);

        mProjektDao = Room.databaseBuilder(this, AppDatabase.class, "db-projekte" )
                .fallbackToDestructiveMigration()
                .allowMainThreadQueries()
                .build()
                .getDao();

        mProjektRecylcerView = findViewById(R.id.projektsRecyclerView);
        mProjektRecylcerView.setLayoutManager(new LinearLayoutManager(this));
        mAddProjektFloatingButton = findViewById(R.id.addfloatingbutton);

        int colors[] = {ContextCompat.getColor(this, R.color.colorAccent),
                ContextCompat.getColor(this, android.R.color.holo_red_light),
                ContextCompat.getColor(this, android.R.color.holo_orange_light),
                ContextCompat.getColor(this, android.R.color.holo_green_light),
                ContextCompat.getColor(this, android.R.color.holo_blue_dark),
                ContextCompat.getColor(this, android.R.color.holo_purple)};

        mProjektRecylcerAdapter = new ProjektRecyclerAdapter(this, new ArrayList<Projekt>(), colors);

        mProjektRecylcerAdapter.addActionCallback(new ProjektRecyclerAdapter.ActionCallback() {
            @Override
            public void onLongClickListener(Projekt projekt) {
                Intent intent = new Intent(RoomMain.this, CreateProjektActivity.class );
                intent.putExtra(UpdateProjektActivity.EXTRA_PROJEKT_ID, projekt.getProjektname());
                startActivityForResult(intent, CREATE_PROJEKT);
            }
        });

        mProjektRecylcerView.setAdapter(mProjektRecylcerAdapter);

        mAddProjektFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(RoomMain.this, CreateProjektActivity.class);
                startActivityForResult(intent, CREATE_PROJEKT);

            }
        });

        loadProjekte();
    }

    private void loadProjekte(){
        Toast.makeText(this, "Items stored: " +mProjektDao.getProjekte().size(), Toast.LENGTH_LONG);
        mProjektRecylcerAdapter.updateData(mProjektDao.getProjekte());

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_PROJEKT && resultCode == RESULT_OK) {
            loadProjekte();
        } else if (requestCode == UPDATE_PROJEKT && resultCode == RESULT_OK){
            loadProjekte();
        }
    }


    @Override
    protected void onResume() {

        super.onResume();
        loadProjekte();
    }
}
