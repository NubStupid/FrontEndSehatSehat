package com.example.sehatsehat.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.ViewProgramViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class ViewProgramActivity : ComponentActivity() {
    val vm by viewModels<ViewProgramViewModel> { SehatViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val program = intent.getParcelableExtra<FitnessProgram>("program")
            ?: return finish() // Safely exit if null

        val activeUser = intent.getParcelableExtra<UserEntity>("active_user")?: return finish()
        vm.init(program,activeUser)
        setContent {
            SehatSehatTheme {
                ProgramDetailScreen(
                    program = program,
                    onBackClick = { finish() },
                    onPurchaseClick = { selectedProgram ->
                        startActivity(PaymentActivity.newIntent(this, selectedProgram))
                    }
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context, program: FitnessProgram): Intent {
            return Intent(context, ViewProgramActivity::class.java).apply {
                putExtra("program", program)
            }
        }
    }
    fun parseBalanceToIDR(balance:Int):String{
            val formatter = NumberFormat.getCurrencyInstance(Locale("in","ID"))
            formatter.currency = Currency.getInstance("IDR")
            formatter.maximumFractionDigits = 0
            return formatter.format(balance)
        }


        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        fun ProgramDetailScreen(
            program: FitnessProgram,
            onBackClick: () -> Unit,
            onPurchaseClick: (FitnessProgram) -> Unit
        ) {
            val program_progress = vm.programProgress.observeAsState()
            val progress = program_progress.value

            val uiProgram = vm.uiProgram.observeAsState()
            val ui = uiProgram.value

            val user = vm.user.observeAsState()
            val user_ = user.value

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Program Details") },
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
                    if (!program.isPurchased) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = { onPurchaseClick(program) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB800))
                            ) {
                                Text(
                                    text = "Purchase for ${parseBalanceToIDR(program.price)}",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }else{
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shadowElevation = 8.dp,
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    Log.d("masuk","masuk")
                                    if(ui != null && user_ != null && progress != null){
                                        vm.nextProgress(progress.id,ui,user_)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6B46C1).copy(0.7f))
                            ) {
                                Text(
                                    text = "Complete current progress!",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            ) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                ) {
                    item {
                        // Program Hero Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(200.dp),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            program.backgroundGradient.map { Color(it) }
                                        )
                                    )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(24.dp),
                                    verticalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Column {
                                        Text(
                                            text = program.title,
                                            color = Color.White,
                                            fontSize = 24.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = "by ${program.instructor}",
                                            color = Color.White.copy(alpha = 0.9f),
                                            fontSize = 14.sp,
                                            modifier = Modifier.padding(top = 4.dp)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Column {
                                            Text(
                                                text = program.duration,
                                                color = Color.White,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        if (!program.isPurchased) {
                                            Surface(
                                                shape = RoundedCornerShape(20.dp),
                                                color = Color(0xFFFFB800)
                                            ) {
                                                Text(
                                                    text = parseBalanceToIDR(program.price),
                                                    color = Color.White,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    item {
                        // Program Details Section
                        Column(
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            if(!program.isPurchased){
                                Text(
                                    text = "About This Program",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Text(
                                    text = program.detailedDescription,
                                    fontSize = 14.sp,
                                    lineHeight = 20.sp,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )

                                Text(
                                    text = "Key Benefits",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.padding(bottom = 16.dp)
                                ) {
                                    program.benefits.forEach { benefit ->
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = "Benefit",
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Text(
                                                text = benefit,
                                                fontSize = 14.sp,
                                                modifier = Modifier.padding(start = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }else{
                                Text(
                                    text = "Current Progress",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                                val progressObs = vm.progress.observeAsState()
                                val progress = progressObs.value?:0f
                                Slider(
                                    value = progress,
                                    onValueChange = {},
                                    valueRange = 0f..1f,
                                    enabled = true,
//                                    (0xFF6B46C1, 0xFF9333EA)
                                    colors = SliderColors(activeTickColor = Color(0xFF6B46C1), activeTrackColor = Color(0xFF9333EA), inactiveTickColor = Color(0xFF6B46C1).copy(alpha = 0.3f), inactiveTrackColor = Color(0xFF9333EA).copy(0.3f), thumbColor = Color(0xFF9333EA), disabledThumbColor = Color(0xFF9333EA), disabledActiveTrackColor = Color(0xFF9333EA), disabledActiveTickColor = Color(0xFF9333EA), disabledInactiveTickColor = Color(0xFF9333EA), disabledInactiveTrackColor = Color(0xFF9333EA))
                                )
                                val percentage = progress.times(100).toInt()
                                Text(
                                    text = "Completion: ${percentage}%",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    modifier = Modifier.padding(bottom = 14.dp)
                                )

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(2.dp)
                                        .background(Color.DarkGray.copy(0.4f))
                                        .padding(vertical = 8.dp)

                                )

                                val completedObs = vm.isCompleted.observeAsState()
                                val completed = completedObs.value
                                if(completed == false){
                                    val typeObs = vm.progressType.observeAsState()
                                    val type = typeObs.value
                                    Text(
                                        text = "Current Progress",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier
                                            .padding(bottom = 8.dp)
                                            .fillMaxWidth(1f)
                                            .padding(2.dp)
                                    )
                                    if(type == "Workout"){
                                        val workoutTitleObs = vm.workoutTitle.observeAsState()
                                        val workoutTitle = workoutTitleObs.value
                                        val estimatedObs = vm.estimatedTime.observeAsState()
                                        val estimated = estimatedObs.value
                                        val focusedAt = vm.focusedAt.observeAsState()
                                        val focused = focusedAt.value?: emptyList()

                                        Text(
                                            text = "Title: ${workoutTitle}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Text(
                                            text = "Estimated time: ${estimated}",
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Text(
                                            text = "Focus:",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(bottom = 16.dp)
                                        ) {
                                            focused.forEach { benefit ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.PlayArrow,
                                                        contentDescription = "Benefit",
                                                        tint = Color(0xFF10B981),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Text(
                                                        text = benefit,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(start = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                    }else{
                                        val mealNameObs = vm.mealName.observeAsState()
                                        val mealName = mealNameObs.value
                                        val ingredientsObs = vm.ingredients.observeAsState()
                                        val ingredients = ingredientsObs.value?: emptyList()
                                        val caloriesObs = vm.calories.observeAsState()
                                        val calories = caloriesObs.value
                                        val fatObs = vm.fat.observeAsState()
                                        val fat = fatObs.value
                                        val proteinObs = vm.protein.observeAsState()
                                        val protein = proteinObs.value

                                        Text(
                                            text = "Meal: $mealName",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(8.dp),
                                            modifier = Modifier.padding(bottom = 16.dp)
                                        ) {
                                            ingredients.forEach { benefit ->
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Icon(
                                                        Icons.Default.Create,
                                                        contentDescription = "Ingredient",
                                                        tint = Color(0xFF10B981),
                                                        modifier = Modifier.size(20.dp)
                                                    )
                                                    Text(
                                                        text = benefit,
                                                        fontSize = 16.sp,
                                                        modifier = Modifier.padding(start = 8.dp)
                                                    )
                                                }
                                            }
                                        }
                                        Text(
                                            text = "Calories : $calories",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            text = "Protein : $protein",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                        )
                                        Text(
                                            text = "Fat : $fat",
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Normal,
                                            modifier = Modifier.padding(bottom = 8.dp)
                                        )
                                    }

                                }else{
                                    Text(
                                        text = "Completed!",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    )
                                }

                            }
                        }
                    }
                }
            }
}

}
