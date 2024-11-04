package com.project.laybare.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.project.laybare.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageSelectOptionDialog(
    onAlbumSelected: () -> Unit,
    onCameraSelected: () -> Unit,
    onCancel: () -> Unit)
{
    val bottomSheetState = rememberModalBottomSheetState()


    ModalBottomSheet(
        onDismissRequest = onCancel,
        sheetState = bottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
        containerColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .clickable {
                        onAlbumSelected()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.album_image),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )

                Text(
                    text = "앨범",
                    style = TextStyle(
                        color = colorResource(id = R.color.textBlack),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                )


            }

            Column(
                modifier = Modifier
                    .width(IntrinsicSize.Min)
                    .height(IntrinsicSize.Min)
                    .clickable {
                        onCameraSelected()
                    },
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.camera_image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(10.dp)
                )

                Text(
                    text = "사진찍기",
                    style = TextStyle(
                        color = colorResource(id = R.color.textBlack),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }


        }
        
        Spacer(modifier = Modifier.height(30.dp))
    }



}

