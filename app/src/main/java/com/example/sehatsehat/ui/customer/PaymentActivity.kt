package com.example.sehatsehat.ui.customer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.PaymentActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class PaymentActivity : ComponentActivity() {
    private val vm by viewModels<PaymentActivityViewModel> { SehatViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val program = intent.getParcelableExtra<FitnessProgram>("program") ?: return finish()
        val activeUser = intent.getParcelableExtra<UserEntity>("active_user") ?: return finish()

        setContent {
            SehatSehatTheme {
                PaymentScreen(
                    program = program,
                    user = activeUser,
                    onBackClick = { finish() },
                    onPaymentSuccess = { updatedBalance ->
                        activeUser.balance = updatedBalance
                        Toast.makeText(
                            this,
                            "Payment successful! New balance: ${parseBalanceToIDR(updatedBalance)}",
                            Toast.LENGTH_LONG
                        ).show()
                        val data = Intent().apply {
                            putExtra("new_balance", updatedBalance)
                        }
                        setResult(Activity.RESULT_OK, data)
                        finish()
                    }
                )
            }
        }
    }

    private fun parseBalanceToIDR(balance: Int): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
        formatter.currency = Currency.getInstance("IDR")
        formatter.maximumFractionDigits = 0
        return formatter.format(balance)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaymentScreen(
        program: FitnessProgram,
        user: UserEntity,
        onBackClick: () -> Unit,
        onPaymentSuccess: (Int) -> Unit
    ) {
        val paymentUrl by vm.paymentUrl.observeAsState()
        val newBalance by vm.newBalance.observeAsState()
        var selectedPaymentMethod by remember { mutableStateOf<String?>(null) }
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        // Panggil callback jika balance terupdate
        newBalance?.let { onPaymentSuccess(it) }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Payment") },
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
            },
            bottomBar = {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shadowElevation = 8.dp,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (paymentUrl.isNullOrEmpty()) {
                        Button(
                            onClick = {
                                if (selectedPaymentMethod == "Balance" && user.balance < program.price) {
                                    Toast.makeText(
                                        context,
                                        "Insufficient balance!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    return@Button
                                }
                                scope.launch(Dispatchers.IO) {
                                    vm.pay(
                                        programId = program.id,
                                        userId = user.username,
                                        price = program.price,
                                        viaMidtrans = selectedPaymentMethod == "Midtrans"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB800)),
                            enabled = selectedPaymentMethod != null
                        ) {
                            Text(
                                text = "Pay ${parseBalanceToIDR(program.price)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        ShowQRCode(
                            qrCodeUrl = paymentUrl!!,
                            onClick = {
                                if (user.balance >= program.price) {
                                    scope.launch(Dispatchers.IO) {
                                        vm.pay(
                                            programId = program.id,
                                            userId = user.username,
                                            price = program.price,
                                            viaMidtrans = false
                                        )
                                    }
                                } else {
                                    Toast.makeText(context, "Insufficient balance!", Toast.LENGTH_SHORT).show()
                                }
                                selectedPaymentMethod = "Balance"
                            }
                        )
                    }
                }
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Order Summary
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Order Summary",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Program:", fontSize = 16.sp)
                            Text(
                                text = program.title,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = "Your Balance:", fontSize = 16.sp)
                            Text(
                                text = parseBalanceToIDR(user.balance),
                                fontSize = 16.sp
                            )
                        }
                        Divider(modifier = Modifier.padding(vertical = 16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = parseBalanceToIDR(program.price),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFFB800)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Payment Method",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf(
                        PaymentMethod("Balance", "Use your account balance"),
                        PaymentMethod("Midtrans", "Pay via Midtrans QR")
                    ).forEach { method ->
                        PaymentMethodCard(
                            method = method,
                            isSelected = selectedPaymentMethod == method.name,
                            onClick = { selectedPaymentMethod = method.name }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ShowQRCode(qrCodeUrl: String, onClick: () -> Unit) {
        Image(
            painter = rememberImagePainter(qrCodeUrl),
            contentDescription = "QR Code",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Text(text = "Confirm Payment")
        }
    }

    @Composable
    fun PaymentMethodCard(
        method: PaymentMethod,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (isSelected) Color(0xFFFFF8E1) else Color(0xFFF8F9FA)
            ),
            border = if (isSelected) BorderStroke(1.dp, Color(0xFFFFB800)) else null
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = isSelected,
                    onClick = onClick,
                    colors = RadioButtonDefaults.colors(selectedColor = Color(0xFFFFB800))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = method.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    Text(text = method.description, fontSize = 14.sp, color = Color.Gray)
                }
            }
        }
    }

    data class PaymentMethod(val name: String, val description: String)
}