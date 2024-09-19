package com.project.laybare.fragment.search

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.fragment.similarImage.SimilarImageView
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun SearchScreen(viewModel : SearchViewModel, onSearchClicked : () -> Unit) {
    val imagePagingItems: LazyPagingItems<ImageEntity> = viewModel.mImageListState.collectAsLazyPagingItems()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp, 20.dp, 20.dp, 20.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.back_arrow),
                contentDescription = null,
                Modifier
                    .size(35.dp)
                    .clickable {

                    }
            )

            Spacer(modifier = Modifier.width(20.dp))
            SearchBox(text = viewModel.mKeywordState, onSearchClicked)
        }


        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            Modifier.padding(20.dp, 0.dp, 20.dp, 0.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp), // 열 사이의 간격 설정
            verticalArrangement = Arrangement.spacedBy(10.dp) // 행 사이의 간격 설정
        ) {
            items(imagePagingItems.itemCount) { index ->
                imagePagingItems[index]?.let{ image ->
                    SearchImageView(image)
                }
            }
        }

    }
}

@Composable
fun SearchBox(text : MutableState<String>, onSearchClicked : () -> Unit){
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BasicTextField(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        keyboardActions =  KeyboardActions(onSearch = {
            onSearchClicked()
            focusManager.clearFocus()
            keyboardController?.hide()
        }),
        singleLine = true,
        value = text.value,
        textStyle = TextStyle(
            color = colorResource(id = R.color.textBlack),
            fontSize = 14.sp
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .background(
                        color = colorResource(id = R.color.gray_bg),
                        shape = RoundedCornerShape(size = 20.dp)
                    )
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 48.dp, 0.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                innerTextField()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp, 20.dp, 0.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(text.value.isNotEmpty()){
                    Icon(
                        modifier = Modifier
                            .size(20.dp)
                            .clickable {
                                text.value = ""
                            },
                        painter = painterResource(id = R.drawable.remove_icon),
                        tint = colorResource(id = R.color.gray50),
                        contentDescription = null
                    )
                }
            }
        },
        onValueChange = {
            text.value = it
        }
    )
}

@Composable
fun SearchImageView(item : ImageEntity) {
    Column(
        Modifier.height(350.dp)
    ) {
        Surface(
            color = colorResource(id = R.color.gray_bg),
            shape = RoundedCornerShape(8.dp),
        ) {
            GlideImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageModel = { item.link },
                imageOptions = ImageOptions(
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center,
                    requestSize = IntSize(400, 800)
                )
            )
        }
    }

}









@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun SearchScreenPreview() {

    val list = arrayListOf(
        "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg",
        "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg",
        "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg",
        "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg",
        "https://cdn.travie.com/news/photo/first/201710/img_19975_1.jpg",
    )

    MaterialTheme {

    }
}

