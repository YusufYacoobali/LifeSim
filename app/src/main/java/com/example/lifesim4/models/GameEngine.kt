package com.example.lifesim4.models

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.lifesim4.R
import com.example.lifesim4.tools.Tools
import com.github.javafaker.Faker
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.util.Random

class GameEngine private constructor() : Serializable {

    data class Message(val message: String, val isAgeText: Boolean) : Serializable

    var startNew: Boolean = false
    private lateinit var currentPlayer: Person
    private var messages: MutableList<Message> = mutableListOf()
    var allMessage: MutableList<Message> = mutableListOf()
    private val everyone: MutableList<Character> = mutableListOf()
    val assets: MutableList<Asset> = mutableListOf()
    companion object {
        private var instance: GameEngine? = null
        val random = Random()
        val faker = Faker()
        val femaleFirstNames = listOf(
            "Emma", "Olivia", "Ava", "Sophia", "Isabella", "Mia", "Charlotte", "Amelia",
            "Harper", "Evelyn", "Abigail", "Emily", "Liz", "Mila", "Ella", "Avery",
            "Sofia", "Camila", "Aria", "Scarlett", "Victoria", "Madison", "Luna", "Grace",
            "Chloe", "Penelope", "Layla", "Riley", "Zoey", "Nora", "Lily", "Eleanor",
            "Hannah", "Lillian", "Addison", "Aubrey", "Ellie", "Stella", "Natalie", "Zoe"
        )

        val maleFirstNames = listOf(
            "Liam", "Noah", "William", "James", "Oliver", "Benja", "Elijah", "Lucas",
            "Mason", "Logan", "Alex", "Henry", "Jacob", "Michael", "Daniel", "Jackson",
            "Seb", "Aiden", "Matthew", "Samuel", "David", "Joseph", "Carter", "Owen",
            "Wyatt", "John", "Jack", "Luke", "Jayden", "Dylan", "Gray", "Levi", "Isaac",
            "Gab", "Julian", "Mateo", "Anthony", "Jaxon", "Lincoln", "Joshua", "Chris"
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

        fun loadGameEngineFromFile(context: Context, fileName: String): GameEngine? {
            try {
                val file = File(context.filesDir, fileName)
                val fileInputStream = FileInputStream(file)
                val objectInputStream = ObjectInputStream(fileInputStream)
                val gameEngine = objectInputStream.readObject() as GameEngine
                objectInputStream.close()
                fileInputStream.close()
                println("This is field " + gameEngine.getPlayer().name)
                instance = gameEngine

                //currentPlayer = saveData.person
                //println(saveData.person.name)
                return gameEngine
            } catch (e: Exception) {
                // Handle any exceptions that may occur during loading
                e.printStackTrace()
            }
            return null
        }

    }

    //data class SaveData(val gameEngine: GameEngine, val person: Person) : Serializable

    fun saveGameEngineToFile(context: Context, fileName: String) {
        try {
            val file = File(context.filesDir, fileName)
            //val saveData = SaveData(gameEngine, person)

            ObjectOutputStream(FileOutputStream(file)).use { fileOutputStream ->
                fileOutputStream.writeObject(this)
            }
            println("GAME SAVED")
            //println(person.name)
            //currentPlayer = person
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Failed to save game state to file: $fileName", e)
        }
    }


    fun simulate() {
        applyStatsAndAge()
        sendMessage(Message("Age: ${currentPlayer.age} years", true))
        lifeChanges()  //check if student/baby/unemployed etc
        ageAssets() //deteritoate asset conditions
       // randomEvents() //cause random events with 40% chance of happing
        calcNetWorth()
        checkLife()
    }

    private fun checkLife(){
        if (currentPlayer.health <= 0){
            sendMessage(Message("You Died! You will now start as another person", false))
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
                sendMessage(Message("Nursery Started", false))
            }
            5 -> sendMessage(Message("Primary School Started", false))
            11 -> sendMessage(Message("Secondary School Started", false))
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
            //sendMessage(currentPlayer.title)
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
            //sendMessage("Random event occurred")
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
        asset.state = AssetState.OWNED
    }

    fun goGym(){
        currentPlayer.apply {
            health += 5
            money -= 100
            charm += 1
        }
    }

    fun sendMessage(message: Message){
        messages.add(message)
        allMessage.add(message)
    }

    fun getAllMessages(): MutableList<Message> {
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
        for (asset in assets)
            println("ALL: asset id: ${asset.id} and name ${asset.name}.")
        val idInt = id.toIntOrNull()
        return assets.find { it.id == idInt }
    }

    fun getPlayer(): Person {
        return currentPlayer
    }

    fun setPlayer(player: Person) {
        currentPlayer = player
    }

    fun startGame() {
        allMessage.clear()
        everyone.clear()
        assets.clear()
        createFamily()
    }

    private fun processDoctorOption(message: String, minCharge: Long, thresholdHealth: Int, defaultHealth: Int, additional: Int, minChargeRate: Double, maxChargeRate: Double) {
        if (currentPlayer.money >= minCharge) {
            val newHealth = if (currentPlayer.health < thresholdHealth) random.nextInt(11) + additional else defaultHealth
            val change = newHealth - currentPlayer.health
            currentPlayer.health = newHealth
            val randomCharge = ((random.nextDouble() * (maxChargeRate - minChargeRate) + minChargeRate) * currentPlayer.money).toLong()
            currentPlayer.money -= randomCharge
            sendMessage(Message("You visited the ${message}.\nHealth ${if (change >= 0) "+$change" else change} costing you\n ${Tools.formatMoney(randomCharge)}", false))
            currentPlayer.doctorOptions(1)
        } else {
            sendMessage(Message("Minimum charge is $${minCharge}. You are broke and cannot afford this...lol", false))
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
                sendMessage(Message("You got that plastic. \nIt costed you $20k", false))
            }
            else -> {
                //sendMessage("Invalid option")
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

        val house1 = Asset.House(900,"My House", 250000.0, 80, 250000, 2200, AssetState.LIVING_IN, R.drawable.home_cheap_1)
        val car = Asset.Car(903,"My Car", 30000.0, 9, 29000, AssetState.PRIMARY, CarType.NORMAL, R.drawable.buy_car)
        val boat = Asset.Boat(905,"My Yacth", 3000000.0, 9, 200000, R.drawable.buy_boat, AssetState.OWNED)
        val plane = Asset.Plane(906,"My Jet", 5000000.0, 9, 10000000, R.drawable.buy_planes, AssetState.OWNED)
        currentPlayer.assets.addAll(listOf(house1,car,boat,plane))
        assets.addAll(listOf(house1,car,boat,plane))

        sendMessage(Message("Age: ${currentPlayer.age} years", true))
        sendMessage(Message("You are born as a ${currentPlayer.gender}", false))
        sendMessage(Message("Your name is ${currentPlayer.name}", false))
        //currentPlayer.age++
    }
}
