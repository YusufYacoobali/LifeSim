package com.example.lifesim4.models

import com.github.javafaker.Faker

class GameEngine private constructor() {

    companion object {
        private var instance: GameEngine? = null
        private var messages: MutableList<String> = mutableListOf()
        private val persons: MutableList<Person> = mutableListOf()
        private lateinit var currentPlayer: Person
        val faker = Faker()
        val femaleFirstNames = listOf(
            "Emma", "Olivia", "Ava", "Sophia", "Isabella", "Mia", "Charlotte", "Amelia",
            "Harper", "Evelyn", "Abigail", "Emily", "Elizabeth", "Mila", "Ella", "Avery",
            "Sofia", "Camila", "Aria", "Scarlett", "Victoria", "Madison", "Luna", "Grace",
            "Chloe", "Penelope", "Layla", "Riley", "Zoey", "Nora", "Lily", "Eleanor",
            "Hannah", "Lillian", "Addison", "Aubrey", "Ellie", "Stella", "Natalie", "Zoe"
        )

        val maleFirstNames = listOf(
            "Liam", "Noah", "William", "James", "Oliver", "Benjamin", "Elijah", "Lucas",
            "Mason", "Logan", "Alexander", "Henry", "Jacob", "Michael", "Daniel", "Jackson",
            "Sebastian", "Aiden", "Matthew", "Samuel", "David", "Joseph", "Carter", "Owen",
            "Wyatt", "John", "Jack", "Luke", "Jayden", "Dylan", "Grayson", "Levi", "Isaac",
            "Gabriel", "Julian", "Mateo", "Anthony", "Jaxon", "Lincoln", "Joshua", "Christopher"
        )

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
        val currentMessages = messages.toMutableList()
        messages.clear()
        return currentMessages
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

        val lastName = faker.name().lastName()

        val father = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 40,
            gender = "Male",
            money = 1000
        )

        val mother = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 40,
            gender = "Female",
            money = 1000
        )

        val child1 = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 0,
            money = 0,
            gender = "Male",
            fame = FameLevel.U,
            father = father,
            mother = mother
        )

        val child2 = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            father = father,
            mother = mother
        )

        father.children = mutableListOf(child1, child2)
        mother.children = mutableListOf(child1, child2)
        currentPlayer = child1

        persons.addAll(listOf(father, mother, child1, child2))
    }
}
