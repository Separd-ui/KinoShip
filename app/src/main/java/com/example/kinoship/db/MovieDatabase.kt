package com.example.kinoship.db

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kinoship.models.Result
import com.example.kinoship.utils.Constans

@Database(entities = arrayOf(Result::class),version = 2,exportSchema = false)
@TypeConverters(Converters::class)
abstract class MovieDatabase:RoomDatabase() {

    abstract fun getDao():MovieDao

    companion object{
        @Volatile
        private var INSTANCE:MovieDatabase?=null

        fun getDatabase(context: Context) = INSTANCE?: synchronized(this){
            val instance= Room.databaseBuilder(
                context,
                MovieDatabase::class.java,
                Constans.DATABASE_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE=instance
            instance
        }

    }
}

val migration_1_2 = object : Migration(1,2){
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE movie_list ADD COLUMN dateAdded Long"
        )
    }

}