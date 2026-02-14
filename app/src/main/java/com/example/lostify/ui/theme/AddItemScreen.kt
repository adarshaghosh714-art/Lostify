package com.example.lostify.ui.theme

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.lostify.data.ItemType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    viewModel: AddItemViewModel
) {

    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var itemType by remember { mutableStateOf(ItemType.LOST) }
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val imagePickerLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                context.contentResolver.takePersistableUriPermission(
                    it,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
                imageUri = it
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Item") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            ImageUploadCard(
                imageUri = imageUri,
                onImageClick = {
                    imagePickerLauncher.launch("image/*")
                }
            )

            LostFoundToggle(
                selectedType = itemType,
                onTypeChange = { itemType = it }
            )

            AddItemForm(
                title = title,
                onTitleChange = { title = it },
                location = location,
                onLocationChange = { location = it },
                description = description,
                onDescriptionChange = { description = it },
                contactNumber = contactNumber,
                onContactNumberChange = { contactNumber = it },
                email = email,
                onEmailChange = { email = it }
            )

            Spacer(modifier = Modifier.height(8.dp))

            PostButton {

                if (title.isNotBlank() && location.isNotBlank()) {

                    viewModel.addItem(
                        type = itemType,
                        title = title.trim(),
                        location = location.trim(),
                        description = description.trim(),
                        contactNumber = contactNumber.trim(),
                        email = email.trim(),
                        imageUri = imageUri?.toString()
                    )

                    navController.popBackStack()
                }
            }
        }
    }
}


@Composable
fun ImageUploadCard(
    imageUri: Uri?,
    onImageClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = "Upload Image",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}


@Composable
fun LostFoundToggle(
    selectedType: ItemType,
    onTypeChange: (ItemType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                MaterialTheme.colorScheme.surfaceVariant,
                RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
    ) {
        ItemTypeButton(
            text = "Lost",
            selected = selectedType == ItemType.LOST,
            onClick = { onTypeChange(ItemType.LOST) },
            modifier = Modifier.weight(1f)
        )
        ItemTypeButton(
            text = "Found",
            selected = selectedType == ItemType.FOUND,
            onClick = { onTypeChange(ItemType.FOUND) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ItemTypeButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected)
                MaterialTheme.colorScheme.primary
            else
                Color.Transparent,
            contentColor = if (selected)
                MaterialTheme.colorScheme.onPrimary
            else
                MaterialTheme.colorScheme.onSurface
        )
    ) {
        Text(text)
    }
}

@Composable
fun AddItemForm(
    title: String,
    onTitleChange: (String) -> Unit,
    location: String,
    onLocationChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    contactNumber: String,
    onContactNumberChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

        OutlinedTextField(
            value = title,
            onValueChange = onTitleChange,
            label = { Text("Item Title") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = location,
            onValueChange = onLocationChange,
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = contactNumber,
            onValueChange = onContactNumberChange,
            label = { Text("Contact Number") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Email (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun PostButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text("Post to Campus")
    }
}

@Preview(showBackground = true)
@Composable
fun AddItemScreenPreview() {
    val context = LocalContext.current
    val application = context.applicationContext as Application

    AddItemScreen(
        navController = rememberNavController(),
        viewModel = AddItemViewModel(application)
    )
}
