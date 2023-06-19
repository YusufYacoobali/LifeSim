package com.example.lifesim4.models

import com.github.javafaker.Faker
import kotlin.random.Random

data class Person(
    var name: String,
    var age: Int = 0,
    val gender: String,
    var title: String = "Baby",
    var health: Int = Random.nextInt(65, 101),
    var healthChange: Int = 0,
    var money: Long = 0,
    var moneyChange: Long = 0,
    var netWorth: Long = 0,
    var genius: Int = Random.nextInt(0, 101),
    var geniusChange: Int = 0,
    var charm: Int = Random.nextInt(0, 101),
    var charmChange: Int = 0,
    var fortune: Int = Random.nextInt(30, 100),
    var fame: FameLevel = FameLevel.U,
    val cashFlow: Long = 0,
    var father: Person? = null,
    var mother: Person? = null,
    var sisters: MutableList<Person> = mutableListOf(),
    var brothers: MutableList<Person> = mutableListOf(),
    var assets: MutableList<Asset> = mutableListOf(),
    var job: Job? = null,
    var educationLevel: String? = null,
    var relationshipStatus: String? = null,
    var children: MutableList<Person> = mutableListOf(),
    var resident: String? = null,
    var nationality: String? = null,
    var friends: MutableList<NPC> = mutableListOf(),
    var enemies: MutableList<NPC> = mutableListOf(),
    var lovers: MutableList<NPC> = mutableListOf()
)

data class NPC(
    var name: String,
    var age: Int = 0,
    val gender: String,
    var health: Int = 100,
    var charm: Int = Random.nextInt(0, 101),
    var genius: Int = Random.nextInt(0, 101),
    var money: Long = 0,
    var fame: FameLevel = FameLevel.U,
    var job: Job? = null,
    var relationshipStatus: String? = null,
    var residence: String? = null,
    var nationality: String? = null,
    var affectionType: AffectionType,
    var affection: Int
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

sealed class Asset(val name: String, val value: Double, val condition: Int) {
    class House(name: String, value: Double, condition: Int, val squareFeet: Int, var state: HouseState) :
        Asset(name, value, condition)
    class Car(name: String, value: Double, condition: Int, var state: CarState) : Asset(name, value, condition)
    class Plane(name: String, value: Double, condition: Int) : Asset(name, value, condition)
    class Boat(name: String, value: Double, condition: Int) : Asset(name, value, condition)
}

enum class HouseState {
    LIVING_IN,
    RENTING_OUT,
    VACANT,
    UNDER_CONSTRUCTION,
}

enum class CarState {
    PRIMARY,
    RENTING,
    FINANCE,
    STOLEN,
}

enum class AffectionType {
    Friend,
    BestFriend,
    Enemy,
    Wife,
    Girlfriend,
}


