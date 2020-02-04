package ru.shcherbakov.realgwenthelper.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_weather.view.*
import ru.shcherbakov.realgwenthelper.R

class MainActivity : AppCompatActivity() {

    private lateinit var weatherDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(
                R.id.firstPlayerDeck,
                DeckFragment.newInstance()
            )
            .add(
                R.id.secondPlayerDeck,
                DeckFragment.newInstance()
            )
            .commit()

        weatherDialog = AlertDialog.Builder(this).apply {
            val view = layoutInflater.inflate(R.layout.dialog_weather, null)
            //TODO: установи листенеры
            view.buttonClose.setOnClickListener {
                weatherDialog.hide()
            }
            setView(view)
        }.create()
        buttonWeather.setOnClickListener {
            weatherDialog.show()
        }
    }
}
