package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.model.UserPogramEntity
import kotlinx.coroutines.launch

class ViewProgramViewModel (
    val sehatRepository: SehatRepository
):ViewModel(){
    private val _programViewed = MutableLiveData<ProgramEntity?>()
    val programViewed: LiveData<ProgramEntity?>
        get() = _programViewed
    private val _programProgress = MutableLiveData<ProgramProgressEntity?>()
    val programProgress: LiveData<ProgramProgressEntity?>
        get() = _programProgress
    private val _userProgram = MutableLiveData<UserPogramEntity?>()
    val userProgram:LiveData<UserPogramEntity?>
        get() = _userProgram

    private val _progress = MutableLiveData<Float>()
    val progress:LiveData<Float>
        get() = _progress

    private val _isCompleted = MutableLiveData<Boolean>()
    val isCompleted: LiveData<Boolean>
        get() = _isCompleted

    private val _progressType = MutableLiveData<String>()
    val progressType:LiveData<String>
        get() = _progressType

    private val _workoutTitle = MutableLiveData<String>()
    val workoutTitle:LiveData<String>
        get() = _workoutTitle
    private val _estimatedTime = MutableLiveData<String>()
    val estimatedTime:LiveData<String>
        get() = _estimatedTime
    private val _focusedAt = MutableLiveData<List<String>>()
    val focusedAt:LiveData<List<String>>
        get() = _focusedAt

    private val _mealName = MutableLiveData<String>()
    val mealName:LiveData<String>
        get() = _mealName
    private val _ingredients = MutableLiveData<List<String>>()
    val ingredients:LiveData<List<String>>
        get() = _ingredients
    private val _calories = MutableLiveData<Int>()
    val calories:LiveData<Int>
        get() = _calories
    private val _fat = MutableLiveData<Int>()
    val fat:LiveData<Int>
        get() = _fat
    private val _protein = MutableLiveData<Int>()
    val protein:LiveData<Int>
        get() = _protein

    private val _uiProgram = MutableLiveData<FitnessProgram>()
    val uiProgram:LiveData<FitnessProgram>
        get() = _uiProgram
    private val _user = MutableLiveData<UserEntity>()
    val user:LiveData<UserEntity>
        get() = _user

    fun init(program: FitnessProgram, user:UserEntity){
        viewModelScope.launch {
            sehatRepository.mealSync()
            sehatRepository.workoutSync()
            _user.value = user
            _uiProgram.value = program

            val pE = sehatRepository.getProgramById(program.id)
            Log.d("pE","pE")
            if(pE != null){
                _programViewed.value = pE
                val upE = sehatRepository.getUserProgramByProgramId(pE.id)
            Log.d("upE","upE")
                if(upE != null){
                    _userProgram.value = upE
                    val ppE = sehatRepository.getProgramProgressById(upE.program_progress_id)
                    Log.d("ppE","ppE")
                    if(ppE != null){
                        Log.d("ss","ss")
                        _programProgress.value = ppE
                        val percentage = (ppE.progress_index.toFloat()/ppE.progress_list.split(",").size.toFloat())
//                        Log.d("p",percentage.toString())
//                        Log.d("p2",ppE.progress_index.toString())
                        Log.d("pp",percentage.toString())
                        _progress.value = percentage
                        if(percentage == 1f){
                            _isCompleted.value = true
                        }else{
                            _isCompleted.value = false
                            renderProgress(ppE)
                        }
                    }
                }
            }
        }
    }

    fun renderProgress(programProgress: ProgramProgressEntity){
        viewModelScope.launch {
            if(programProgress.progress_index != programProgress.progress_list.split(",").size){
                val progressToDo = programProgress.progress_list.split(",").get(programProgress.progress_index)
                if(progressToDo.contains("WO")){
                    _progressType.value = "Workout"
                    val workout = sehatRepository.getWorkoutById(progressToDo)
                    if(workout != null){
                        _workoutTitle.value = workout.workout_title
                        _estimatedTime.value = "${workout.estimated_time} minute(s)"
                        _focusedAt.value = workout.focused_at.split(",")
                    }
                }else{
                    _progressType.value = "Meal"
                    val meal = sehatRepository.getMealById(progressToDo)
                    if(meal != null){
                        _mealName.value = meal.meal_name
                        _ingredients.value = meal.ingredients.split(",")
                        _calories.value = meal.calories.toInt()
                        _fat.value = meal.fat.toInt()
                        _protein.value = meal.protein.toInt()
                    }
                }
            }
        }

    }
    fun nextProgress(progress_id:String,program: FitnessProgram, user:UserEntity){
        viewModelScope.launch {
            sehatRepository.incrementProgress(progress_id)
            init(program,user);
        }
    }
}