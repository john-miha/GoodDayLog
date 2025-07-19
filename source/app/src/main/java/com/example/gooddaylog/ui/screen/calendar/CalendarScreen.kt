package com.example.gooddaylog.ui.screen.calendar

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.gooddaylog.data.db.entity.Diary
import com.example.gooddaylog.di.AppModule

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    navController: NavController,
) {
    // DIコンテナからRepositoryを取得し、ViewModelFactory経由でViewModelを生成
    val context = LocalContext.current
    val diaryRepository = AppModule.provideDiaryRepository(context)
    val viewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModelFactory(diaryRepository)
    )

    // ViewModelのuiStateを監視
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Good Day Log") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // FABをタップしたらViewModelのメソッドを呼ぶ
                viewModel.addDummyDiary()
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Add Diary")
            }
        }
    ) { innerPadding ->
        // 日記リストを表示
        DiaryList(
            modifier = Modifier.padding(innerPadding),
            diaries = uiState.diaries
        )
    }
}

@Composable
fun DiaryList(modifier: Modifier = Modifier, diaries: List<Diary>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(diaries) { diary ->
            DiaryCard(diary)
        }
    }
}

@Composable
fun DiaryCard(diary: Diary) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = diary.content)
            Text(text = "ID: ${diary.id}, CreatedAt: ${diary.createdAt}")
        }
    }
}