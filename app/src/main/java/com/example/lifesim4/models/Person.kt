package com.example.lifesim4.models

data class Person(
    var name: String,
    var age: Int = 0,
    var health: Int = 100,
    var money: Long = 0,
    var fame: FameLevel = FameLevel.U,
    var father: Person? = null,
    var mother: Person? = null,
    var sisters: List<Person> = emptyList(),
    var brothers: List<Person> = emptyList(),
    var assets: MutableList<String> = mutableListOf(),
    var job: Job? = null,
    var educationLevel: String? = null,
    var relationshipStatus: String? = null,
    var children: MutableList<Person> = mutableListOf(),
    var skills: MutableList<String> = mutableListOf(),
    var hobbies: MutableList<String> = mutableListOf(),
    var residence: String? = null,
    var nationality: String? = null,
    var currentCity: String? = null,
    var friends: MutableList<Person> = mutableListOf(),
    var enemies: MutableList<Person> = mutableListOf()
)

// FameLevel.kt
enum class FameLevel(val multiplier: Double) {
    U(1.0),
    D(1.5),
    C(2.5),
    B(4.0),
    A(7.0),
    A_PLUS(10.0)
}

// Job.kt
data class Job(
    val jobName: String,
    val salary: Double,
    val nextJob: Job?,
    val jobLevel: Int
)

