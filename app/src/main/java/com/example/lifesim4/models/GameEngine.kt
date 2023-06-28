package com.example.lifesim4.models

import com.example.lifesim4.R
import com.example.lifesim4.tools.Tools
import com.github.javafaker.Faker
import java.util.Random

class GameEngine private constructor() {

    var startNew: Boolean = false

    companion object {
        private var instance: GameEngine? = null
        private var messages: MutableList<String> = mutableListOf()
        private val everyone: MutableList<Character> = mutableListOf()
        private val assets: MutableList<Asset> = mutableListOf()
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

    private fun checkLife(){
        if (currentPlayer.health <= 0){
            sendMessage("You Died! You will now start as another person")
            startNew = true
        }
    }

    private fun applyStatsAndAge(){
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
        when (currentPlayer.age) {
            3 -> {
                currentPlayer.apply {
                    title = "Student"
                    healthChange += 1
                    geniusChange += 1
                }
                sendMessage("Nursery Started")
            }
            5 -> sendMessage("Primary School Started")
            11 -> sendMessage("Secondary School Started")
            18 -> currentPlayer.apply {
                healthChange -= 1
                geniusChange -= 1
            }
            22 -> {
                if (currentPlayer.job == null) {
                    currentPlayer.title = "Unemployed"
                }
            }
            40 -> currentPlayer.apply {
                healthChange -= 1
                healthChange -= 4 // testing
                geniusChange -= 1
                charmChange -= 1
            }
            60 -> currentPlayer.apply {
                healthChange -= 2
                geniusChange -= 1
                charmChange -= 3
            }
        }
        if (currentPlayer.job != null) {
            sendMessage(currentPlayer.title)
        }
    }

    fun ageAssets() {
        for (asset in currentPlayer.assets) {
            when (asset) {
                is Asset.House -> {
                    asset.value *= 1.02
                    asset.value *= ((asset.condition / 2) / 100.0 + 0.59)
                }
                is Asset.Car -> {
                    asset.value *= when (asset.type) {
                        CarType.NORMAL -> 0.97
                        CarType.SPORTS -> 0.95
                        CarType.HYPERCAR -> 1.02
                        CarType.COLLECTABLE -> 1.032
                    }
                }
                is Asset.Plane, is Asset.Boat -> {
                    asset.value *= 0.99
                }
            }
            asset.condition -= 2
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
        everyone.add(person)
    }

    fun addAssets(asset: Asset) {
        assets.add(asset)
    }

    fun buyAsset(asset: Asset){
        asset.boughtFor = asset.value.toLong()
        currentPlayer.money -= asset.value.toLong()
        currentPlayer.assets.add(asset)
        if (asset is Asset.House){
            asset.state = HouseState.VACANT
        }
        if (asset is Asset.Car){
            asset.state = CarState.OWNED
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
        //currentMessages.reverse() //works for dialog box but not for
        return currentMessages
    }


    // Method to get all persons in the game world
    fun getPerson(name: String): Character? {
        return everyone.find { it.name == name }
    }

    fun getAsset(id: String): Asset? {
        val idInt = id.toIntOrNull()
        return assets.find { it.id == idInt }
    }

    fun getPlayer(): Person {
        return currentPlayer
    }

    fun startGame() {
        // Create the current player and their family
        createFamily()
    }

    private fun processDoctorOption(message: String, minCharge: Long, thresholdHealth: Int, defaultHealth: Int, additional: Int, minChargeRate: Double, maxChargeRate: Double) {
        if (currentPlayer.money >= minCharge) {
            val newHealth = if (currentPlayer.health < thresholdHealth) random.nextInt(11) + additional else defaultHealth
            val change = newHealth - currentPlayer.health
            currentPlayer.health = newHealth
            val randomCharge = ((random.nextDouble() * (maxChargeRate - minChargeRate) + minChargeRate) * currentPlayer.money).toLong()
            currentPlayer.money -= randomCharge
            sendMessage("You visited the ${message}.\nHealth ${if (change >= 0) "+$change" else change} costing you\n ${Tools.formatMoney(randomCharge)}")
        } else {
            sendMessage("Minimum charge is $${minCharge}. You are broke and cannot afford this...lol")
        }
    }

    fun goDoctors(option: Int) {
        when (option) {
            1 -> {
                processDoctorOption("expensive Doctor",20000, 90, 100, 90,0.5, 0.3)
            }
            2 -> {
                processDoctorOption("GP",2000, 70, 88, 70,0.27, 0.19)
            }
            3 -> {
                processDoctorOption("witch",400, 99, 100, 50,0.8, 0.2)
//                val witchAttack = random.nextInt(10) - 5 // -2 to +2
//                if (witchAttack != 0) {
//                    currentPlayer.health += witchAttack
//                    val message = if (witchAttack > 0) {
//                        "The witches have helped!\n+$witchAttack health"
//                    } else {
//                        "The witches have cursed you!\n-${witchAttack} health."
//                    }
//                    sendMessage(message)
//                }
            }
            4 -> {
                processDoctorOption("medicine cupboard",0, 10, 30, 10,0.0, 0.1)
            }
            5 -> {
                //plastic surgery
                if (currentPlayer.money >= 20000)
                    currentPlayer.money -= (currentPlayer.money*0.12).toLong()
                currentPlayer.charm += 10
                sendMessage("You got that plastic. \nIt costed you $20k")
            }
            else -> {
                sendMessage("Invalid option")
            }
        }
    }

    private fun createFamily() {

        val lastName = faker.name().lastName()

        val father = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 40,
            gender = "Male",
            money = 1000,
            affection = 90,
            affectionType = AffectionType.Father

        )

        val mother = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 40,
            gender = "Female",
            money = 1000,
            affection = 90,
            affectionType = AffectionType.Mother
        )

        val child1 = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 0,
            money = 0,
            gender = "Male",
            fame = FameLevel.U,
            father = father,
            mother = mother,
            affection = 90,
            affectionType = AffectionType.Me
        )

        val child2 = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 7,
            gender = "Female",
            fame = FameLevel.A,
            father = father,
            mother = mother,
            affection = 90,
            affectionType = AffectionType.Sibling
        )

        val child3 = Person(
            name = "${maleFirstNames.random()} $lastName",
            age = 16,
            gender = "Male",
            fame = FameLevel.B,
            father = father,
            mother = mother,
            affection = 90,
            affectionType = AffectionType.Sibling
        )

        val grandchild = Person(
            name = "${femaleFirstNames.random()} $lastName",
            age = 2,
            gender = "Female",
            fame = FameLevel.C,
            father = child1,
            mother = mother,
            affection = 90,
            affectionType = AffectionType.Child
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

        everyone.addAll(listOf(father, mother, child1, child2, child3, grandchild, lover, gf, enemy, friend))

        val house1 = Asset.House(900,"My House", 250000.0, 80, 250000, 2200, HouseState.LIVING_IN, R.drawable.home_cheap_1)
        val house2 = Asset.House(901,"Casa Primero", 2500000.0, 100, 100000, 4000, HouseState.RENTING_OUT, R.drawable.home_luxury_1)
        val house3 = Asset.House(902,"Casa Cinco", 280000.0, 59, 30, 3600, HouseState.VACANT, R.drawable.home_medium_1)
        val car = Asset.Car(903,"My Car", 30000.0, 9, 29000, CarState.PRIMARY, CarType.NORMAL, R.drawable.buy_car)
        val car2 = Asset.Car(904,"Rover", 2000.0, 19, 4000, CarState.STOLEN, CarType.SPORTS, R.drawable.buy_car)
        val boat = Asset.Boat(905,"My Yacth", 3000000.0, 9, 200000, R.drawable.buy_boat)
        val plane = Asset.Plane(906,"My Jet", 5000000.0, 9, 10000000, R.drawable.buy_planes)
        currentPlayer.assets.addAll(listOf(house1,house2,house3,car,car2,boat,plane))
        assets.addAll(listOf(house2,house3,house1,car,car2,boat,plane))
    }
}
