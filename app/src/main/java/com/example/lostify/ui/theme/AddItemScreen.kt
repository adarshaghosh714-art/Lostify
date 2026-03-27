package com.example.lostify.ui.theme

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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.lostify.data.ItemType
import com.example.lostify.data.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemScreen(
    navController: NavController,
    viewModel: AddItemViewModel
) {

    val context = LocalContext.current
    val profileViewModel: ProfileViewModel = viewModel()
    val user by profileViewModel.user.collectAsState()

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var itemType by remember { mutableStateOf(ItemType.LOST) }
    var title by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }


    LaunchedEffect(Unit) {
        profileViewModel.fetchUserData()
    }


    LaunchedEffect(user) {
        if (user.phone.isNotEmpty()) contactNumber = user.phone
        if (user.email.isNotEmpty()) email = user.email
    }

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
                title = { Text("Add Item", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2)
                )
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD))
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

            PostButton {
                if (title.isNotBlank() && location.isNotBlank()) {
                    viewModel.addItem(
                        type = itemType,
                        title = title.trim(),
                        location = location.trim(),
                        description = description.trim(),
                        contactNumber = contactNumber.trim(),
                        email = email.trim(),
                        imageUri = imageUri,
                        onComplete = {
                            navController.popBackStack()
                        }
                    )
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
            .background(Color.White, RoundedCornerShape(16.dp))
            .clickable { onImageClick() },
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text("Tap to Upload Image", color = Color.Gray)
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
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(4.dp)
    ) {

        ToggleButton(
            text = "Lost",
            selected = selectedType == ItemType.LOST,
            onClick = { onTypeChange(ItemType.LOST) },
            modifier = Modifier.weight(1f)
        )

        ToggleButton(
            text = "Found",
            selected = selectedType == ItemType.FOUND,
            onClick = { onTypeChange(ItemType.FOUND) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun ToggleButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) Color(0xFF1976D2) else Color.Transparent,
            contentColor = if (selected) Color.White else Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
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

        OutlinedTextField(title, onTitleChange, label = { Text("Item Title") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(location, onLocationChange, label = { Text("Location") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(description, onDescriptionChange, label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        OutlinedTextField(contactNumber, onContactNumberChange, label = { Text("Phone Number") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(email, onEmailChange, label = { Text("Email (optional)") }, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun PostButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1976D2),
            contentColor = Color.White
        )
    ) {
        Text("Post to Campus")
    }
}