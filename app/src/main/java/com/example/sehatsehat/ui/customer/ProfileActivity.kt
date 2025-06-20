package com.example.sehatsehat.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.R
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.LoginActivity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.ProfileViewModel

class ProfileActivity : ComponentActivity() {
    val vm by viewModels<ProfileViewModel>(){ SehatViewModelFactory}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val displayName = intent.getStringExtra("display_name") ?: "Hanvy Hendrawan"
        val activeUser = intent.getParcelableExtra<UserEntity>("active_user")
        if(activeUser != null){
            vm.init(activeUser)

            setContent {
                SehatSehatTheme {
                    ProfileScreen(
                        displayName = displayName,
                        onBackClick = { finish() },
                        onEditProfile = {
                            // Handle edit profile
                        },
                        onLogout = {
                            val intent = Intent(this, LoginActivity::class.java).apply {
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            }
                            startActivity(intent)
                            finish()
                        },
                        onChatWithChatbot = {

                        },
                        onTopUp = {

                        }
                    )
                }
            }
        }
    }

    companion object {
        fun newIntent(context: Context, displayName: String? = null): Intent {
            return Intent(context, ProfileActivity::class.java).apply {
                displayName?.let { putExtra("display_name", it) }
            }
        }
    }
        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun ProfileScreen(
            displayName: String,
            onBackClick: () -> Unit,
            onEditProfile: () -> Unit,
            onTopUp:()->Unit,
            onChatWithChatbot:()->Unit,
            onLogout: () -> Unit
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("My Profile") },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.White,
                            titleContentColor = Color.Black
                        )
                    )
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier.size(120.dp),
                            contentAlignment = Alignment.BottomEnd
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                            IconButton(
                                onClick = onEditProfile,
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFFFB800), CircleShape)
                            ) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = displayName,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        val memberSince:State<String?> = vm.memberSince.observeAsState()
                        val since = memberSince.value
                        Text(
                            text = since.toString(),
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                        val programAssigned:State<Int?> = vm.programAssigned.observeAsState()
                        val assigned = programAssigned.value
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            ProfileStatItem(value = assigned.toString(), label = "Programs")
        //                    ProfileStatItem(value = "36", label = "Workouts")
        //                    ProfileStatItem(value = "98%", label = "Completion")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Account Settings",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ProfileOptionItem(
                            icon = Icons.Default.Person,
                            text = "Update Personal Information",
                            onClick = onEditProfile
                        )
                        ProfileOptionItem(
                            icon = Icons.Default.Person,
                            text = "Top Up",
                            onClick = { /* Handle privacy settings */ }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Support",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        ProfileOptionItem(
                            icon = Icons.Default.Build,
                            text = "Help Center using Chatbot",
                            onClick = { /* Open help center */ }
                        )


                        Spacer(modifier = Modifier.height(24.dp))

                        Button(
                            onClick = onLogout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF8F9FA),
                                contentColor = Color.Red
                            )
                        ) {
                            Text(
                                text = "Logout",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }

        @Composable
        fun ProfileStatItem(value: String, label: String) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = value,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFB800)
                )
                Text(
                    text = label,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }

        @Composable
        fun ProfileOptionItem(icon: ImageVector, text: String, onClick: () -> Unit) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable(onClick = onClick),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        icon,
                        contentDescription = text,
                        tint = Color(0xFFFFB800),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = text,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = "Navigate",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

    }
}
