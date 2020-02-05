package ru.shcherbakov.realgwenthelper.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.rxkotlin.zipWith
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.fragment_deck.backgroundImages
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
            player.score.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowArchery.score.subscribe {
            archeryScore = it
            player.score.onNext(infantryScore + archeryScore + siegeScore)
        }
        rowSiege.score.subscribe {
            siegeScore = it
            player.score.onNext(infantryScore + archeryScore + siegeScore)
        }
    }

    companion object {
        private val MENU_KEY = "menu"

        fun newInstance(player: Player): DeckFragment {
            return DeckFragment(player)
        }
    }
}
