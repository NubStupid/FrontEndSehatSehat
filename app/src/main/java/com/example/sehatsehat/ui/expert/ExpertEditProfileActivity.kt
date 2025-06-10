package com.example.sehatsehat.ui.expert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.ui.theme.SehatSehatTheme

class ExpertEditProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Ambil data dari Intent, sama seperti di Homepage
        val displayName = intent.getStringExtra("display_name") ?: "Hanvy Hendrawan"
        val email = intent.getStringExtra("email") ?: "hanvy.h@example.com"
        val phone = intent.getStringExtra("phone") ?: "081234567890"

        setContent {
            SehatSehatTheme {
                EditProfileScreen(
                    initialDisplayName = displayName,
                    initialEmail = email,
                    initialPhone = phone,
                    onNavigateBack = {
                        // Aksi untuk kembali ke halaman sebelumnya
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    initialDisplayName: String,
    initialEmail: String,
    initialPhone: String,
    onNavigateBack: () -> Unit
) {
    var displayName by remember { mutableStateOf(initialDisplayName) }
    var email by remember { mutableStateOf(initialEmail) }
    var phoneNumber by remember { mutableStateOf(initialPhone) }
    var specialization by remember { mutableStateOf("Fitness & Cardio") }
    var bio by remember { mutableStateOf("Certified fitness expert with 5+ years of experience in helping clients achieve their goals.") }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()) // Membuat kolom bisa di-scroll
        ) {
            Spacer(modifier = Modifier.height(24.dp))

//            // Profile Picture Section
//            ProfilePictureEditable(
//                modifier = Modifier.align(Alignment.CenterHorizontally)
//            )

            Spacer(modifier = Modifier.height(32.dp))

            // Form Fields
            ProfileTextField(
                label = "Full Name",
                value = displayName,
                onValueChange = { displayName = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "Email",
                value = email,
                onValueChange = { email = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "Phone Number",
                value = phoneNumber,
                onValueChange = { phoneNumber = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "Specialization",
                value = specialization,
                onValueChange = { specialization = it }
            )
            Spacer(modifier = Modifier.height(16.dp))

            ProfileTextField(
                label = "Bio",
                value = bio,
                onValueChange = { bio = it },
                singleLine = false,
                minLines = 4
            )

            Spacer(modifier = Modifier.weight(1f)) // Mendorong tombol ke bawah

            // Save Button
            Button(
                onClick = {
                    // TODO: Tambahkan logika untuk menyimpan perubahan
                    onNavigateBack() // Kembali setelah menyimpan
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB800) // Warna kuning khas aplikasi
                )
            ) {
                Text(
                    text = "Save Changes",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

//@Composable
//fun ProfilePictureEditable(modifier: Modifier = Modifier) {
//    Box(modifier = modifier.size(120.dp)) {
//        // Placeholder untuk foto profil
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .clip(CircleShape)
//                .background(Color.Gray.copy(alpha = 0.3f)),
//            contentAlignment = Alignment.Center
//        ) {
//            // Anda bisa mengganti Icon ini dengan AsyncImage untuk memuat gambar dari URL
//            Icon(
//                Icons.Default.CameraAlt,
//                contentDescription = "Profile Picture Placeholder",
//                tint = Color.Gray,
//                modifier = Modifier.size(48.dp)
//            )
//        }
//        // Tombol Edit kecil di atas foto
//        IconButton(
//            onClick = { /* TODO: Logika untuk mengganti gambar */ },
//            modifier = Modifier
//                .size(36.dp)
//                .clip(CircleShape)
//                .background(Color(0xFFFFB800))
//                .align(Alignment.BottomEnd)
//        ) {
//            Icon(
//                Icons.Default.CameraAlt,
//                contentDescription = "Edit Picture",
//                tint = Color.White,
//                modifier = Modifier.size(20.dp)
//            )
//        }
//    }
//}

@Composable
fun ProfileTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column {
        Text(
            text = label,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFFFFB800),
                unfocusedBorderColor = Color.LightGray,
                cursorColor = Color(0xFFFFB800),
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5)
            ),
            singleLine = singleLine,
            minLines = minLines
        )
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfileScreenPreview() {
    SehatSehatTheme {
        EditProfileScreen(
            initialDisplayName = "Hanvy Hendrawan",
            initialEmail = "hanvy.h@example.com",
            initialPhone = "081234567890",
            onNavigateBack = {}
        )
    }
}