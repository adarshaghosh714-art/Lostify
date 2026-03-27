package com.example.lostify.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lostify.R
import com.example.lostify.data.FirebaseLostItem
import com.example.lostify.data.ItemType
import com.example.lostify.data.LostItemViewModel
import com.example.lostify.navigation.NavRoutes
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    itemList: List<FirebaseLostItem>,
    viewModel: LostItemViewModel
) {

    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid

    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf<ItemType?>(null) }

    val filteredItems = itemList.filter { item ->

        val matchesType =
            selectedType == null || item.type == selectedType?.name

        val matchesQuery =
            (item.title ?: "").contains(query, ignoreCase = true)

        matchesType && matchesQuery
    }

    Scaffold(

        containerColor = Color(0xFFE3F2FD),

        topBar = {
            Column {

                TopAppBar(
                    title = {

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Image(
                                painter = painterResource(id = R.drawable.ic_brand),
                                contentDescription = "Lostify Logo",
                                modifier = Modifier.padding(8.dp)
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Lostify",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("profile")
                        }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = Color.White
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF1976D2)
                    )
                )

                SearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    onSearch = { active = false },
                    active = active,
                    onActiveChange = { active = it },
                    placeholder = {
                        Text(
                            text = "Search lost items",
                            fontStyle = FontStyle.Italic
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search"
                        )
                    },
                    modifier = Modifier.padding(12.dp)
                ) {}
            }
        },

        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(NavRoutes.AddItem.route)
                },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Item"
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {

            FilterRow(
                selectedType = selectedType,
                onTypeSelected = { selectedType = it }
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredItems.isEmpty()) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "No items found",
                        color = Color.Gray
                    )
                }

            } else {

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    items(filteredItems) { item ->

                        LostItemCard(
                            item = item,
                            isOwner = currentUserId == item.userId,
                            onClick = { itemId ->
                                navController.navigate(
                                    route = NavRoutes.Detail.passItemId(itemId)
                                )
                            },
                            onDelete = { selectedItem ->
                                viewModel.deleteItem(selectedItem)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterRow(
    selectedType: ItemType?,
    onTypeSelected: (ItemType?) -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        FilterButton(
            text = "All",
            selected = selectedType == null,
            onClick = { onTypeSelected(null) },
            modifier = Modifier.weight(1f)
        )

        FilterButton(
            text = "Lost",
            selected = selectedType == ItemType.LOST,
            onClick = { onTypeSelected(ItemType.LOST) },
            modifier = Modifier.weight(1f)
        )

        FilterButton(
            text = "Found",
            selected = selectedType == ItemType.FOUND,
            onClick = { onTypeSelected(ItemType.FOUND) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun FilterButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor =
                if (selected) Color(0xFF1976D2)
                else Color.Transparent,
            contentColor =
                if (selected) Color.White
                else Color.Black
        ),
        shape = RoundedCornerShape(10.dp),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {

        Text(text)
    }
}