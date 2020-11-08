package cn.fengcrush.lifestyle.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MarkDao {
    @Insert
    void insertMarks(MarkBean markBean);

    @Delete
    void deleteMarks(MarkBean markBean);

    @Query("SELECT * FROM MARKBEAN ORDER BY ID DESC")
    List<MarkBean> getAllMarks();
}
