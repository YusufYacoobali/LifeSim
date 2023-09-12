package com.sim.lifesim4.models

import java.io.Serializable
import kotlin.random.Random

interface Character  : Serializable {
    // Define common properties and methods here
    val name: String
    var affectionType: AffectionType
    var age: Int
    var health: Int
    val gender: Boolean
    var money: Long
    var fame: FameLevel
    var job: Job?
    var affection: Int
    var isAlive: Boolean
}

data class Person(
    override val name: String,
    override var age: Int = 0,
    override val gender: Boolean,
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
    var fortuneChange: Int = 0,
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
    override var affection: Int,
    override var isAlive: Boolean = true,
    var jobLevelHistory: MutableMap<JobType, Pair<JobLevel, Int>> = mutableMapOf(),
    var followers: Long = 0
) : Character, Serializable {

    fun age(){
        age++
        health = (health + healthChange).coerceAtMost(100)
        charm = (charm + charmChange).coerceAtMost(100)
        genius = (genius + geniusChange).coerceAtMost(100)
        money += moneyChange
        //money += 100000 //testing
        addWorkHistory()
    }

    fun lifeChanges(): String{
        when (age) {
            3 -> {
                title = "Student"
                healthChange += 1
                geniusChange += 1
                return "Nursery Started"
                //sendMessage(GameEngine.Message("Nursery Started", false))
            }
            5 -> return "Primary School Started"
            11 -> return "Secondary School Started"
            18 ->  {
                healthChange -= 1
                geniusChange -= 1
                return ""
            }
            22 -> {
                if (job == null) {
                    title = "Unemployed"
                }
                return ""
            }
            40 ->  {
                healthChange -= 1
                healthChange -= 4 // testing
                geniusChange -= 1
                charmChange -= 1
                return ""
            }
            60 ->  {
                healthChange -= 2
                geniusChange -= 1
                charmChange -= 3
                return ""
            }
            else -> return ""
        }
    }

    fun ageAssets() {
        for (asset in assets) {
            when (asset) {
                is Asset.House -> {
                    asset.value *= 1.04
                    //asset.value *= ((asset.condition / 2) / 100.0 + 0.59)
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

    fun calcNetWorth(){
        var businessTotal = 0
        val assetTotal = assets.sumOf { asset -> asset.value }.toLong()
        netWorth = money + assetTotal + businessTotal
    }

    fun isEligibleForJob(job: Job.FullTimeJob): Boolean {
        return when (job.level) {
            JobLevel.Entry -> true
            else -> {
                val industryExperience = jobLevelHistory[job.type]
                industryExperience?.let { (level, years) ->
                    when {
                        level.levelNumber + 1 == job.level.levelNumber -> years >= 2
                        level.levelNumber + 1 >= job.level.levelNumber -> true
                        else -> false
                    }
                } ?: false
            }
        }
    }

    fun startJob(newJob: Job){
        if (job != null){
            moneyChange -= job!!.salary.toLong()
        }
        job = newJob
        title = newJob.name

        if (newJob is Job.FullTimeJob){
            job = newJob
            //check if exists before overwriting to 0
            val currentExperience = jobLevelHistory[newJob.type]
            //if new job is higher level than current/highest then replace as highest
            if (currentExperience == null || newJob.level > currentExperience.first) {
                jobLevelHistory[newJob.type] = Pair(newJob.level, 0)
            }
            moneyChange += newJob.salary.toLong()

        } else if (newJob is Job.PartTimeJob) {
            //yearly salary
            moneyChange += (newJob.salary * newJob.hoursPerWeek * 52).toLong()
        }
    }

    fun addWorkHistory() {
        if (job != null && job is Job.FullTimeJob) {
            val currentExperience = jobLevelHistory[(job as Job.FullTimeJob).type]
            if (currentExperience != null && (job as Job.FullTimeJob).level == currentExperience.first) {
                val (level, years) = currentExperience
                jobLevelHistory[(job as Job.FullTimeJob).type] = Pair(level, years + 1)
            }
        }
    }

    fun failedCrime(){
        job = null
        title = "Prisoner"
        //fine them too
    }

    fun didCrime(crime: Job.Crime){
        //succeed in crime
        money+= crime.salary.toLong()
        fame = FameLevel.A
    }

    fun isDead(): Boolean{
        return if (health <= 0){
            isAlive = false
            true
        } else false
    }

    fun emigrate() : Pair<Int, Long>{
        return if (money >= 5000) {
            val charge = Random.nextInt(5000, 10000)
            money -= charge
            return 0 to charge.toLong()
        } else 0 to -1L
    }

    private fun doctorProcess(thresholdHealth: Int, maximumHealth: Int, maxCost: Double, minCost: Double): Pair<Int, Long> {
        var newHealth = if (health < thresholdHealth) {
            Random.nextInt(thresholdHealth, maximumHealth)
        } else {
            maximumHealth
        }
        val change = newHealth - health
        val randomCharge =
            ((GameEngine.random.nextDouble() * (maxCost - minCost) + minCost) * money).toLong()
        health = newHealth
        money -= randomCharge
        return change to randomCharge
    }


    fun  doctorOptions(option: Int) : Pair<Int, Long> {
        when (option){
            1 -> {
                return if (money > 80000) {
                    doctorProcess(90, 100, 0.5,0.3)
                } else 0 to -1L
            }
            2 -> {
                return if (money > 10000) {
                    doctorProcess(70, Random.nextInt(70,88), 0.22,0.1)
                } else 0 to -1L
            }
            3 -> {
                //calcs need fixing
                return if (money > 400) {
                    val witchAttack = Random.nextInt(health-20,health+20)
                    val change = witchAttack - health
                    health = witchAttack
                    money -= 400
                    return change to 400
                } else 0 to -1L
            }
            4 -> {
                val newHealth = Random.nextInt(1,100)
                val change = newHealth - health
                health = newHealth
                return change to 0
            }
            5 -> {
                //plastic surgery
                return if (money >= 20000){
                    var charge = (money*0.12).toLong()
                    var newCharm = Random.nextInt(40,101)
                    val change = newCharm - charm
                    money -= charge
                    charm = newCharm
                    change to charge
                } else {
                    0 to -1L
                }
                //sendMessage(GameEngine.Message("You got that plastic. \nIt costed you $20k", false))
            }
            else -> {
                return 0 to 0L
            }
        }
    }

    private fun fitnessProcess(charge: Long, toughness: Int): Triple<Int, Int, Long> {
        var charmChange = 0
        if (charm < 96){
            charmChange = toughness
        }
        var vitalityChange = 0
        if (health < 96){
            vitalityChange = Random.nextInt(0, 2)
        }

        health += vitalityChange
        charm += charmChange
        money -= charge
        return Triple(vitalityChange, charmChange, charge) //change to randomCharge
    }

    fun  fitnessOptions() : Triple<Int, Int, Long> {
        return if (money > 10) {
            fitnessProcess(10L,  Random.nextInt(0, 4))
        } else Triple(0,0,-1L)
    }

    private fun partyProcess(charge: Long): Pair<Int, Long> {
        var vitalityChange = 0
        if (health < 96){
            vitalityChange = Random.nextInt(0, 2)
        }

        health += vitalityChange
        money -= charge
        return Pair(vitalityChange, charge) //change to randomCharge
    }

    fun partyOptions(option: Int) : Pair<Int, Long> {
        when (option) {
            1 -> {
                return if (money > 40) {
                    partyProcess(40)
                } else 0 to -1L
            }
            2 -> {
                return partyProcess(0)
            }
            3 -> {
                return if (money > 150) {
                    partyProcess(150)
                } else 0 to -1L
            }
            4 -> {
                return if (money > 200) {
                    partyProcess(200)
                } else 0 to -1L
            }
            5 -> {
                //casino option coming soon? sort out options
                return if (money > 40) {
                    partyProcess(40)
                } else 0 to -1L
            }
            else -> {
                return 0 to 0L
            }
        }
    }

    private fun holidayProcess(charge: Long, maxHealthAddition: Int): Pair<Int, Long> {
        var vitalityChange = 0
        if (health < 90){
            vitalityChange = Random.nextInt(0, maxHealthAddition)
        } else {
            vitalityChange = Random.nextInt(0, 10)
            if (vitalityChange + health >= 100)
                vitalityChange = 100-health
        }

        health += vitalityChange
        money -= charge
        return Pair(vitalityChange, charge) //change to randomCharge
    }

    fun holidayOptions(option: Int) : Pair<Int, Long> {
        when (option) {
            1 -> {
                return if (money > 15000) {
                    holidayProcess(15000, 10)
                } else 0 to -1L
            }
            2 -> {
                return if (money > 8000) {
                    holidayProcess(8000, 8)
                } else 0 to -1L
            }
            3 -> {
                return if (money > 12000) {
                    holidayProcess(12000, 6)
                } else 0 to -1L
            }
            4 -> {
                return if (money > 2000) {
                    holidayProcess(2000, 3)
                } else 0 to -1L
            }
            else -> {
                return 0 to 0L
            }
        }
    }

    private fun charityProcess(charge: Long, luck: Int): Pair<Int, Long> {
        var fortuneChange = 0
        if (fortune < 90){
            fortuneChange = Random.nextInt(0, luck)
        } else {
            fortuneChange = Random.nextInt(0, 10)
            if (fortuneChange + fortune >= 100)
                fortuneChange = 100-fortune
        }

        fortune += fortuneChange
        money -= charge
        return Pair(fortuneChange, charge) //change to randomCharge
    }

    fun charityOptions(option: Int) : Pair<Int, Long> {
        when (option) {
            1 -> {
                return charityProcess(0, 4)
            }
            2 -> {
                return if (money > 0) {
                    charityProcess((money*0.1).toLong(), 10) //10% to charity
                } else 0 to -1L
            }
            3 -> {
                return charityProcess(0, 6)
            }
            else -> {
                return 0 to 0L
            }
        }
    }

    fun hasSkill(skill: Skill): Int{
        when (skill){
            (Skill.READ) -> {
                moneyChange -= 300
                val change = Random.nextInt(0, 2)
                geniusChange += change
                return change
            }
            (Skill.ACT) -> {
                moneyChange -= 2400
                val change = Random.nextInt(0, 2)
                fame = FameLevel.B      //need to fix
                return change
            }
            (Skill.PRAY) -> {
                moneyChange -= 50
                val change = Random.nextInt(0, 1)
                fortuneChange += change
                return change
            }
            (Skill.FIGHT) -> {
                moneyChange -= 1100
                val change = Random.nextInt(0, 2)
                healthChange += change
                return change
            }
            (Skill.POLITICS) -> {
                moneyChange -= 450
                val change = Random.nextInt(0, 2)
                charmChange += change
                return change
            }
            (Skill.CRIME) -> {
                moneyChange -= 500
                val change = Random.nextInt(0, 2)
                charmChange += change
                return change
            }
            else -> return -1
        }
    }

    fun turnOffSkill(skill: Skill){
        when (skill){
            (Skill.READ) -> {
                moneyChange += 300
                geniusChange -= 1
            }
            (Skill.ACT) -> {
                moneyChange += 2400
                fame = FameLevel.C      //need to fix
            }
            (Skill.PRAY) -> {
                moneyChange += 50
                fortuneChange -= 3
            }
            (Skill.FIGHT) -> {
                moneyChange += 1100
                healthChange -= 1
            }
            (Skill.POLITICS) -> {
                moneyChange += 450
                charmChange -= 1
            }
            (Skill.CRIME) -> {
                moneyChange += 500
                charmChange -= 1
            }

            else -> {}
        }
    }

    fun date(person: NPC){
        person.affectionType = AffectionType.Girlfriend
        lovers.add(person)
    }
}

data class NPC(
    override val name: String,
    override var age: Int = 0,
    override val gender: Boolean,
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
    override var affection: Int,
    override var isAlive: Boolean = true
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
    Me,
    Stranger
}

enum class Skill {
    READ, ACT, PRAY, FIGHT, POLITICS, CRIME
}

