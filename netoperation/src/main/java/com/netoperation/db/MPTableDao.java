package com.netoperation.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Set;

import io.reactivex.Flowable;

@Dao
public interface MPTableDao {

    @Insert
    void insertMpTableData(MPTable mpTable);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateMPTable(MPTable mpTable);

    @Query("SELECT * from MPTable")
    MPTable getMPTable();

    @Query("SELECT readArticleIds from MPTable")
    Flowable<Set> getArticleIdsFlowable();

    @Query("SELECT mpBannerMsg from MPTable")
    String getMpBannerMsg();

    @Query("SELECT allowedArticleCounts from MPTable")
    int getAllowedArticleCounts();

    @Query("SELECT allowedTimeInSecs from MPTable")
    int getAllowedArticleTimesInSecs();

    @Query("SELECT readArticleIds from MPTable")
    Set getArticleIds();

//    @Query("UPDATE MPTable SET readArticleIds = :newReadArticleIds WHERE id = :id")
//    int updateArticleIds(Set<String> newReadArticleIds, int id);

}
