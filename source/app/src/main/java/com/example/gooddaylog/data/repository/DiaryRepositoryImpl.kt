package com.example.gooddaylog.data.repository

import com.example.gooddaylog.data.db.dao.DiaryDao
import com.example.gooddaylog.data.db.entity.Diary
import kotlinx.coroutines.flow.Flow

// Repositoryの実装クラス
class DiaryRepositoryImpl(
    private val diaryDao: DiaryDao // DAOをコンストラクタで受け取る
) : DiaryRepository {

    override fun getAllDiaries(): Flow<List<Diary>> {
        return diaryDao.getAllDiaries()
    }

    override suspend fun addDiary(diary: Diary) {
        diaryDao.insertDiary(diary)
    }
}