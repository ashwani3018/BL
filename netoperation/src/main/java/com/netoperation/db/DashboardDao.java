package com.netoperation.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.netoperation.model.RecoBean;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.ABORT;

@Dao
public interface DashboardDao {

//    @Insert(onConflict = ABORT)
    @Insert
    void insertDashboard(DashboardTable HomeTable);

    @Query("SELECT * FROM DashboardTable WHERE recoFrom = :recoFrom")
    List<DashboardTable> getAllDashboardBean(String recoFrom);

    @Query("SELECT * FROM DashboardTable WHERE aid = :aid")
    DashboardTable getSingleDashboardBean(String aid);

    @Query("DELETE FROM DashboardTable")
    void deleteAll();

    @Query("DELETE FROM DashboardTable WHERE recoFrom = :recoFrom")
    void deleteAll(String recoFrom);

    @Query("UPDATE DashboardTable SET bean = :bean WHERE aid = :aid")
    int updateRecobean(String aid, RecoBean bean);


}
