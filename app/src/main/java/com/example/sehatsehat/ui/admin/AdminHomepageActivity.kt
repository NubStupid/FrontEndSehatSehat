package com.example.sehatsehat.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.ui.LoginActivity

class AdminHomepageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopBar()
                NavigationSection()
                // Jika perlu, tambahkan konten lain di bawah tombol (misalnya daftar program)
            }
        }
    }

    @Composable
    private fun TopBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Admin Dashboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            IconButton(
                onClick = {
                    // Logout: kembali ke LoginActivity dan finish activity ini
                    startActivity(Intent(this@AdminHomepageActivity, LoginActivity::class.java))
                    finish()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @Composable
    private fun NavigationSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BigNavButton(
                modifier       = Modifier.weight(1f),
                icon           = Icons.Outlined.List,
                title          = "List Program",
                backgroundColor= Color(0xFF00AA13),
                onClick        = {
                    startActivity(Intent(this@AdminHomepageActivity, AdminListProgramActivity::class.java))
                }
            )
            BigNavButton(
                modifier       = Modifier.weight(1f),
                icon           = Icons.Outlined.DateRange,
                title          = "List Artikel",
                backgroundColor= Color(0xFF00AA13),
                onClick        = {
                    startActivity(Intent(this@AdminHomepageActivity, AdminListArtikelActivity::class.java))
                }
            )
            BigNavButton(
                modifier       = Modifier.weight(1f),
                icon           = Icons.Outlined.Person,
                title          = "List User",
                backgroundColor= Color(0xFF00AA13),
                onClick        = {
                    startActivity(Intent(this@AdminHomepageActivity, AdminListUserActivity::class.java))
                }
            )
        }
    }

    @Composable
    private fun BigNavButton(
        modifier: Modifier = Modifier,        // ← tambahkan parameter
        icon: ImageVector,
        title: String,
        backgroundColor: Color,
        onClick: () -> Unit
    ) {
        Card(
            modifier = modifier                // ← gunakan modifier di sini
                .height(160.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(color = backgroundColor, shape = RoundedCornerShape(36.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
        }
    }
}
