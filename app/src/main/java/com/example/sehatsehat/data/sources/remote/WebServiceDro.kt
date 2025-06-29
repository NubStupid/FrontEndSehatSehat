package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.model.UserPogramEntity
import com.example.sehatsehat.model.WorkoutEntity

data class ChatBotMessage(
    val response:String
)
data class ChatLogDRO(
    val status:Int,
    val message:String,
)
data class ChatLogFromGroupDRO(
    val status:Int,
    val chats:List<ChatLogEntity>
)

data class RegisterDRO(
    val message: String,
    val user: UserDRO? = null
)

data class LoginDRO(
    val message: String,
    val user: UserDRO? = null
)

data class UserDRO(
    val username: String,
    val display_name: String,
    val password: String,
    val dob: String,
    val role: String,
    val pp_url: String,
    val balance: Int = 0,
    val createdAt: String,
    val updatedAt: String
)

data class RegisterResponse(
    val message: String,
    val user: UserDRO? = null
)

data class ProgramDRO(
    val status: Int,
    val message: String? = null,
    val error: String? = null,
    val program: ProgramEntity? = null
)

/**
 * DRO untuk daftar Program. Misalnya respons GET /api/v1/programs.
 *
 * Contoh JSON respons:
 * {
 *   "status": 200,
 *   "programs": [
 *     { "id": "P001", "title": "Program A", "description": "Deskripsi A", "createdAt": 1686000000000 },
 *     { "id": "P002", "title": "Program B", "description": "Deskripsi B", "createdAt": 1686100000000 }
 *   ]
 * }
 */
data class ProgramListDRO(
    val status: Int,
    val programs: List<ProgramEntity>
)

data class UserProgramListDRO(
    val status:Int,
    val userPrograms:List<UserPogramEntity>
)

data class UserListDRO(
    val status: Int,
    val users: List<UserEntity>
)

data class DashboardDRO(
    val available:List<ProgramEntity>,
    val ongoing:List<ProgramEntity>,
    val completed:List<ProgramEntity>
)

data class ProgramProgressDRO(
    val status: Int,
    val progress:List<ProgramProgressEntity>
)
data class ProgramProgressSingleDRO(
    val status: Int,
    val progress:ProgramProgressEntity
)

data class MealListDRO(
    val status: Int,
    val meals: List<MealEntity>
)

data class WorkoutListDRO(
    val status: Int,
    val workouts: List<WorkoutEntity>
)

// Data class untuk request update role
data class UpdateRoleRequest(
    val role: String
)

// Data class untuk response update role
data class UpdateRoleResponse(
    val message: String,
    val success: Boolean
)

data class ArticleJSON(
    val author:String,
    val title:String,
    val description:String,
    val publishedAt:String,
    val content:String
)

data class ArticlesDRO(
    val response: List<ArticleJSON>
)

data class userTopUpResponse(
    val message: String,
    val success: Boolean
)

data class userUpdateProfileResponse(
    val message: String,
    val success: Boolean
)

data class TransactionDetails(
    val order_id:String,
    val gross_amount:Int
)

data class PaymentRequest(
    val transactionDetails: TransactionDetails,
    val acquirer: String = "gopay",
    val userId: String,          // ID user
    val programPrice: Int      // harga program
)

data class PaymentResponse(
    val status: Int,
    val payment_url: String?,
    val newBalance: Int?       // hanya untuk Balance
)