package com.netoperation.db;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

@Database(entities = {DashboardTable.class, BookmarkTable.class,
        BreifingTable.class, UserProfileTable.class, MPTable.class},
        version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class THPDB extends RoomDatabase {

    private static volatile THPDB INSTANCE;

    public abstract DashboardDao dashboardDao();
    public abstract BookmarkTableDao bookmarkTableDao();
    public abstract BreifingDao breifingDao();
    public abstract UserProfileDao userProfileDao();
    public abstract MPTableDao mpTableDao();

    public static THPDB getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (THPDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            THPDB.class, "THPDB.db")
                            .addMigrations(MIGRATION_1_2)
                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            // Since we didn't alter the table, there's nothing else to do here.
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN morningTime TEXT");
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN noonTime TEXT");
            database.execSQL("ALTER TABLE BreifingTable "
                    + " ADD COLUMN eveningTime TEXT");
        }
    };
}
