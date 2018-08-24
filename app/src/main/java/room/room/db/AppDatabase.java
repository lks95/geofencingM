package room.room.db;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import room.room.db.typeconverters.DateTypeconverter;
import room.room.model.Projekt;

@Database(entities = {Projekt.class}, version = 2)
@TypeConverters({DateTypeconverter.class})

public abstract class AppDatabase extends RoomDatabase {
    public abstract ProjektDao getDao();
}

