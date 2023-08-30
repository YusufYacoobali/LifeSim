package com.sim.lifesim4.models

import java.io.Serializable

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

enum class PriceCategory : Serializable {
    CHEAP,
    MEDIUM,
    HIGH,
    LUXURY
}
