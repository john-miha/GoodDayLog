package com.example.gooddaylog.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gooddaylog.data.db.entity.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    // 新しい日記を挿入する。競合した場合は置き換える。
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: Diary)

    // 全ての日記を作成日時の降順（新しいものが上）で取得し、Flowで監視する
    @Query("SELECT * FROM diaries ORDER BY createdAt DESC")
    fun getAllDiaries(): Flow<List<Diary>>
}