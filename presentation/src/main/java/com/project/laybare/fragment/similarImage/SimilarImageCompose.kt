package com.project.laybare.fragment.similarImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.domain.repository.api.SearchImageRepository
import com.project.domain.usecase.SearchImageUseCase
import com.project.laybare.R

@Composable
fun SimilarImageCompose(
    item : ArrayList<String>,
    onBackClicked: (index : Int) -> Unit,
    onImageClicked: (url : Int) -> Unit,
    viewModel : SimilarImageViewModel
) {


    val keywords = viewModel.mKeywordList
    val imageList = viewModel.mImageList

    Column(
        Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(20.dp, 0.dp, 20.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                Modifier.size(35.dp)
            )

            Image(
                painter = painterResource(id = R.drawable.lay_bare),
                contentDescription = null,
                Modifier.size(54.dp, 26.dp)
            )
        }


        Spacer(modifier = Modifier.size(10.dp))


        LazyRow (
            Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)

        ){
            itemsIndexed(keywords){ index, item ->
                KeywordBox(item)
            }
        }

        Spacer(modifier = Modifier.size(20.dp))


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp), // 열 사이의 간격 설정
            verticalArrangement = Arrangement.spacedBy(10.dp) // 행 사이의 간격 설정
        ) {
            itemsIndexed(imageList) { index, image ->
                SimilarImageView(onImageClicked, index, image)
            }
        }


    }

}


@Composable
fun KeywordBox(keyword : String, modifier: Modifier = Modifier){
    Surface(
        color = colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(8.dp), // 모서리 둥글게
    ) {
        Text(
            text = keyword,
            style = TextStyle(
                color = colorResource(id = R.color.black),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = modifier.padding(10.dp, 4.dp, 10.dp, 4.dp)
        )
    }
}



/*
@Preview(showBackground = true)
@Composable
fun ItemListPreview() {
    // Sample data for the preview
    val items = arrayListOf("Item 1", "Item 2", "Item 3", "Item 4", "Item 5")


    // Apply the theme and display the ItemList composable
    MaterialTheme {
        SimilarImageCompose(
            items,
            onBackClicked = { index ->
                // 클릭된 항목의 인덱스를 처리
                println("Item clicked at index: $index")
            },
            onImageClicked = { index ->
                // 클릭된 항목의 인덱스를 처리
                println("Item clicked at index: $index")
            },
            SimilarImageViewModel(SearchImageUseCase())
        )
    }
}

 */