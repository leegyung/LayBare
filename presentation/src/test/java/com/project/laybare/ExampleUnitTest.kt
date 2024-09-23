package com.project.laybare

import com.project.laybare.fragment.similarImage.TodoListMutation
import com.project.laybare.fragment.similarImage.TodoListReducer
import com.project.laybare.fragment.similarImage.TodoListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val state = MutableStateFlow<TodoListUiState>(TodoListUiState.Empty)
        println(state.value.toString())
        state.update {
            TodoListReducer().invoke(TodoListMutation.ShowError("error"))
        }
        println(state.value.toString())
    }
}