package com.example.lifesim4.models

import com.github.javafaker.Faker
import kotlin.random.Random

data class Person(
    var name: String,
    var age: Int = 0,
    val gender: String,
    var health: Int = Random.nextInt(65, 101),
    var money: Long = 0,
    var netWorth: Long = 0,
    var genius: Int = Random.nextInt(0, 101),
    var charm: Int = Random.nextInt(0, 101),
    var fortune: Int = Random.nextInt(30, 100),
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
    var resident: String? = null,
    var nationality: String? = null,
    var friends: MutableList<Person> = mutableListOf(),
    var enemies: MutableList<Person> = mutableListOf()
)

data class NPC(
    var name: String,
    var age: Int = 0,
    val gender: String,
    var health: Int = 100,
    var money: Long = 0,
    var fame: FameLevel = FameLevel.U,
    var job: Job? = null,
    var relationshipStatus: String? = null,
    var residence: String? = null,
    var nationality: String? = null,
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

