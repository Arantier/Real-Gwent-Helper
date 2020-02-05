package ru.shcherbakov.realgwenthelper.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.fragment_deck.backgroundImages
import kotlinx.android.synthetic.main.fragment_deck.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Player
import ru.shcherbakov.realgwenthelper.data.Row

class DeckFragment private constructor(val player: Player) : Fragment(), DeckRowInterface {
    val rowInfantry = RowFragment.newInstance(
        this,
        RowFragment.TYPE_INFANTRY,
        if (player.name.equals("First")) RowFragment.FIRST_PLAYER else RowFragment.SECOND_PLAYER
    )
    val rowArchery = RowFragment.newInstance(
        this, RowFragment.TYPE_ARCHERY,
        if (player.name.equals("First")) RowFragment.FIRST_PLAYER else RowFragment.SECOND_PLAYER
    )
    val rowSiege = RowFragment.newInstance(
        this, RowFragment.TYPE_SIEGE,
        if (player.name.equals("First")) RowFragment.FIRST_PLAYER else RowFragment.SECOND_PLAYER
    )

    var infantryScore = 0
    var archeryScore = 0
    var siegeScore = 0

    var frost
        get() = rowInfantry.badWeather
        set(value) {
            rowInfantry.badWeather = value
            if (value){
                imageFrost.visibility = View.VISIBLE
            } else {
                imageFrost.visibility = View.GONE
            }
        }
    var fog
        get() = rowArchery.badWeather
        set(value) {
            rowArchery.badWeather = value
            if (value){
                imageFog.visibility = View.VISIBLE
            } else {
                imageFog.visibility = View.GONE
            }
        }
    var rain
        get() = rowSiege.badWeather
        set(value) {
            rowSiege.badWeather = value
            if (value){
                imageRain.visibility = View.VISIBLE
            } else {
                imageRain.visibility = View.GONE
            }
        }

    fun resetDeck() {
        rowInfantry.resetRow()
        rowArchery.resetRow()
        rowSiege.resetRow()

        rowInfantry.score.subscribe {
            infantryScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowArchery.score.subscribe {
            archeryScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowSiege.score.subscribe {
            siegeScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }

        imageDeckBlocker.visibility = View.GONE
    }

    override fun showRowMenu(row: Row, type: Int) {
        childFragmentManager.beginTransaction()
            .add(R.id.menu, RowMenuFragment.newInstance(row, this, type), MENU_KEY)
            .commit()
        rows.visibility = View.GONE
        backgroundImages.visibility = View.GONE
        menu.visibility = View.VISIBLE
    }

    override fun closeRowMenu() {
        childFragmentManager.apply {
            val fragment = findFragmentByTag(MENU_KEY)
            if (fragment != null) {
                beginTransaction().remove(fragment)
                    .commit()
            }
            rows.visibility = View.VISIBLE
            backgroundImages.visibility = View.VISIBLE
            menu.visibility = View.GONE
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_deck, container, false).apply {
        childFragmentManager.apply {
            beginTransaction()
                .add(
                    R.id.infantry, rowInfantry
                )
                .add(
                    R.id.archery, rowArchery
                )
                .add(
                    R.id.siege, rowSiege
                )
                .commit()
        }
        rowInfantry.score.subscribe {
            infantryScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowArchery.score.subscribe {
            archeryScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowSiege.score.subscribe {
            siegeScore = it
            player.liveScore.onNext(infantryScore + archeryScore + siegeScore)
        }

        buttonPass.setOnLongClickListener {
            imageDeckBlocker.visibility = View.VISIBLE
            player.passed.onNext(true)
            true
        }
    }

    companion object {
        private val MENU_KEY = "menu"

        fun newInstance(player: Player): DeckFragment {
            return DeckFragment(player)
        }
    }
}
