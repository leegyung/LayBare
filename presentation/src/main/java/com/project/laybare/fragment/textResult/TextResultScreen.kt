package com.project.laybare.fragment.textResult

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController

@Composable
fun TextResultMainScreen(viewModel : TextResultViewModel, navController: NavController) {
    val uiState by viewModel.container.stateFlow.collectAsState()
    val sideEffect = viewModel.container.sideEffectFlow

    LaunchedEffect(Unit) {
        sideEffect.collect{ sideEffect ->
            when(sideEffect){
                TextResultSideEffect.MoveToPreviousPage -> navController.popBackStack()
            }
        }
    }

    TextResultScreen(
        uiState = uiState,
        onSendEvent = { event ->
            viewModel.handelEvent(event)
        }
    )





}

@Composable
fun TextResultScreen(uiState : TextResultState, onSendEvent : (event : TextResultEvent) -> Unit) {

}



@Preview(showBackground = true)
@Composable
fun TextResultScreenPreview() {

    val state = TextResultState(
        "hello world",
        "hello world"
    )


    MaterialTheme {
        TextResultScreen(
            state,
            onSendEvent = {}
        )
    }
}