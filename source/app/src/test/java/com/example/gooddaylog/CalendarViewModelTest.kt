package com.example.gooddaylog

import app.cash.turbine.test
import com.example.gooddaylog.data.db.entity.Diary
import com.example.gooddaylog.data.repository.DiaryRepository
import com.example.gooddaylog.ui.screen.calendar.CalendarUiState
import com.example.gooddaylog.ui.screen.calendar.CalendarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class CalendarViewModelTest {

    // テスト対象のViewModel
    private lateinit var viewModel: CalendarViewModel

    // モック化するRepository
    @Mock
    private lateinit var diaryRepository: DiaryRepository

    // Coroutinesのメインディスパッチャをテスト用に置き換える
    private val testDispatcher = StandardTestDispatcher()

    // テストの再現性を保証するための固定タイムスタンプ
    private const val FAKE_TIMESTAMP = 1721369600000L // 2025-07-19T15:13:20Z

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("初期化処理(init)のテスト")
    inner class InitializationTest {

        @Test
        @DisplayName("成功: リポジトリから日記リストを取得し、UiStateが更新されること")
        fun `GIVEN repository returns diaries WHEN viewModel is initialized THEN uiState contains diaries`() = runTest {
            // Arrange
            val fakeDiaries = listOf(
                Diary(id = 1, content = "素晴らしい一日だった", createdAt = FAKE_TIMESTAMP),
                Diary(id = 2, content = "良いことがあった", createdAt = FAKE_TIMESTAMP + 1)
            )
            whenever(diaryRepository.getAllDiaries()).thenReturn(flowOf(fakeDiaries))

            // Act
            viewModel = CalendarViewModel(diaryRepository)

            // Assert
            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(fakeDiaries, state.diaries)
            }
        }

        @Test
        @DisplayName("正常系: 日記が一件もない場合、UiStateが空のリストを保持すること")
        fun `GIVEN repository returns empty list WHEN viewModel is initialized THEN uiState has empty list`() = runTest {
            // Arrange
            val emptyList = emptyList<Diary>()
            whenever(diaryRepository.getAllDiaries()).thenReturn(flowOf(emptyList))

            // Act
            viewModel = CalendarViewModel(diaryRepository)

            // Assert
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.diaries.isEmpty())
            }
        }

        @Test
        @DisplayName("異常系: リポジトリがエラーを返した場合、UiStateが空のリストを保持すること")
        fun `GIVEN repository throws error WHEN viewModel is initialized THEN uiState has empty list`() = runTest {
            // Arrange
            whenever(diaryRepository.getAllDiaries()).thenReturn(flow { throw RuntimeException("DB Error") })

            // Act
            viewModel = CalendarViewModel(diaryRepository)

            // Assert
            // エラーが発生してもクラッシュせず、初期状態（空リスト）を維持することを期待
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.diaries.isEmpty())
            }
        }
    }

    @Nested
    @DisplayName("addDummyDiaryメソッドのテスト")
    inner class AddDummyDiaryTest {

        @Test
        @DisplayName("成功: addDummyDiaryが呼ばれたら、RepositoryのaddDiaryが呼ばれること")
        fun `WHEN addDummyDiary is called THEN repository's addDiary is called`() = runTest {
            // Arrange
            // 初期化時にDBを監視するため、空のFlowを返すように設定しておく
            whenever(diaryRepository.getAllDiaries()).thenReturn(flowOf(emptyList()))
            viewModel = CalendarViewModel(diaryRepository)

            // Act
            viewModel.addDummyDiary()

            // Assert
            // TODO: diaryRepository.addDiary が、引数 content="今日も良い日だった！" で呼ばれたことを verify を使って検証する
            // verify(diaryRepository).addDiary(???)
        }
    }
}
