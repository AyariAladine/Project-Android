package com.example.bookproject.DataClass

data class SignUpRequest(
    val name : String,
    val email: String,
    val password: String
)

data class SignUpResponse(
    val success: Boolean,
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String
)

data class ForgotPasswordRequest(val email: String)
data class ForgotPasswordResponse(val message: String, val resetCode: String)

data class VerifyCodeRequest(val email: String ,val code: String)

data class VerifyCodeResponse(val message: String)

data class ResetPasswordRequest(
    val email: String,
    val code: String,
    val newPassword: String
)

