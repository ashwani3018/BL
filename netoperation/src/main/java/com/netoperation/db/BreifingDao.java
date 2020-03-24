package com.netoperation.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface BreifingDao {

    @Insert
    void insertBreifing(BreifingTable breifingTable);

    @Query("DELETE FROM BreifingTable")
    void deleteAll();

    @Query("SELECT * FROM BreifingTable")
    BreifingTable getBreifingTable();


}
