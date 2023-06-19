package com.example.lifesim4.models

import com.github.javafaker.Faker
import java.util.Random

class GameEngine private constructor() {

    var startNew: Boolean = false

    companion object {
        private var instance: GameEngine? = null
        private var messages: MutableList<String> = mutableListOf()
        private val persons: MutableList<Person> = mutableListOf()
        private lateinit var currentPlayer: Person
        val random = Random()
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
        applyStatsAndAge()
        lifeChanges()  //check if student/baby/unemployed etc
        ageAssets() //deteritoate asset conditions
       // randomEvents() //cause random events with 40% chance of happing
        calcNetWorth()
        checkLife()
    }

    fun checkLife(){
        if (currentPlayer.health <= 0){
            sendMessage("You Died!")
            startNew = true
        }
    }

    fun applyStatsAndAge(){
        currentPlayer.apply {
            age++
            health = (health + healthChange).coerceAtMost(100)
            charm = (charm + charmChange).coerceAtMost(100)
            genius = (genius + geniusChange).coerceAtMost(100)
            money += moneyChange
            money += 100000 //testing
        }
    }

    fun lifeChanges(){
        if (currentPlayer.age == 3) {
            currentPlayer.apply {
                title = "Student"
                healthChange += 1
                geniusChange += 1
            }
            sendMessage("Nursery Started")
        } else if (currentPlayer.age == 5) {
            sendMessage("Primary School Started")
        } else if (currentPlayer.age == 11) {
            sendMessage("Secondary School Started")
        } else if (currentPlayer.age == 18){
            currentPlayer.apply {
                healthChange -= 1
                geniusChange -= 1
            }
        } else if (currentPlayer.age == 22 && currentPlayer.job == null) {
            currentPlayer.title = "Unemployed"
        } else if (currentPlayer.job != null) {
            sendMessage(currentPlayer.title)
        } else if (currentPlayer.age == 40){
            currentPlayer.apply {
                healthChange -= 1
                healthChange -= 4 //testing
                geniusChange -= 1
                charmChange -= 1
            }
        } else if (currentPlayer.age == 60){
            currentPlayer.apply {
                healthChange -= 2
                geniusChange -= 1
                charmChange -= 3
            }
        }
    }

    fun ageAssets(){
        for (asset in currentPlayer.assets){
            //actions to deterioate and pay rent etc
        }
    }

    fun randomEvents() {
        val chance = 0.4 // 40% chance

        if (random.nextDouble() < chance) {
            sendMessage("Random event occurred")
        }
    }

    fun calcNetWorth(){
        var businessTotal = 0
        val assetTotal = currentPlayer.assets.sumOf { asset -> asset.value }.toLong()
        currentPlayer.netWorth = currentPlayer.money + assetTotal + businessTotal
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

    private fun processDoctorOption(minCharge: Long, thresholdHealth: Int, defaultHealth: Int, additional: Int, minChargeRate: Double, maxChargeRate: Double) {
        if (currentPlayer.money > minCharge) {
            currentPlayer.health = if (currentPlayer.health < thresholdHealth) random.nextInt(11) + additional else defaultHealth
            val randomCharge = ((random.nextDouble() * (maxChargeRate - minChargeRate) + minChargeRate) * currentPlayer.money).toLong()
            currentPlayer.money -= randomCharge
            sendMessage("Nice! Your health is now ${currentPlayer.health}. This costed you ${formatMoney(randomCharge)}")
        } else {
            sendMessage("Minimum charge is $${minCharge}. You are broke and cannot afford this...lol")
        }
    }


    fun goDoctors(option: Int) {
        when (option) {
            1 -> {
                processDoctorOption(20000, 90, 100, 90,0.5, 0.3)
            }
            2 -> {
                processDoctorOption(2000, 70, 88, 70,0.27, 0.19)
            }
            3 -> {
                processDoctorOption(400, 99, 100, 50,0.8, 0.2)
            }
            4 -> {
                processDoctorOption(0, 10, 30, 10,0.0, 0.1)
            }
            5 -> {
                //plastic surgery
                currentPlayer.money -= 20000
                currentPlayer.charm += 10
                sendMessage("You got that plastic. It costed you $20k")
            }
            else -> {
                sendMessage("Invalid option")
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

        val child3 = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 10,
            gender = "Male",
            fame = FameLevel.C,
            father = father,
            mother = mother
        )

        val grandchild = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            father = child1,
            mother = mother
        )


// Create a Lover instance
        val lover = NPC(
            name = "${femaleFirstNames.random()} ${maleFirstNames.random()}",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            affection = 90,
            affectionType = AffectionType.Wife
        )
        val gf = NPC(
            name = "${femaleFirstNames.random()} ${maleFirstNames.random()}",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            affection = 90,
            affectionType = AffectionType.Girlfriend
        )

        val enemy = NPC(
            name = "${femaleFirstNames.random()} ${faker.name().lastName()}",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            affection = 90,
            affectionType = AffectionType.Enemy
        )
        val friend = NPC(
            name = "${femaleFirstNames.random()} ${faker.name().lastName()}",
            age = 10,
            gender = "Female",
            fame = FameLevel.C,
            affection = 90,
            affectionType = AffectionType.Friend
        )

        father.children = mutableListOf(child1, child2)
        mother.children = mutableListOf(child1, child2)
        child1.sisters.add(child2)
        child1.brothers.add(child3)
        child1.children.add(grandchild)
        child1.lovers.add(lover)
        child1.lovers.add(gf)
        child1.enemies.add(enemy)
        child1.friends.add(friend)
        currentPlayer = child1

        persons.addAll(listOf(father, mother, child1, child2))

        val house1 = Asset.House("My House", 250000.0, 80, 2200, HouseState.LIVING_IN)
        val house2 = Asset.House("Casa Primero", 2500000.0, 100, 4000, HouseState.RENTING_OUT)
        val house3 = Asset.House("Casa Cinco", 280000.0, 59, 3600, HouseState.VACANT)
        val car = Asset.Car("My Car", 30000.0, 9, CarState.PRIMARY)
        val car2 = Asset.Car("Rover", 2000.0, 19, CarState.STOLEN)
        val boat = Asset.Boat("My Yacth", 3000000.0, 9)
        val plane = Asset.Plane("My Jet", 5000000.0, 9)
        currentPlayer.assets.addAll(listOf(house1,house2,house3,car,car2,boat,plane))
    }

    private fun formatMoney(amount: Long): String {
        val suffixes = listOf("", "K", "M", "B", "T", "Q", "Qu", "S")
        val suffixIndex = (Math.max(0, Math.floor(Math.log10(amount.toDouble()) / 3).toInt())).coerceAtMost(suffixes.size - 1)
        val shortValue = amount / Math.pow(10.0, (suffixIndex * 3).toDouble())
        val formattedValue = "%.2f".format(shortValue)
        return "$$formattedValue${suffixes[suffixIndex]}"
    }
}
