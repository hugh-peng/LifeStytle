package cn.fengcrush.lifestyle.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {MarkBean.class},version =  1,exportSchema = false)
public abstract class MarkDatabase extends RoomDatabase {
    public abstract MarkDao getMarkDao();
    //使用单例模式
    private static MarkDatabase mInstance;

    public MarkDatabase() {
    }

    //强制运行在UI线程中，不推荐
    public static MarkDatabase getmInstance(Context context) {
        if(mInstance == null) {
            synchronized (MarkDatabase.class) {
                if(mInstance == null) {
                    mInstance = Room.databaseBuilder(context.getApplicationContext(),MarkDatabase.class,"mark_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return mInstance;
    }
}
