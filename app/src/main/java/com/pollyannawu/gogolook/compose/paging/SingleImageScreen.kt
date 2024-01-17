package com.pollyannawu.gogolook.compose.paging

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.google.accompanist.coil.rememberCoilPainter
import com.pollyannawu.gogolook.R
import com.pollyannawu.gogolook.data.dataclass.Hit

private const val DIMENSION_RATIO = "1:1"


@Composable
fun SingleImageScreen(
    hit: Hit,
    modifier: Modifier = Modifier,
    isLinear: Boolean = true
) {
    Card(
        modifier = Modifier
            .padding(all = 16.dp)
            .fillMaxSize()
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp)
    ) {

        val singleImage by remember {
            mutableStateOf(hit)
        }

        val userPainter = rememberCoilPainter(
            request = singleImage.userImageURL,
            fadeIn = true
        )

        val mainImagePainter = rememberCoilPainter(
            request = singleImage.largeImageURL,
            fadeIn = true
        )

        ConstraintLayout {

            // set constraint id
            val (
                userImage, userName,
                mainImage,
                likes, downloads, comments, views
            ) = createRefs()

            if (isValidUrl(singleImage.userImageURL)) {


                    Image(
                        painter = userPainter,
                        modifier = Modifier

                            .clip(CircleShape)
                            .fillMaxWidth(.15f)
                            .paint(painterResource(id = R.drawable.gogolook))
                            .background(MaterialTheme.colorScheme.surface)
                            .constrainAs(userImage) {
                                top.linkTo(parent.top, margin = 16.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                                height = Dimension.ratio(DIMENSION_RATIO)
                            },
                        contentDescription = "user image",
                        contentScale = ContentScale.Crop
                    )
                }

                    Text(
                        text = singleImage.user,
                        modifier = Modifier

                            .padding(start = 16.dp)
                            .constrainAs(userName) {
                                start.linkTo(userImage.end)
                                top.linkTo(userImage.top)
                                bottom.linkTo(userImage.bottom)
                            },
                        style = MaterialTheme.typography.titleMedium,
                    )

                    Image(
                        painter = mainImagePainter,
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .paint(painterResource(id = R.drawable.gogolook))
                            .fillMaxWidth()
                            .constrainAs(mainImage) {
                                top.linkTo(userImage.bottom, margin = 16.dp)
                                start.linkTo(parent.start, margin = 16.dp)
                                end.linkTo(parent.end, margin = 16.dp)

                                height = Dimension.ratio(DIMENSION_RATIO)
                            },
                        contentDescription = "image content",
                        contentScale = ContentScale.Crop

                    )

                    if (isLinear) {

                        NumberIcon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .constrainAs(likes) {
                                    top.linkTo(mainImage.bottom, margin = 8.dp)
                                    start.linkTo(downloads.end)

                                    end.linkTo(mainImage.end, margin = 16.dp)
                                    height = Dimension.ratio(DIMENSION_RATIO)

                                },
                            number = singleImage.likes,
                            iconId = R.drawable.like_icon
                        )

                        NumberIcon(
                            modifier = Modifier
                                .constrainAs(downloads) {
                                    top.linkTo(likes.top)
                                    start.linkTo(comments.end)
                                    end.linkTo(likes.start)
                                    height = Dimension.ratio(DIMENSION_RATIO)
                                },
                            number = singleImage.downloads,
                            iconId = R.drawable.download_icon
                        )

                        NumberIcon(
                            modifier = Modifier

                                .constrainAs(comments) {
                                    top.linkTo(likes.top)
                                    start.linkTo(views.end)
                                    end.linkTo(downloads.start)
                                    height = Dimension.ratio(DIMENSION_RATIO)
                                },
                            number = singleImage.comments,
                            iconId = R.drawable.comment_icon
                        )

                        NumberIcon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .constrainAs(views) {
                                    top.linkTo(likes.top)
                                    start.linkTo(mainImage.start, margin = 16.dp)
                                    end.linkTo(comments.start)
                                    height = Dimension.ratio(DIMENSION_RATIO)
                                },
                            number = singleImage.views,
                            iconId = R.drawable.view_icon
                        )

                    } else {
                        // when grid layout
                        NumberIcon(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .constrainAs(likes) {
                                    top.linkTo(mainImage.bottom, margin = 8.dp)
                                    start.linkTo(comments.end)
                                    end.linkTo(mainImage.end, margin = 16.dp)
                                    height = Dimension.ratio(DIMENSION_RATIO)

                                },
                            number = singleImage.likes,
                            iconId = R.drawable.like_icon
                        )
                        NumberIcon(
                            modifier = Modifier.constrainAs(comments) {
                                top.linkTo(likes.top)
                                start.linkTo(mainImage.start, margin = 16.dp)
                                end.linkTo(likes.start)
                                height = Dimension.ratio(DIMENSION_RATIO)
                            },
                            number = singleImage.comments,
                            iconId = R.drawable.comment_icon

                        )
                    }
                }

        }
    }




fun isValidUrl(url: String): Boolean {
    return url.startsWith("https")
}

@Composable
fun NumberIcon(modifier: Modifier, number: Int, iconId: Int) {

    Column(
        modifier.wrapContentWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val numberText = getNumberText(number)
        Text(
            text = numberText,
            modifier = modifier,
            style = MaterialTheme.typography.titleMedium,
        )
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "icon",
            modifier = Modifier
                .fillMaxSize(0.3f)
                .padding(top = 8.dp)
        )
    }

}

fun getNumberText(number: Int): String {
    return if (number in 1000..999999) {
        val quotient = number / 1000
        val reminders = (number % 1000) / 100
        "${quotient}.${reminders}k"
    } else if(number > 999999) {
        val quotient = number / 1000000
        val reminders = (number % 1000000) / 100000
        "${quotient}.${reminders}m"
    } else {
        number.toString()
    }
}


//@Preview
//@Composable
//fun previewLinearImageScreen() {
//
//    val fakeHit: Hit = Hit(
//        id = 1,
//        pageURL = "https://example.com/page/1",
//        type = "photo",
//        tags = "nature, outdoors",
//        previewURL = "https://example.com/images/preview/1.jpg",
//        previewWidth = 320,
//        previewHeight = 240,
//        webformatURL = "https://example.com/images/webformat/1.jpg",
//        webformatWidth = 1024,
//        webformatHeight = 768,
//        largeImageURL = "https://example.com/images/large/1.jpg",
//        imageWidth = 2048,
//        imageHeight = 1536,
//        imageSize = 1024 * 1024,
//        views = 1000,
//        downloads = 500,
//        collections = 50,
//        likes = 300,
//        comments = 100,
//        userId = 101,
//        user = "John Doe",
//        userImageURL = "https://example.com/users/johndoe.jpg"
//    )
//
//    SingleImageScreen(hit = fakeHit, isLinear = true)
//}
