package com.jacgr.filmsdbroom.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.jacgr.filmsdbroom.R
import kotlin.concurrent.thread

class Splash : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        thread {
            Thread.sleep(1500)
            startActivity(Intent(this, MainActivity::class.java))
            Animatoo.animateFade(this)
            finish()
        }

    }
}