package com.example.gooddaylog.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gooddaylog.data.db.dao.DiaryDao
import com.example.gooddaylog.data.db.entity.Diary

@Database(entities = [Diary::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryDao(): DiaryDao

    companion object {
        // @Volatileアノテーションで、この変数が常に最新の値であることが保証される
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            // INSTANCEがnullでなければそれを返し、nullなら同期的にデータベースを構築する
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "good_day_log_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}