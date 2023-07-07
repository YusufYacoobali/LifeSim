package com.example.lifesim4.models

import java.io.Serializable

// Job.kt
sealed class Job(
    val id: Int,
    val name: String,
    val salary: Double,
    val type: JobType,
    val icon: Int,
) : Serializable {
    init {
        require(id >= 0) { "ID must be non-negative" }
    }

    class FullTimeJob(
        id: Int,
        name: String,
        salary: Double,
        type: JobType,
        icon: Int,
        val level: JobLevel
    ) : Job(id, name, salary, type, icon)

    class PartTimeJob(
        id: Int,
        name: String,
        salary: Double,
        type: JobType,
        icon: Int,
        val hoursPerWeek: Int
    ) : Job(id, name, salary, type, icon)

    class Entrepreneur(
        id: Int,
        name: String,
        salary: Double,
        type: JobType,
        icon: Int,
        val businessName: String
    ) : Job(id, name, salary, type, icon)

    class Government(
        id: Int,
        name: String,
        salary: Double,
        type: JobType,
        icon: Int,
        val department: String
    ) : Job(id, name, salary, type, icon)

    class Crime(
        id: Int,
        name: String,
        salary: Double,
        type: JobType,
        icon: Int,
        val illegalActivity: String
    ) : Job(id, name, salary, type, icon)

    companion object {
        private var nextId = 0

        fun getNextId(): Int {
            return nextId++
        }
    }
}

enum class JobType : Serializable {
    Astronaut,
    Programmer,
    Marketing,
    Finance,
    Teacher,
    Chef,
    Engineer,
    Doctor,
    Artist,
    Retail,
    Waiter,
    Barista,
    Tutor,
    Babysitter,
    DogWalker,
}

enum class JobLevel(val levelNumber: Int) : Serializable {
    Entry(1),
    Normal(2),
    Manager(3),     // Represents a managerial-level job
    Specialist(4),  // Represents a specialized-level job
    Senior(5),
    Director(6),
    Executive(7),
}