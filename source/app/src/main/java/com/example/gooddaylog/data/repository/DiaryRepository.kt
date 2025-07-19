package com.example.gooddaylog.data.repository

import com.example.gooddaylog.data.db.entity.Diary
import kotlinx.coroutines.flow.Flow

// Repositoryのインターフェース（設計図）
interface DiaryRepository {
    fun getAllDiaries(): Flow<List<Diary>>
    suspend fun addDiary(diary: Diary)
}