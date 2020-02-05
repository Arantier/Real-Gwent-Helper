package ru.shcherbakov.realgwenthelper.ui

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
import androidx.core.view.children
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

    private lateinit var firstPlayerStateDisposable: Disposable
    private lateinit var secondPlayerStateDisposable: Disposable

    private var firstPlayerDeck = DeckFragment.newInstance(firstPlayer)
    private var secondPlayerDeck = DeckFragment.newInstance(secondPlayer)

    fun solveResult(isFirstPassed: Boolean, isSecondPassed: Boolean) {
        if (isFirstPassed && isSecondPassed) {
            if (firstPlayer.score > secondPlayer.score) {
                secondPlayer.lives--
                if (secondPlayer.lives > 0) {
                    imageSecondPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                } else {
                    imageSecondPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                }
            } else if (firstPlayer.score < secondPlayer.score) {
                firstPlayer.lives--

                if (firstPlayer.lives > 0) {
                    imageFirstPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                } else {
                    imageFirstPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                }
            } else {
                firstPlayer.lives--
                secondPlayer.lives--
                if (secondPlayer.lives > 0) {
                    imageSecondPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                } else {
                    imageSecondPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                }
                if (firstPlayer.lives > 0) {
                    imageFirstPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                } else {
                    imageFirstPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_empty))
                }
            }

            if (firstPlayer.lives != 0 && secondPlayer.lives != 0) {
                firstPlayer.isPassed = false
                secondPlayer.isPassed = false
                firstPlayerDeck.resetDeck()
                secondPlayerDeck.resetDeck()
            } else {
                resultView.visibility = View.VISIBLE
                if (firstPlayer.lives == 0 && secondPlayer.lives != 0) {
                    resultView.rotation = 180.0f
                } else if (firstPlayer.lives == secondPlayer.lives) {
                    textWon.visibility = View.GONE
                    textLose.visibility = View.GONE
                    textTie.visibility = View.VISIBLE
                }
            }
        }
    }

    fun resetGame() {
        arrayOf(firstPlayer, secondPlayer).forEach { player ->
            player.apply {
                lives = 2
                passed.onNext(false)
            }

            imageFirstPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
            imageFirstPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
            imageSecondPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
            imageSecondPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))

            val newFirstPlayerDeck = DeckFragment.newInstance(firstPlayer)
            val newSecondPlayerDeck = DeckFragment.newInstance(secondPlayer)
            supportFragmentManager.beginTransaction()
                .replace(R.id.firstPlayerDeck, newFirstPlayerDeck)
                .replace(R.id.secondPlayerDeck, newSecondPlayerDeck)
                .commit()
            firstPlayerDeck = newFirstPlayerDeck
            secondPlayerDeck = newSecondPlayerDeck
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageFirstPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
        imageFirstPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
        imageSecondPlayerGem1.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))
        imageSecondPlayerGem2.setImageDrawable(resources.getDrawable(R.drawable.ic_gem_filled))

        firstPlayerScoreDisposable = firstPlayer.liveScore
            .subscribe {
                firstPlayer.score = it
                textFirstPlayerScore.text = it.toString()
                textFirstPlayerScoreInversed.text = it.toString()
            }
        secondPlayerScoreDisposable = secondPlayer.liveScore
            .subscribe {
                secondPlayer.score = it
                textSecondPlayerScore.text = it.toString()
                textSecondPlayerScoreInversed.text = it.toString()
            }

        firstPlayerScoreDisposable = firstPlayer.passed
            .subscribe {
                firstPlayer.isPassed = it
                solveResult(firstPlayer.isPassed, secondPlayer.isPassed)
            }
        secondPlayerStateDisposable = secondPlayer.passed
            .subscribe {
                secondPlayer.isPassed = it
                solveResult(firstPlayer.isPassed, secondPlayer.isPassed)
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
            view.apply {
                buttonClear.setOnClickListener {
                    buttonFrost.isChecked = false
                    buttonFog.isChecked = false
                    buttonRain.isChecked = false
                    buttonStorm.isChecked = false
                }

                buttonFrost.setOnCheckedChangeListener { buttonView, isChecked ->
                    firstPlayerDeck.frost = isChecked
                    secondPlayerDeck.frost = isChecked
                }

                buttonFog.setOnCheckedChangeListener { buttonView, isChecked ->
                    firstPlayerDeck.fog = isChecked
                    secondPlayerDeck.fog = isChecked
                }

                buttonRain.setOnCheckedChangeListener { buttonView, isChecked ->
                    firstPlayerDeck.rain = isChecked
                    secondPlayerDeck.rain = isChecked
                }

                buttonStorm.setOnCheckedChangeListener { buttonView, isChecked ->
                    buttonFog.isChecked = isChecked
                    buttonRain.isChecked = isChecked
                }

                buttonClose.setOnClickListener {
                    weatherDialog.hide()
                }
            }
            setView(view)
        }.create()
        buttonWeather.setOnClickListener {
            weatherDialog.show()
        }

        buttonReset.setOnClickListener { resetGame() }

        resultView.children.forEach {
            it.setOnClickListener {
                resetGame()
                textTie.visibility = View.GONE
                textWon.visibility = View.VISIBLE
                textLose.visibility = View.VISIBLE
                resultView.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        firstPlayerScoreDisposable.dispose()
        secondPlayerScoreDisposable.dispose()

        firstPlayerStateDisposable.dispose()
        secondPlayerStateDisposable.dispose()
    }
}
