package ru.shcherbakov.realgwenthelper.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_weather.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Player

class MainActivity : AppCompatActivity() {

    private lateinit var weatherDialog: Dialog
    private val firstPlayer = Player("First")
    private val secondPlayer = Player("Second")

    private lateinit var firstPlayerScoreDisposable: Disposable
    private lateinit var secondPlayerScoreDisposable: Disposable

    private val firstPlayerDeck = DeckFragment.newInstance(firstPlayer)
    private val secondPlayerDeck = DeckFragment.newInstance(secondPlayer)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        firstPlayerScoreDisposable = firstPlayer.score
            .subscribe {
                textFirstPlayerScore.text = it.toString()
                textFirstPlayerScoreInversed.text = it.toString()
            }
        secondPlayerScoreDisposable = secondPlayer.score
            .subscribe {
                textSecondPlayerScore.text = it.toString()
                textSecondPlayerScoreInversed.text = it.toString()
            }
        supportFragmentManager.beginTransaction()
            .add(
                R.id.firstPlayerDeck, firstPlayerDeck
            )
            .add(
                R.id.secondPlayerDeck, secondPlayerDeck
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

    override fun onDestroy() {
        super.onDestroy()
        firstPlayerScoreDisposable.dispose()
        secondPlayerScoreDisposable.dispose()
    }
}
