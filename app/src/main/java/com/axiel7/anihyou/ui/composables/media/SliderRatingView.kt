package com.axiel7.anihyou.ui.composables.media

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.axiel7.anihyou.R
import com.axiel7.anihyou.ui.theme.AniHyouTheme

@Composable
fun SliderRatingView(
    maxValue: Double,
    modifier: Modifier = Modifier,
    initialRating: Double = 0.0,
    showAsDecimal: Boolean = false,
    onRatingChanged: (Double) -> Unit,
) {
    var rating by remember { mutableStateOf(initialRating) }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.star_24),
                contentDescription = "star",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = if (showAsDecimal) String.format("%.1f", rating)
                else String.format("%.0f", rating),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Slider(
            value = rating.toFloat(),
            onValueChange = { rating = it.toDouble() },
            valueRange = 0f..maxValue.toFloat(),
            steps = if (maxValue <= 10.0 && !showAsDecimal) maxValue.toInt() else 0,
            onValueChangeFinished = {
                onRatingChanged(rating)
            }
        )
    }
}

@Preview
@Composable
fun SliderRatingViewPreview() {
    AniHyouTheme {
        Surface {
            SliderRatingView(
                maxValue = 100.0,
                showAsDecimal = true,
                onRatingChanged = {}
            )
        }
    }
}