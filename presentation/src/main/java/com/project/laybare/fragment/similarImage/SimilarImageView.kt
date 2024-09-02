package com.project.laybare.fragment.similarImage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SimilarImageView(onItemClicked: (index : Int) -> Unit, index : Int, data : ImageEntity){

    val url = "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg"

    Column(
        Modifier.height(350.dp)
    ) {
        Surface(
            color = colorResource(id = R.color.gray_bg),
            shape = RoundedCornerShape(8.dp), // 모서리 둥글게
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { onItemClicked(index) },
                imageModel = { data.link },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
            )
        }
    }
}


/*
@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    // Apply the theme and display the ItemList composable
    MaterialTheme {
        SimilarImageView(onItemClicked = { index ->
            // 클릭된 항목의 인덱스를 처리
            println("Item clicked at index: $index")
        }, 1)
    }
}

 */