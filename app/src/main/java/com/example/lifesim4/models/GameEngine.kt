package com.example.lifesim4.models

class GameEngine private constructor() {

    companion object {
        private var instance: GameEngine? = null
        private var messages: MutableList<String> = mutableListOf()
        private val persons: MutableList<Person> = mutableListOf()
        private lateinit var currentPlayer: Person

        fun getInstance(): GameEngine {
            if (instance == null) {
                synchronized(GameEngine::class.java) {
                    if (instance == null) {
                        instance = GameEngine()
                    }
                }
            }
            return instance!!
        }
    }

    // Method to add a Person to the game world
    fun addPerson(person: Person) {
        persons.add(person)
    }

    // Method to simulate the game world
    fun simulate() {
        // Perform game simulation logic here
        // Example: Update the age, money, and health of the current player
        currentPlayer.apply {
            age++
            money += 100
            health--
        }
    }

    fun goGym(){
        currentPlayer.apply {
            health += 5
            money -= 100
            charm += 1
        }
    }

    fun sendMessage(message: String){
        messages.add(message)
    }

    fun getAllMessages(): MutableList<String> {
        return messages
    }

    // Method to get all persons in the game world
    fun getAllPersons(): List<Person> {
        return persons.toList()
    }

    fun getPlayer(): Person {
        return currentPlayer
    }

    fun startGame() {
        // Create the current player and their family
        createFamily()
    }

    private fun createFamily() {

        val father = Person(
            name = "Father Doe",
            age = 40,
            gender = "Male",
            health = 100,
            money = 1000
        )

        val mother = Person(
            name = "Mother Doe",
            age = 40,
            gender = "Female",
            health = 100,
            money = 1000
        )

        val child1 = Person(
            name = "Bill Snchobal",
            age = 0,
            health = 80,
            money = 0,
            gender = "Male",
            genius = 67,
            charm = 43,
            fame = FameLevel.U,
            father = father,
            mother = mother
        )

        val child2 = Person(
            name = "Sister Doe",
            age = 10,
            gender = "Female",
            health = 100,
            money = 0,
            fame = FameLevel.B,
            father = father
        )

        father.children = mutableListOf(child1, child2)
        mother.children = mutableListOf(child1, child2)
        currentPlayer = child1

        persons.addAll(listOf(father, mother, child1, child2))
    }
}
