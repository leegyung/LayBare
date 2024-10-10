package com.project.laybare.fragment.textResult

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.project.laybare.R

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

    Column(
        Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Absolute.SpaceBetween
        ) {
            Image(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .clickable { onSendEvent(TextResultEvent.MoveToPreviousPage) },
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null
            )

            Image(
                modifier = Modifier
                    .height(30.dp)
                    .width(30.dp)
                    .clickable { onSendEvent(TextResultEvent.RollBackText) },
                painter = painterResource(id = R.drawable.reset_icon),
                contentDescription = null
            )
        }


        TextBox(
            uiState.modifiedText,
            onSendEvent
        )

    }
}

@Composable
fun TextBox(text : String, onSendEvent : (event : TextResultEvent) -> Unit) {

    val scrollState = rememberScrollState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp, 20.dp, 20.dp),
        color = colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(15.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                value = text,
                textStyle = TextStyle(
                    fontSize = 14.sp, // 글자 크기 설정
                    lineHeight = 20.sp // 줄 간격 설정
                ),
                singleLine = false,
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                keyboardActions =  KeyboardActions(onDone = {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }),
                onValueChange = {
                    onSendEvent(TextResultEvent.OnTextChanged(it))
                }
            )

        }
    }


}



@Preview(showBackground = true)
@Composable
fun TextResultScreenPreview() {

    val state = TextResultState(
        "hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world ",
        "hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world " +
                "hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world hello world"
    )


    MaterialTheme {
        TextResultScreen(
            state,
            onSendEvent = {}
        )
    }
}