package com.example.gooddaylog.di

import android.content.Context
import com.example.gooddaylog.data.db.AppDatabase
import com.example.gooddaylog.data.repository.DiaryRepository
import com.example.gooddaylog.data.repository.DiaryRepositoryImpl

// アプリ全体の依存性を管理するオブジェクト
object AppModule {
    fun provideDiaryRepository(context: Context): DiaryRepository {
        val database = AppDatabase.getInstance(context)
        return DiaryRepositoryImpl(database.diaryDao())
    }
}