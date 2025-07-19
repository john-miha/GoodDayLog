package com.example.gooddaylog.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gooddaylog.ui.screen.calendar.CalendarScreen
import com.example.gooddaylog.ui.screen.input.InputScreen

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    // NavHostが画面遷移のコントローラーとなる
    NavHost(
        navController = navController,
        startDestination = Screen.Calendar.route // 最初に表示する画面を指定
    ) {
        // カレンダー画面へのルート
        composable(route = Screen.Calendar.route) {
            CalendarScreen(navController = navController)
        }

        // 入力画面へのルート
        composable(
            route = Screen.Input.route,
            arguments = Screen.Input.navArguments // Screenで定義した引数をここで登録
        ) { backStackEntry ->
            // ナビゲーションから引数を安全に取得
            val diaryId = backStackEntry.arguments?.getLong("diaryId") ?: 0L
            InputScreen(
                navController = navController,
                diaryId = diaryId
            )
        }

        // TODO: 設定画面など、他の画面へのルートもここに追加していく
        // composable(route = Screen.Settings.route) { ... }
    }
}
