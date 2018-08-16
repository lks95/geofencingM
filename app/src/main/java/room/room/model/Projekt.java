package room.room.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "projekt")
public class Projekt {

    private String standort;
    private String name;

    @PrimaryKey
    @NonNull

    private String projektname;
    private Date createdDate;

    public String getStandort() {
        return standort;
    }

    public void setStandort(String location) {
        this.standort = standort;
    }

    public String getName() {
        return name;
    }

    public void setName(String workername) {
        this.name = name;
    }

    @NonNull
    public String getProjektname() {
        return projektname;
    }

    public void setProjektname(@NonNull String projektname) {
        this.projektname = projektname;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
