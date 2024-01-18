package com.pollyannawu.gogolook.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.coil.rememberCoilPainter
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.dataclass.Hit



@Composable
fun SingleImageScreen(
    modifier: Modifier = Modifier,
    hit: Hit,
    isLinear: Boolean = true
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp)
    ) {

        val singleImage by remember {
            mutableStateOf(hit)
        }

        val mainImagePainter = rememberCoilPainter(
            request = singleImage.largeImageURL,
            fadeIn = true
        )


        Column {
            UserInfoComposable(singleImage = singleImage)
            MainImageComposable(mainImagePainter)
            ActionRowComposable(
                singleImage = singleImage,
                isLinear = isLinear
            )
        }
    }
}

@Composable
fun MainImageComposable(painter: Painter, modifier: Modifier = Modifier) {

    Image(
        painter = painter,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .background(MaterialTheme.colorScheme.surface)
            .paint(painterResource(id = R.drawable.gogolook))
            .fillMaxWidth()
            .aspectRatio(1f),
        contentDescription = "image content",
        contentScale = ContentScale.Crop

    )
}

@Composable
fun UserInfoComposable(
    singleImage: Hit,
    modifier: Modifier = Modifier
) {

    val userPainter = rememberCoilPainter(
        request = singleImage.userImageURL,
        fadeIn = true
    )
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = userPainter,
            modifier = Modifier
                .padding(all = 8.dp)
                .clip(CircleShape)
                .fillMaxWidth(.15f)
                .aspectRatio(1f)
                .paint(painterResource(id = R.drawable.gogolook))
                .background(MaterialTheme.colorScheme.surface),
            contentDescription = "user image",
            contentScale = ContentScale.Crop
        )
        Text(
            text = singleImage.user,
            modifier = Modifier
                .padding(start = 16.dp),
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Composable
fun ActionRowComposable(singleImage: Hit, isLinear: Boolean) {
    Row(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        if (!isLinear) {
            Spacer(modifier = Modifier.weight(1f)) // Spacer before the first icon
        }

        NumberIconComposable(number = singleImage.likes, iconId = R.drawable.like_icon)

        if (!isLinear) {
            Spacer(modifier = Modifier.weight(1f)) // Spacer between the icons
        } else {
            Spacer(modifier = Modifier.weight(1f)) // Spacer between the icons
            NumberIconComposable(number = singleImage.downloads, iconId = R.drawable.download_icon)
            Spacer(modifier = Modifier.weight(1f)) // Spacer between the icons
        }

        NumberIconComposable(number = singleImage.comments, iconId = R.drawable.comment_icon)

        if (!isLinear) {
            Spacer(modifier = Modifier.weight(1f)) // Spacer after the last icon
        } else {
            Spacer(modifier = Modifier.weight(1f))
            NumberIconComposable(number = singleImage.views, iconId = R.drawable.view_icon)
        }
    }
}


@Composable
fun NumberIconComposable(modifier: Modifier = Modifier, number: Int, iconId: Int) {

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numberText = getNumberText(number)
        Text(
            text = numberText,
            modifier = Modifier
                .wrapContentWidth()
                .padding(top = 8.dp),
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "icon",
            modifier = Modifier
                .fillMaxSize(0.1f)
                .padding(top = 8.dp)
        )
    }

}

fun getNumberText(number: Int): String {
    return if (number in 1000..999999) {
        val quotient = number / 1000
        val reminders = (number % 1000) / 100
        "${quotient}.${reminders}k"
    } else if (number > 999999) {
        val quotient = number / 1000000
        val reminders = (number % 1000000) / 100000
        "${quotient}.${reminders}m"
    } else {
        number.toString()
    }
}


@Preview
@Composable
fun previewLinearImageScreen() {

    val fakeHit: Hit = Hit(
        id = 1,
        pageURL = "https://example.com/page/1",
        type = "photo",
        tags = "nature, outdoors",
        previewURL = "https://example.com/images/preview/1.jpg",
        previewWidth = 320,
        previewHeight = 240,
        webformatURL = "https://example.com/images/webformat/1.jpg",
        webformatWidth = 1024,
        webformatHeight = 768,
        largeImageURL = "https://example.com/images/large/1.jpg",
        imageWidth = 2048,
        imageHeight = 1536,
        imageSize = 1024 * 1024,
        views = 1000,
        downloads = 500,
        collections = 50,
        likes = 300,
        comments = 100,
        userId = 101,
        user = "John Doe",
        userImageURL = "https://example.com/users/johndoe.jpg"
    )

    SingleImageScreen(hit = fakeHit, isLinear = true)
}
