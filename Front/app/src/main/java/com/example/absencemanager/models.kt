package com.example.absencemanager

import com.google.gson.annotations.SerializedName

class models {

    data class Classroom(
        val id: Int,
        val name: String,
        val student_count: Int,
    )

    data class Student(
        val id: Int,
        val first_name: String,
        val last_name: String
    )

    data class Absence(
        val student: Student,
        val classroom: Classroom,
        val date: String,
        val reason: String
    )

    data class Motivation(
        val student: Student,
        val classroom: Classroom,
        val date: String,
        val level: String
    )
    data class LoginRequest(
        val username: String,
        val password: String
    )

    data class LoginResponse(
        val message: String,
        val username: String,
        val error: String?,
        val user_id: Int  // Ajouter l'ID utilisateur
    )
    data class TokenResponse(
        val access: String,
        val refresh: String
    )
    data class DashboardData(
        val attendanceRate: Int,
        @SerializedName("classes_count") val classesCount: Int,
        @SerializedName("students_count") val studentsCount: Int
    )

    data class DetectionResult(
        val success: Boolean,
        val detectedStudents: List<String>
    )

}
