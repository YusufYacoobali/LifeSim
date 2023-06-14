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

    fun simulate() {
        // Perform game simulation logic here
        // Example: Update the age, money, and health of the current player
        currentPlayer.apply {
            age++
            money += 100
            health--
            //applycashflow() add salary to net worth and money
            //changeWorkStatus() check if student/baby/unemployed etc
            //applyStatResults() eg if going gym for full year
            //ageAssets() deteritoate asset conditions
            //randomEvents() cause random events with 40% chance of happing
        }
    }

    // Method to add a Person to the game world
    fun addPerson(person: Person) {
        persons.add(person)
    }

    fun goGym(){
        currentPlayer.apply {
            health += 5
            money -= 100
            charm += 1
        }
    }

    fun goDoctors(option: Int) {
        when (option) {
            1 -> {
                sendMessage("Visited best doctor")
                // Additional actions for option 1
                currentPlayer.money -= if (20000 > currentPlayer.money * 0.15) 20000 else (currentPlayer.money * 0.15).toLong()
                currentPlayer.health = 100
            }
            2 -> {
                sendMessage("Visited doctor")
                // Additional actions for option 2
            }
            3 -> {
                sendMessage("Visited witch")
                // Additional actions for option 3
            }
            4 -> {
                sendMessage("Made home potion")
                // Additional actions for option 4
            }
            5 -> {
                sendMessage("Got surgery")
                // Additional actions for option 5
            }
            else -> {
                sendMessage("Invalid option")
                // Additional actions for invalid options
            }
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

    fun formatMoney(amount: Long): String {
        val suffixes = listOf("", "K", "M", "B", "T", "Q", "Qu", "S")
        val suffixIndex = (Math.floor(Math.log10(amount.toDouble())) / 3).toInt()
        val shortValue = amount / Math.pow(10.0, (suffixIndex * 3).toDouble())
        val formattedValue = "%.2f".format(shortValue)
        return "$$formattedValue${suffixes[suffixIndex]}"
    }
}
