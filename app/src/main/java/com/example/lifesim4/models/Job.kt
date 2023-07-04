package com.example.lifesim4.models

// Job.kt
data class Job (
    val jobName: String,
    val salary: Double,
    val nextJob: Job?,
    val jobLevel: Int
)