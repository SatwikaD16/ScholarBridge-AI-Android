package com.tribalscholar.app.data.model

data class UserSession(
    val isLoggedIn: Boolean = false,
    val studentId: String = "",
    val studentName: String = "",
    val mobileNumber: String = "",
    val lastLoginTime: String = ""
)
