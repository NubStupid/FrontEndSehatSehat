package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.WorkoutEntity

class AdminAddProgramViewModel : ViewModel() {
    var programName by mutableStateOf("")
        private set
    var pricing by mutableStateOf("")
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private var nextId by mutableStateOf(1)

    fun onProgramNameChange(name: String) {
        programName = name
    }

    fun onPricingChange(value: String) {
        pricing = value.filter { it.isDigit() || it == '.' }
    }

    private fun formatProgramId(number: Int): String {
        return "PR" + number.toString().padStart(5, '0')
    }


    fun submit(exerciseList: String, estimated_time: String, focused_at: String, mealList: String, ingredients: String, fat: String, calories: String, protein: String, onSuccess: () -> Unit) {
        val priceDouble = pricing.toDoubleOrNull()
        if (programName.isBlank() || priceDouble == null) {
            errorMessage = "Mohon isi nama program dan harga dengan benar"
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val response = SehatApplication.retrofitService.getAllPrograms()
                nextId = response.programs.size + 1
                val programId = formatProgramId(nextId)

                val timestamp = System.currentTimeMillis()

                Log.e("program id", programId)

                val newProgram = ProgramEntity(
                    id = programId,
                    program_name = programName,
                    pricing = priceDouble.toFloat(),
                    createdAt = timestamp.toString(),
                    updatedAt = timestamp.toString()
                )

                SehatApplication.retrofitService.insertProgram(newProgram)

                exerciseList.split(",").map { it.trim() }.forEachIndexed { index, title ->
                    val workout = WorkoutEntity(
                        id = "WO${System.currentTimeMillis()}$index",
                        workout_title = title,
                        estimated_time = estimated_time.toInt(),
                        focused_at = focused_at,
                        program_id = newProgram.id,
                        expert_username = "Sehat Sehat Instructor"
                    )
                    SehatApplication.retrofitService.insertWorkout(workout)
                }

                mealList.split(",").map { it.trim() }.forEachIndexed { index, name ->
                    val meal = MealEntity(
                        id = "ME${System.currentTimeMillis()}$index",
                        meal_name = name,
                        ingredients = ingredients,
                        calories = calories.toFloat(),
                        fat = fat.toFloat(),
                        protein = protein.toFloat(),
                        program_id = newProgram.id
                    )
                    SehatApplication.retrofitService.insertMeal(meal)
                }

                onSuccess()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Terjadi kesalahan"
            } finally {
                isLoading = false
            }
        }
    }
}
