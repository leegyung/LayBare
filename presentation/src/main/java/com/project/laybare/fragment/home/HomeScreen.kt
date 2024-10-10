package com.project.laybare.fragment.home

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController


@Composable
fun HomeMainScreen(viewModel : HomeViewModel, navController: NavController) {

}

@Composable
fun HomeScreen() {

}


@Preview(showBackground = true)
@Composable
fun HomeScreenTest() {

    MaterialTheme {
        HomeScreen()
    }

}