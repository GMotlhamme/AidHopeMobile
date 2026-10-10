package com.example.aidhopemobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val AidBurgundy = Color(0xFF8B0000)
private val PageBackground = Color(0xFFF5F6F8)
private val TextDark = Color(0xFF202124)
private val MutedText = Color(0xFF687386)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                AidHopeApp()
            }
        }
    }
}

@Composable
fun AidHopeApp() {
    var selectedTab by remember { mutableStateOf("Home") }
    var selectedAction by remember { mutableStateOf("") }
    var selectedBeneficiaryId by remember {
        mutableStateOf<String?>(null)
    }

    val beneficiaries = remember {
        mutableStateListOf(
            BeneficiaryRecord(
                "B001", "Thabo", "Mokoena", "071 234 5678",
                "", "", "HIV Positive"
            ),
            BeneficiaryRecord(
                "B002", "Nomsa", "Dlamini", "082 345 6789",
                "", "", "Affected"
            ),
            BeneficiaryRecord(
                "B003", "Sipho", "Khumalo", "079 876 5432",
                "", "", "HIV Positive"
            ),
            BeneficiaryRecord(
                "B004", "Lerato", "Nkosi", "083 111 2222",
                "", "", "Affected"
            ),
            BeneficiaryRecord(
                "B005", "Anele", "Zulu", "076 555 4444",
                "", "", "HIV Positive"
            )
        )
    }
    Scaffold(
        containerColor = PageBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(AidBurgundy)
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "AID HOPE +",
                    color = Color.White,
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "User profile",
                    tint = Color.White
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                val tabs = listOf(
                    Triple("Home", Icons.Default.Home, "Home"),
                    Triple("Beneficiaries", Icons.Default.People, "Beneficiaries"),
                    Triple("Awareness", Icons.Default.CalendarMonth, "Awareness"),
                    Triple("Reports", Icons.Default.BarChart, "Reports")
                )

                tabs.forEach { (label, icon, route) ->
                    NavigationBarItem(
                        selected = selectedTab == route,
                        onClick = {
                            selectedTab = route
                            selectedAction = ""
                        },
                        icon = {
                            Icon(icon, contentDescription = label)
                        },
                        label = { Text(label, fontSize = 10.sp) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = AidBurgundy,
                            selectedTextColor = AidBurgundy,
                            indicatorColor = Color(0xFFF8E6E6)
                        )
                    )
                }
            }
        }
    ) { padding ->

        when {
            selectedTab == "Beneficiaries" -> {
                val selectedBeneficiary = beneficiaries.find {
                    it.id == selectedBeneficiaryId
                }

                if (selectedBeneficiary != null) {
                    BeneficiaryDetailsScreen(
                        beneficiary = selectedBeneficiary,
                        modifier = Modifier.padding(padding),
                        onBack = {
                            selectedBeneficiaryId = null
                        },
                        onSave = { updated ->
                            val index = beneficiaries.indexOfFirst {
                                it.id == updated.id
                            }

                            if (index != -1) {
                                beneficiaries[index] = updated
                            }

                            selectedBeneficiaryId = null
                        },
                        onDelete = {
                            beneficiaries.removeAll {
                                it.id == selectedBeneficiary.id
                            }

                            selectedBeneficiaryId = null
                        }
                    )
                } else {
                    BeneficiariesScreen(
                        beneficiaries = beneficiaries,
                        modifier = Modifier.padding(padding),
                        onAdd = {
                            selectedAction = "Add Beneficiary"
                        },
                        onSelect = { beneficiary ->
                            selectedBeneficiaryId = beneficiary.id
                        }
                    )
                }
            }

            selectedTab == "Home" && selectedAction.isEmpty() -> {
                DashboardScreen(
                    modifier = Modifier.padding(padding),
                    onAction = { selectedAction = it }
                )
            }

            selectedTab == "Beneficiaries" -> {
                BeneficiariesScreen(
                    beneficiaries = beneficiaries,
                    modifier = Modifier.padding(padding),
                    onAdd = { selectedAction = "Add Beneficiary" },
                    onSelect = TODO()
                )
            }

            else -> {
                PlaceholderScreen(
                    title = selectedAction.ifEmpty { selectedTab },
                    onBack = {
                        selectedAction = ""
                        selectedTab = "Home"
                    },
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

@Composable
fun DashboardScreen(
    modifier: Modifier = Modifier,
    onAction: (String) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                "Welcome, Staff User",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                "Together we create healthier, stronger communities.",
                color = MutedText,
                fontSize = 14.sp
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(
                    title = "Beneficiaries",
                    value = "12",
                    icon = Icons.Default.People,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Support Sessions",
                    value = "8",
                    icon = Icons.Default.Favorite,
                    modifier = Modifier.weight(1f)
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                SummaryCard(
                    title = "Awareness Events",
                    value = "5",
                    icon = Icons.Default.Campaign,
                    modifier = Modifier.weight(1f)
                )
                SummaryCard(
                    title = "Pending Requests",
                    value = "3",
                    icon = Icons.Default.Schedule,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        Text(
            "Quick Actions",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        QuickAction(
            title = "Add Beneficiary",
            icon = Icons.Default.PersonAdd,
            onClick = { onAction("Add Beneficiary") }
        )
        QuickAction(
            title = "Log Support Session",
            icon = Icons.Default.FavoriteBorder,
            onClick = { onAction("Log Support Session") }
        )
        QuickAction(
            title = "Create Awareness Event",
            icon = Icons.Default.Event,
            onClick = { onAction("Create Awareness Event") }
        )
        QuickAction(
            title = "View Awareness Requests",
            icon = Icons.Default.Assignment,
            onClick = { onAction("Awareness Requests") }
        )
    }
}

@Composable
fun SummaryCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = AidBurgundy,
                modifier = Modifier.size(28.dp)
            )
            Text(
                value,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
            Text(
                "View details  ›",
                fontSize = 11.sp,
                color = MutedText
            )
        }
    }
}

@Composable
fun QuickAction(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(icon, contentDescription = null, tint = AidBurgundy)
            Text(
                title,
                modifier = Modifier.weight(1f),
                fontWeight = FontWeight.Medium,
                color = TextDark
            )
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MutedText
            )
        }
    }
}

@Composable
fun PlaceholderScreen(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            title,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )
        Text(
            "This section is next on our implementation list.",
            color = MutedText
        )
        Button(
            onClick = onBack,
            colors = ButtonDefaults.buttonColors(
                containerColor = AidBurgundy
            )
        ) {
            Text("Back")
        }
    }
}


data class BeneficiaryRecord(
    val id: String,
    val firstName: String,
    val lastName: String,
    val contactNumber: String,
    val email: String,
    val address: String,
    val status: String
) {
    val fullName: String
        get() = "$firstName $lastName"
}

@Composable
fun BeneficiariesScreen(
    beneficiaries: List<BeneficiaryRecord>,
    modifier: Modifier = Modifier,
    onAdd: () -> Unit,
    onSelect: (BeneficiaryRecord) -> Unit
) {
    var search by remember { mutableStateOf("") }

    val filteredBeneficiaries = beneficiaries.filter {
        it.fullName.contains(search, ignoreCase = true) ||
                it.id.contains(search, ignoreCase = true) ||
                it.contactNumber.contains(search, ignoreCase = true)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Beneficiaries",
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Button(
                onClick = onAdd,
                colors = ButtonDefaults.buttonColors(
                    containerColor = AidBurgundy
                )
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null
                )
                Text(" Add")
            }
        }

        OutlinedTextField(
            value = search,
            onValueChange = { search = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search by name, contact or ID...") },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = null)
            },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )

        Text(
            "${filteredBeneficiaries.size} beneficiaries",
            color = MutedText,
            fontSize = 13.sp
        )

        if (filteredBeneficiaries.isEmpty()) {
            Text(
                "No beneficiaries found.",
                color = MutedText
            )
        }

        filteredBeneficiaries.forEach { beneficiary ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        onSelect(beneficiary)
                    },
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            )  {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = null,
                            tint = AidBurgundy,
                            modifier = Modifier.size(42.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                beneficiary.fullName,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                            )
                            Text(
                                "ID: ${beneficiary.id}",
                                fontSize = 12.sp,
                                color = MutedText
                            )
                        }
                    }

                    Text(
                        "Contact: ${beneficiary.contactNumber}",
                        fontSize = 13.sp,
                        color = TextDark
                    )

                    Text(
                        "Status: ${beneficiary.status}",
                        fontSize = 13.sp,
                        color = AidBurgundy,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun BeneficiaryFormScreen(
    onBack: () -> Unit,
    onSave: (BeneficiaryRecord) -> Unit
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var contactNumber by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("Affected") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            "Add Beneficiary",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            "Enter the beneficiary's details below.",
            color = MutedText
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = contactNumber,
            onValueChange = { contactNumber = it },
            label = { Text("Contact Number *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Text(
            "Beneficiary Status *",
            fontWeight = FontWeight.Medium,
            color = TextDark
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Affected", "HIV Positive").forEach { option ->
                FilterChip(
                    selected = status == option,
                    onClick = { status = option },
                    label = { Text(option) }
                )
            }
        }

        if (errorMessage.isNotEmpty()) {
            Text(
                errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 13.sp
            )
        }

        Button(
            onClick = {
                when {
                    firstName.isBlank() ||
                            lastName.isBlank() ||
                            contactNumber.isBlank() -> {
                        errorMessage =
                            "Please complete all required fields."
                    }

                    !contactNumber.all { it.isDigit() || it in " +-()" } -> {
                        errorMessage =
                            "Please enter a valid contact number."
                    }

                    else -> {
                        onSave(
                            BeneficiaryRecord(
                                id = "",
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                contactNumber = contactNumber.trim(),
                                email = email.trim(),
                                address = address.trim(),
                                status = status
                            )
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AidBurgundy
            )
        ) {
            Text("Save Beneficiary")
        }

        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancel")
        }
    }
}


@Composable
fun BeneficiaryDetailsScreen(
    beneficiary: BeneficiaryRecord,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onSave: (BeneficiaryRecord) -> Unit,
    onDelete: () -> Unit
) {
    var firstName by remember(beneficiary.id) {
        mutableStateOf(beneficiary.firstName)
    }
    var lastName by remember(beneficiary.id) {
        mutableStateOf(beneficiary.lastName)
    }
    var contact by remember(beneficiary.id) {
        mutableStateOf(beneficiary.contactNumber)
    }
    var email by remember(beneficiary.id) {
        mutableStateOf(beneficiary.email)
    }
    var address by remember(beneficiary.id) {
        mutableStateOf(beneficiary.address)
    }
    var status by remember(beneficiary.id) {
        mutableStateOf(beneficiary.status)
    }
    var error by remember { mutableStateOf("") }
    var showDeleteDialog by remember {
        mutableStateOf(false)
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                showDeleteDialog = false
            },
            title = { Text("Delete beneficiary?") },
            text = {
                Text(
                    "This will remove ${beneficiary.fullName} " +
                            "from the current app data."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete()
                    }
                ) {
                    Text("Delete", color = AidBurgundy)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onBack) {
            Icon(
                Icons.Default.ArrowBack,
                contentDescription = null
            )
            Text(" Back to beneficiaries")
        }

        Text(
            "Beneficiary Details",
            fontSize = 23.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Text(
            "Beneficiary ID: ${beneficiary.id}",
            color = MutedText
        )

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("First Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Last Name *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact Number *") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Address") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Text(
            "Beneficiary Status",
            fontWeight = FontWeight.Medium
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf("Affected", "HIV Positive").forEach { option ->
                FilterChip(
                    selected = status == option,
                    onClick = { status = option },
                    label = { Text(option) }
                )
            }
        }

        if (error.isNotEmpty()) {
            Text(
                error,
                color = MaterialTheme.colorScheme.error
            )
        }

        Button(
            onClick = {
                when {
                    firstName.isBlank() ||
                            lastName.isBlank() ||
                            contact.isBlank() -> {
                        error = "Complete all required fields."
                    }

                    !contact.all {
                        it.isDigit() || it in " +-()"
                    } -> {
                        error = "Enter a valid contact number."
                    }

                    else -> {
                        onSave(
                            beneficiary.copy(
                                firstName = firstName.trim(),
                                lastName = lastName.trim(),
                                contactNumber = contact.trim(),
                                email = email.trim(),
                                address = address.trim(),
                                status = status
                            )
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = AidBurgundy
            )
        ) {
            Text("Save Changes")
        }

        OutlinedButton(
            onClick = { showDeleteDialog = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = AidBurgundy
            )
        ) {
            Icon(
                Icons.Default.Delete,
                contentDescription = null
            )
            Text(" Delete Beneficiary")
        }
    }
}