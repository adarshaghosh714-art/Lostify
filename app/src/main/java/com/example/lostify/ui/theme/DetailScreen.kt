package com.example.lostify.ui.theme



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lostify.R

@Composable
fun ItemDetailsScreen(itemId: String?){

    Column(modifier = Modifier
        .padding(16.dp)){
        Image(
            painter = painterResource(R.drawable.ic_lost1),
            contentDescription =null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = "Item ID: $itemId",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = "EarBuds",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Location: College Canteen",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Date: 24 Jan 2026",
            style = MaterialTheme.typography.bodyMedium,
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Description: White colored Earbuds",
            style = MaterialTheme.typography.bodySmall,
            fontStyle = FontStyle.Italic
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Contact Info: 000000000",
            style = MaterialTheme.typography.bodySmall,
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ItemDetailsScreenPreview(){
    ItemDetailsScreen(itemId = "1")
}


