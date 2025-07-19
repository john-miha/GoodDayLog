package com.example.gooddaylog.ui.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * アプリケーション内の画面ルートを定義する Sealed Class。
 * ルート文字列のタイプミスを防ぎ、引数の管理を容易にする。
 */
sealed class Screen(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    /**
     * カレンダー画面
     */
    object Calendar : Screen("calendar")

    /**
     * 日記入力画面
     */
    object Input : Screen(
        route = "input/{diaryId}",
        navArguments = listOf(navArgument("diaryId") {
            type = NavType.LongType
        })
    ) {
        /**
         * 日記入力画面へのルートを生成する。
         * @param diaryId 編集対象の日記ID。新規作成の場合は 0L を渡す。
         */
        fun createRoute(diaryId: Long): String = "input/$diaryId"
    }

    /**
     * 設定画面
     */
    object Settings : Screen("settings")
}
