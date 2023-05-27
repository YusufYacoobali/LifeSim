package com.example.lifesim4

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.lifesim4.databinding.ActivityMainBinding
import com.example.lifesim4.models.GameEngine
import com.example.lifesim4.models.Person

class MainActivity : AppCompatActivity() {

    private lateinit var gameEngine: GameEngine
    private lateinit var player: Person

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setContentView(binding.root)

        binding.bottomButtons.bottomNavBar.apply {
            background = null
            menu.getItem(2).isEnabled = false
        }

        gameEngine = GameEngine().apply { startGame() }
        player = gameEngine.getPlayer()
        binding.person = player
        binding.statusBar.person = player

        binding.bottomButtons.fab.setOnClickListener {
            gameEngine.simulate()
            binding.statusBar.textAge.text = player.age.toString()
            binding.statusBar.textMoney.text = player.money.toString()
            binding.statusBar.textHealth.text = player.health.toString()
        }
    }
}
