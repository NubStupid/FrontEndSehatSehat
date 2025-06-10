package com.example.sehatsehat.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.ui.LoginActivity
import androidx.compose.material3.IconButton

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
                ProgramListSection(samplePrograms())
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
            IconButton(onClick = {
                startActivity(Intent(this@AdminHomepageActivity, LoginActivity::class.java))
                finish()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun NavigationSection() {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NavCard(
                icon = Icons.Outlined.AccountBox,
                title = "Profile",
                onClick = {
                    startActivity(Intent(this@AdminHomepageActivity, AdminProfileActivity::class.java))
                }
            )
            NavCard(
                icon = Icons.Outlined.Person,
                title = "Users",
                onClick = {
                    startActivity(Intent(this@AdminHomepageActivity, AdminListUserActivity::class.java))
                }
            )
            NavCard(
                icon = Icons.Outlined.Home,
                title = "Programs",
                onClick = {
                    // Already on this screen; optionally scroll or refresh
                }
            )
        }
    }

    @Composable
    private fun NavCard(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        title: String,
        onClick: () -> Unit
    ) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .width(100.dp)
                .height(100.dp)
                .clickable { onClick() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    @Composable
    private fun ProgramListSection(programs: List<ProgramEntity>) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            Text(
                text = "Daftar Program",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            if (programs.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada program terdaftar.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(programs) { program ->
                        ProgramItemCard(program)
                    }
                }
            }
        }
    }

    @Composable
    private fun ProgramItemCard(program: ProgramEntity) {
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Navigasi ke detail program jika diperlukan
                }
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = program.program_name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Harga: Rp ${program.pricing}",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 4.dp),
                    color = Color.DarkGray
                )
            }
        }
    }

    /**
     * Contoh data sementara. Gantilah dengan data nyata dari ViewModel jika tersedia.
     */
    private fun samplePrograms(): List<ProgramEntity> {
        val now = System.currentTimeMillis()
        return listOf(
            ProgramEntity(
                id = "P001",
                program_name = "Program Kebugaran",
                pricing = 50000f,
                createdAt = now,
                updatedAt = now,
                deletedAt = 0L
            ),
            ProgramEntity(
                id = "P002",
                program_name = "Program Nutrisi",
                pricing = 75000f,
                createdAt = now - 86400000L, // satu hari yang lalu
                updatedAt = now - 86400000L,
                deletedAt = 0L
            ),
            ProgramEntity(
                id = "P003",
                program_name = "Program Berat Badan Ideal",
                pricing = 100000f,
                createdAt = now - 2 * 86400000L, // dua hari yang lalu
                updatedAt = now - 2 * 86400000L,
                deletedAt = 0L
            )
        )
    }
}
