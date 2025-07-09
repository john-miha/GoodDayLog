// プロダクトコードのクラスを直接importすることを想定
import com.example.gooddaylog.data.Diary
import com.example.gooddaylog.data.DiaryRepository
import com.example.gooddaylog.ui.home.HomeUiState
import com.example.gooddaylog.ui.home.HomeViewModel

import app.cash.turbine.test
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
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever

/**
 * HomeViewModel（CalendarViewModel）の単体テストクラス。
 *
 * @see HomeViewModel
 */
@ExperimentalCoroutinesApi
@ExtendWith(MockitoExtension::class)
class HomeViewModelTest {

    // テスト対象のViewModel
    private lateinit var viewModel: HomeViewModel

    // モック化するRepository
    @Mock
    private lateinit var diaryRepository: DiaryRepository

    // Coroutinesのメインディスパッチャをテスト用に置き換える
    private val testDispatcher = StandardTestDispatcher()
    
    // レビュー指摘に基づき、テストの再現性を保証するための固定タイムスタンプを定義
    private const val FAKE_TIMESTAMP = 1720494000000L // 2024-07-09T12:00:00Z

    @BeforeEach
    fun setUp() {
        // Mainディスパッチャをテスト用のものに設定
        Dispatchers.setMain(testDispatcher)
    }

    @AfterEach
    fun tearDown() {
        // テスト終了後にMainディスパッチャをリセット
        Dispatchers.resetMain()
    }

    @Nested
    @DisplayName("初期化処理のテスト")
    inner class InitializationTest {

        @Test
        @DisplayName("成功: リポジトリから日記リストを取得し、UiStateが更新されること")
        fun `GIVEN repository returns diaries WHEN viewModel is initialized THEN uiState contains diaries`() = runTest {
            // Arrange
            // レビュー指摘に基づき、System.currentTimeMillis()を固定値に変更
            val fakeDiaries = listOf(
                Diary(id = 1, content = "素晴らしい一日だった", date = FAKE_TIMESTAMP, createdAt = FAKE_TIMESTAMP),
                Diary(id = 2, content = "良いことがあった", date = FAKE_TIMESTAMP, createdAt = FAKE_TIMESTAMP)
            )
            whenever(diaryRepository.getDiaries()).thenReturn(flowOf(fakeDiaries))

            // Act
            viewModel = HomeViewModel(diaryRepository)

            // Assert
            viewModel.uiState.test {
                val state = awaitItem()
                assertEquals(fakeDiaries, state.diaries)
                assertTrue(!state.isLoading)
                assertEquals(null, state.error)
            }
        }

        @Test
        @DisplayName("正常系: 日記が一件もない場合、UiStateが空のリストを保持すること")
        fun `GIVEN repository returns empty list WHEN viewModel is initialized THEN uiState has empty list`() = runTest {
            // Arrange
            val emptyList = emptyList<Diary>()
            whenever(diaryRepository.getDiaries()).thenReturn(flowOf(emptyList))

            // Act
            viewModel = HomeViewModel(diaryRepository)

            // Assert
            viewModel.uiState.test {
                val state = awaitItem()
                assertTrue(state.diaries.isEmpty())
                assertTrue(!state.isLoading)
            }
        }

        @Test
        @DisplayName("異常系: リポジトリがエラーを返した場合、UiStateがエラー状態になること")
        fun `GIVEN repository throws error WHEN viewModel is initialized THEN uiState shows error`() = runTest {
            // Arrange
            val errorMessage = "Database error"
            whenever(diaryRepository.getDiaries()).thenReturn(flow { throw RuntimeException(errorMessage) })

            // Act
            viewModel = HomeViewModel(diaryRepository)

            // Assert
            viewModel.uiState.test {
                val state = awaitItem()
                assertNotNull(state.error)
                assertEquals(errorMessage, state.error)
                assertTrue(state.diaries.isEmpty())
                assertTrue(!state.isLoading)
            }
        }
    }

    // --- 他のViewModelのロジック（日付選択、FABクリックなど）に関するテストをここに追加 ---
}

// レビュー指摘に基づき、プロダクトコードのクラスを直接importして使用するため、以下の仮クラス定義は削除
