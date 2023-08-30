package com.sim.lifesim4.models

import android.content.Context
import com.example.lifesim4.R
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
                return gameEngine
            } catch (e: Exception) {
                // Handle any exceptions that may occur during loading
                e.printStackTrace()
            }
            return null
        }

    }

    fun saveGameEngineToFile(context: Context, fileName: String) {
        try {
            val file = File(context.filesDir, fileName)
            ObjectOutputStream(FileOutputStream(file)).use { fileOutputStream ->
                fileOutputStream.writeObject(this)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException("Failed to save game state to file: $fileName", e)
        }
    }

    fun simulate() {
        currentPlayer.age()
        sendMessage(Message("Age: ${currentPlayer.age} years", true))
        lifeChanges()
        currentPlayer.ageAssets() //deteritoate asset conditions
       // randomEvents() //cause random events with 40% chance of happing
        currentPlayer.calcNetWorth()
        checkLife()
    }

    private fun checkLife(){
        if (currentPlayer.isDead()){
            sendMessage(Message("You will now start as another person", false))
            sendMessage(Message("You Died!", false))
            startNew = true
        }
    }

    fun lifeChanges(){
        val newWork = currentPlayer.lifeChanges()
        if (newWork != ""){
            sendMessage(Message(newWork, false))
        }
    }

    fun randomEvents() {
        val chance = 0.4 // 40% chance

        if (random.nextDouble() < chance) {
            //sendMessage("Random event occurred")
        }
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
    }
}
