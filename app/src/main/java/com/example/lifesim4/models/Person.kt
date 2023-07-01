package com.example.lifesim4.models

import com.github.javafaker.Faker
import java.io.Serializable
import kotlin.random.Random

interface Character  : Serializable {
    // Define common properties and methods here
    val name: String
    var affectionType: AffectionType
    var age: Int
    var health: Int
    val gender: String
    var money: Long
    var fame: FameLevel
    var job: Job?
    var affection: Int
}

data class Person(
    override val name: String,
    override var age: Int = 0,
    override val gender: String,
    var title: String = "Baby",
    override var health: Int = Random.nextInt(65, 101),
    var healthChange: Int = 0,
    override var money: Long = 0,
    var moneyChange: Long = 0,
    var netWorth: Long = 0,
    var genius: Int = Random.nextInt(0, 101),
    var geniusChange: Int = 0,
    var charm: Int = Random.nextInt(0, 101),
    var charmChange: Int = 0,
    var fortune: Int = Random.nextInt(30, 100),
    override var fame: FameLevel = FameLevel.U,
    val cashFlow: Long = 0,
    var father: Person? = null,
    var mother: Person? = null,
    var sisters: MutableList<Person> = mutableListOf(),
    var brothers: MutableList<Person> = mutableListOf(),
    var assets: MutableList<Asset> = mutableListOf(),
    override var job: Job? = null,
    var educationLevel: String? = null,
    var relationshipStatus: String? = null,
    var children: MutableList<Person> = mutableListOf(),
    var resident: String? = null,
    var nationality: String? = null,
    var friends: MutableList<NPC> = mutableListOf(),
    var enemies: MutableList<NPC> = mutableListOf(),
    var lovers: MutableList<NPC> = mutableListOf(),
    override var affectionType: AffectionType,
    override var affection: Int
) : Character, Serializable

data class NPC(
    override val name: String,
    override var age: Int = 0,
    override val gender: String,
    override var health: Int = 100,
    var charm: Int = Random.nextInt(0, 101),
    var genius: Int = Random.nextInt(0, 101),
    override var money: Long = 0,
    override var fame: FameLevel = FameLevel.U,
    override var job: Job? = null,
    var relationshipStatus: String? = null,
    var residence: String? = null,
    var nationality: String? = null,
    override var affectionType: AffectionType,
    override var affection: Int
) : Character

// FameLevel.kt
enum class FameLevel(val multiplier: Double) : Serializable {
    U(1.0),
    D(1.5),
    C(2.5),
    B(4.0),
    A(7.0),
    A_PLUS(10.0)
}

// Job.kt
data class Job (
    val jobName: String,
    val salary: Double,
    val nextJob: Job?,
    val jobLevel: Int
)

sealed class Asset(
    val id: Int,
    val name: String,
    var value: Double,
    var condition: Int,
    var boughtFor: Long,
    val icon: Int,
    var state: AssetState
) : Serializable {
    init {
        require(id >= 0) { "ID must be non-negative" }
    }

    class House(
        id: Int,
        name: String,
        value: Double,
        condition: Int,
        boughtFor: Long,
        val squareFeet: Int,
        state: AssetState,
        icon: Int
    ) : Asset(id, name, value, condition, boughtFor, icon, state)

    class Car(
        id: Int,
        name: String,
        value: Double,
        condition: Int,
        boughtFor: Long,
        state: AssetState,
        val type: CarType,
        icon: Int
    ) : Asset(id, name, value, condition, boughtFor, icon, state)

    class Plane(id: Int, name: String, value: Double, condition: Int, boughtFor: Long, icon: Int, state: AssetState) :
        Asset(id, name, value, condition, boughtFor, icon, state)

    class Boat(id: Int, name: String, value: Double, condition: Int, boughtFor: Long, icon: Int, state: AssetState) :
        Asset(id, name, value, condition, boughtFor, icon, state)

    companion object {
        private var nextId = 0

        fun getNextId(): Int {
            return nextId++
        }
    }
}

enum class AssetState(val description: String) : Serializable {
    LIVING_IN("Living In"),
    RENTING_OUT("Renting Out"),
    VACANT("Vacant"),
    UNDER_CONSTRUCTION("Under Construction"),
    MARKET("For Sale"),
    PRIMARY("Primary Vehicle"),
    LEASED("Leased Vehicle"),
    FINANCE("Financed Vehicle"),
    OWNED("Owned Vehicle"),
    STOLEN("Stolen Vehicle"),
}

//possibly remove car types
enum class CarType(val description: String) : Serializable {
    NORMAL("Normal Car"),
    SPORTS("Sports Car"),
    HYPERCAR("Hypercar"),
    COLLECTABLE("Collectible Car"),
}

enum class AffectionType : Serializable {
    Friend,
    BestFriend,
    Enemy,
    Wife,
    Girlfriend,
    Father,
    Mother,
    Sibling,
    Child,
    Me
}

enum class PriceCategory : Serializable {
    CHEAP,
    MEDIUM,
    HIGH,
    LUXURY
}


