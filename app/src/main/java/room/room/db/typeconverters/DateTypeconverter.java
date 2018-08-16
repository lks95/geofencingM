package room.room.db.typeconverters;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class DateTypeconverter {

    @TypeConverter
    public long convertDateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time){
        return new Date(time);
    }
}
