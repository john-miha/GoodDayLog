package com.example.gooddaylog.ui.screen.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.gooddaylog.data.db.entity.Diary
import com.example.gooddaylog.data.repository.DiaryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// UIの状態を表すデータクラス
data class CalendarUiState(
    val diaries: List<Diary> = emptyList()
)

class CalendarViewModel(
    private val diaryRepository: DiaryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState = _uiState.asStateFlow()

    init {
        // ViewModelが作成されたら、DBからの日記リストの監視を開始する
        viewModelScope.launch {
            diaryRepository.getAllDiaries().collect { diaries ->
                _uiState.value = CalendarUiState(diaries = diaries)
            }
        }
    }

    // 新しいダミーの日記を追加する
    fun addDummyDiary() {
        viewModelScope.launch {
            val newDiary = Diary(content = "今日も良い日だった！")
            diaryRepository.addDiary(newDiary)
        }
    }
}

// ViewModelにRepositoryを注入するためのFactoryクラス
class CalendarViewModelFactory(
    private val repository: DiaryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}