package com.project.laybare.fragment.similarImage

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.domain.entity.ImageEntity
import com.project.domain.entity.ImageLabelEntity
import com.project.laybare.R
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun SimilarImageCompose(
    viewModel : SimilarImageViewModel,
    onBackClicked: () -> Unit,
    onKeywordClicked: (index : Int) -> Unit,
    onImageClicked: (url : Int) -> Unit
) {
    val isLoading by viewModel.mLoadingState


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
                Modifier
                    .size(35.dp)
                    .clickable {
                        onBackClicked()
                    }
            )

            Image(
                painter = painterResource(id = R.drawable.lay_bare),
                contentDescription = null,
                Modifier.size(54.dp, 26.dp)
            )
        }


        Spacer(modifier = Modifier.size(10.dp))

        KeywordList(viewModel, onKeywordClicked)

        Spacer(modifier = Modifier.size(20.dp))

        ImageList(viewModel, onImageClicked)


    }

    ProgressBar(isLoading)

}


@Composable
fun KeywordList(viewModel : SimilarImageViewModel, onKeywordClicked: (index : Int) -> Unit) {
    LazyRow (
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ){
        itemsIndexed(viewModel.mKeywordList){ index, item ->
            if(index == 0) {
                Spacer(modifier = Modifier.size(20.dp))
            }

            KeywordBox(item,
                Modifier.clickable {
                    onKeywordClicked(index)
                }
            )

            if(index == viewModel.mKeywordList.size - 1){
                Spacer(modifier = Modifier.size(20.dp))
            }

        }
    }
}



@Composable
fun KeywordBox(keyword : ImageLabelEntity, modifier: Modifier = Modifier){
    Surface(
        color = if(keyword.isSelected) colorResource(id = R.color.black) else colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(15.dp), // 모서리 둥글게
    ) {
        Text(
            text = keyword.label,
            style = TextStyle(
                color = if(keyword.isSelected) colorResource(id = R.color.white) else colorResource(id = R.color.textBlack),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
            ),
            modifier = modifier.padding(15.dp, 5.dp, 15.dp, 5.dp)
        )
    }
}


@Composable
fun ImageList(viewModel : SimilarImageViewModel, onImageClicked: (url : Int) -> Unit) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp), // 열 사이의 간격 설정
        verticalArrangement = Arrangement.spacedBy(10.dp) // 행 사이의 간격 설정
    ) {
        itemsIndexed(viewModel.mImageList) { index, image ->
            SimilarImageView(onImageClicked, index, image)
        }
    }
}

@Composable
fun SimilarImageView(onItemClicked: (index : Int) -> Unit, index : Int, data : ImageEntity){
    // 기본 이미지 url 로딩이 실패하면
    // 썸네일 이미지로 url로 바꿔서 다시 로드하라고 알려주기 위한 변수
    // *계속 값을 유지 하지 않고 재활용되면 값을 다시 설정
    var imageUrl by remember { mutableStateOf( if (data.linkError) data.thumbnailLink else data.link) }

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
                imageModel = { imageUrl },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    requestSize = IntSize(400, 800)
                ),
                loading = {

                },
                failure = {
                    // 썸네일로 한번도 로딩을 시도하지 않았다면
                    if(!data.linkError) {
                        data.linkError = true
                        imageUrl = data.thumbnailLink
                    }
                }
            )
        }
    }
}


@Composable
fun ProgressBar(isLoading : Boolean) {
    if(isLoading){
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                color = colorResource(id = R.color.gray50), // 프로그래스 바 색상
                strokeWidth = 4.dp, // 두께
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}





/*
@Preview(showBackground = true)
@Composable
fun ItemListPreview() {

    MaterialTheme {
        SimilarImageCompose(
            mViewModel,
            onBackClicked = { index ->
                // 클릭된 항목의 인덱스를 처리
                println("Item clicked at index: $index")
            },
            onImageClicked = { index ->
                // 클릭된 항목의 인덱스를 처리
                println("Item clicked at index: $index")
            }
        )
    }
}

 */




