package room.room.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import room.room.model.Projekt;

@Dao
public interface ProjektDao {

    @Insert
    public void insert(Projekt... projekts);

    @Update
    public void update(Projekt... projekts);

    @Delete
    public void delete(Projekt... projekts);

    @Query("SELECT * FROM projekt")
    public List<Projekt> getProjekte();

    @Query("SELECT * FROM projekt WHERE standort = :text")
    public Projekt getContactWithId(String text);

}
