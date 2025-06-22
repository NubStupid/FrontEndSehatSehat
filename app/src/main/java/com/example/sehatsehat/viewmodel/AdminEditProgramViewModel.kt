package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.WorkoutEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EditProgramUiState(
    val program: ProgramEntity? = null,
    val exercisesCsv: String = "",        // all workout titles joined
    val mealsCsv: String = "",
    val estimatedTime: String = "",
    val focusedAt: String = "",
    val ingredients: String = "",
    val fat: String = "",
    val calories: String = "",
    val protein: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

class AdminEditProgramViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(EditProgramUiState())
    val uiState: StateFlow<EditProgramUiState> = _uiState.asStateFlow()

    fun loadProgram(id: String) {
        Log.d("EditViewModel", "Loading program with ID: $id")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val dro = SehatApplication.retrofitService.getProgramById(id)
                val wkDRO = SehatApplication.retrofitService.getWorkoutsByProgramId(id)
                val mlDRO = SehatApplication.retrofitService.getMealsByProgramId(id)
//                Log.d("EditViewModel", "Program loaded: ${dro.program}")

                val exercisesCsv = wkDRO.workouts
                    .joinToString(",") { it.workout_title }
                val mealsCsv = mlDRO.meals
                    .joinToString(",") { it.meal_name }

                val firstWorkout = wkDRO.workouts.firstOrNull()
                val estimatedTime = firstWorkout?.estimated_time?.toString() ?: ""
                val focusedAt = firstWorkout?.focused_at ?: ""

                val firstMeal = mlDRO.meals.firstOrNull()
                val ingredients = firstMeal?.ingredients ?: ""
                val fat = firstMeal?.fat?.toString() ?: ""
                val calories = firstMeal?.calories?.toString() ?: ""
                val protein = firstMeal?.protein?.toString() ?: ""

                Log.e("program", dro.program.toString())
                Log.e("meal", mlDRO.meals.toString())
                Log.e("workout", wkDRO.workouts.toString())

                if (dro.program != null || wkDRO != null || mlDRO != null) {
                    _uiState.value = _uiState.value.copy(
                        program = dro.program,
                        mealsCsv = mealsCsv,
                        exercisesCsv = exercisesCsv,
                        estimatedTime = estimatedTime,
                        focusedAt = focusedAt,
                        ingredients = ingredients,
                        fat = fat,
                        calories = calories,
                        protein = protein,
                        isLoading = false,
                        error = null
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        program = null,
                        isLoading = false,
                        error = "Program tidak ditemukan"
                    )
                }
            } catch (e: Exception) {
                Log.e("EditViewModel", "Error loading program", e)
                _uiState.value = _uiState.value.copy(
                    program = null,
                    error = e.localizedMessage ?: "Gagal memuat program",
                    isLoading = false
                )
            }
        }
    }

    fun updateProgram(
        id: String,
        program_name: String,
        pricing: Float,
        exercises: String,
        estimated_time: String,
        focused_at: String,
        meals: String,
        ingredients: String,
        fat: String,
        calories: String,
        protein: String,
        onSuccess: () -> Unit
    ) {
        Log.d("EditViewModel", "Updating program: ID=$id, name=$program_name, pricing=$pricing")
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val currentProgram = _uiState.value.program
            if (currentProgram == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Data program tidak tersedia"
                )
                return@launch
            }

            try {
                val updatedProgram = currentProgram.copy(
                    program_name = program_name,
                    pricing = pricing,
                    updatedAt = System.currentTimeMillis().toString()
                )
                SehatApplication.retrofitService.updateProgram(id, updatedProgram)

                exercises.split(",").map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .forEachIndexed { index, title ->
                        val workout = WorkoutEntity(
                            id = "WO${System.currentTimeMillis()}_$index",
                            workout_title = title,
                            estimated_time = estimated_time.toIntOrNull() ?: 0,
                            focused_at = focused_at,
                            program_id = updatedProgram.id,
                        )
                        SehatApplication.retrofitService.insertWorkout(workout)
                    }

                meals.split(",").map { it.trim() }
                    .filter { it.isNotEmpty() }
                    .forEachIndexed { index, name ->
                        val meal = MealEntity(
                            id = "ME${System.currentTimeMillis()}_$index",
                            meal_name = name,
                            ingredients = ingredients,
                            calories = calories.toFloatOrNull() ?: 0f,
                            fat = fat.toFloatOrNull() ?: 0f,
                            protein = protein.toFloatOrNull() ?: 0f,
                            program_id = updatedProgram.id
                        )
                        SehatApplication.retrofitService.insertMeal(meal)
                    }

                _uiState.value = EditProgramUiState(program = updatedProgram)
                Log.d("EditViewModel", "Program and related data updated successfully")
                onSuccess()
            } catch (e: Exception) {
                Log.e("EditViewModel", "Error updating program", e)
                _uiState.value = _uiState.value.copy(
                    error = e.localizedMessage ?: "Gagal memperbarui program",
                    isLoading = false
                )
            }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}