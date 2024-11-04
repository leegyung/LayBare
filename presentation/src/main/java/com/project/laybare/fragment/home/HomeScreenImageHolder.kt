package com.project.laybare.fragment.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.project.domain.entity.ImageEntity
import com.project.laybare.R
import com.project.laybare.fragment.search.SearchEvent
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage
import kotlin.math.absoluteValue


@Composable
fun HomeBannerHolder(items : List<ImageEntity>) {
    val pagerState = rememberPagerState(pageCount = {
        items.size
    })

    HorizontalPager(
        modifier = Modifier
            .fillMaxWidth(),
        state = pagerState,
        contentPadding = PaddingValues(horizontal = 70.dp), // 현재 페이지 양옆 패딩
        pageSpacing = 8.dp, // 다음 페이지와의 간격
    ) { page ->
        val pageOffset = ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction)
        val image = items[page]
        HomeBannerImage(image.link, pageOffset)
    }

}




@Composable
fun HomeBannerImage(url : String, pageOffset : Float) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(520.dp)
            .graphicsLayer {
                alpha = lerp(
                    start = 0.7f,
                    stop = 1f,
                    fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                )

                lerp(
                    start = 1f,
                    stop = 0.90f,
                    fraction = pageOffset.absoluteValue.coerceIn(0f, 1f),
                ).let {
                    scaleX = it
                    scaleY = it
                }
            },
        color = colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(8.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .clickable {

                },
            imageModel = { url },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                requestSize = IntSize(500, 800)
            )
        )
    }
}


@Composable
fun HomeRegularImageHolder(imageList : List<ImageEntity>, keyword : String, viewIndex : Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
                .padding(20.dp),
            contentAlignment = Alignment.CenterStart

        ){

            Image(
                painter = painterResource(id = R.drawable.brush1),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .width(100.dp)
            )


            Text(
                text = keyword,
                style = TextStyle(
                    color = colorResource(id = R.color.textBlack),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.padding()
            )
        }




        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 20.dp)
        ) {
            items(imageList.count()) { i ->
                HomeRegularImage(imageList[i].link)
            }
        }




    }
}

@Composable
fun HomeRegularImage(url : String) {
    Surface(
        modifier = Modifier
            .width(150.dp)
            .height(250.dp),
        color = colorResource(id = R.color.gray_bg),
        shape = RoundedCornerShape(8.dp)
    ) {
        GlideImage(
            modifier = Modifier
                .fillMaxSize()
                .clickable {


                },
            imageModel = { url },
            imageOptions = ImageOptions(
                contentScale = ContentScale.Crop,
                alignment = Alignment.Center,
                requestSize = IntSize(500, 800)
            )
        )
    }
}






@Preview(showBackground = true)
@Composable
fun BannerImageTest() {
    val image = ImageEntity(
        "",
        "https://natureconservancy-h.assetsadobe.com/is/image/content/dam/tnc/nature/en/photos/w/o/WOPA160517_D056-resized.jpg?crop=864%2C0%2C1728%2C2304&wid=600&hei=800&scl=2.88",
        "",
        0,
        0,
        ""
    )


    val urls = arrayListOf(
        image,
        image,
        image,
        image,
        image
    )

    MaterialTheme {
        HomeRegularImageHolder(urls, "Test", 1)
    }

}