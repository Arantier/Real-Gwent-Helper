package ru.shcherbakov.realgwenthelper.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import kotlinx.android.synthetic.main.fragment_deck.*
import kotlinx.android.synthetic.main.fragment_deck.backgroundImages
import kotlinx.android.synthetic.main.fragment_deck.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.DeckRowInterface
import ru.shcherbakov.realgwenthelper.data.Row

class DeckFragment private constructor() : Fragment(), DeckRowInterface {

    private var mode = DECK_MODE

    override fun showRowMenu(row: Row, type: Int) {
        childFragmentManager.beginTransaction()
            .add(R.id.menu, DeckMenuFragment.newInstance(this, type), MENU_KEY)
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
                    R.id.infantry,
                    RowFragment.newInstance(this@DeckFragment, RowFragment.TYPE_INFANTRY)
                )
                .add(
                    R.id.archery,
                    RowFragment.newInstance(this@DeckFragment, RowFragment.TYPE_ARCHERY)
                )
                .add(
                    R.id.siege,
                    RowFragment.newInstance(this@DeckFragment, RowFragment.TYPE_SIEGE)
                )
                .commit()
        }

        this.buttonLeaderMenu.setOnClickListener {
            if (mode == DECK_MODE) {
                backgroundImages.visibility = View.GONE
                infantry.visibility = View.INVISIBLE
                archery.visibility = View.INVISIBLE
                siege.visibility = View.INVISIBLE
                leaderMenu.visibility = View.VISIBLE
                (it as ImageButton).setImageDrawable(context?.getDrawable(R.drawable.ic_back))
                mode = LEADER_MODE
            } else {
                backgroundImages.visibility = View.VISIBLE
                infantry.visibility = View.VISIBLE
                archery.visibility = View.VISIBLE
                siege.visibility = View.VISIBLE
                leaderMenu.visibility = View.GONE
                (it as ImageButton).setImageDrawable(context?.getDrawable(R.drawable.ic_crown))
                mode = DECK_MODE
            }
        }
    }

    companion object {
        private val MENU_KEY = "menu"

        val DECK_MODE = 0
        val EDIT_MODE = 1
        val LEADER_MODE = 2
        fun newInstance(): DeckFragment {
            return DeckFragment()
        }
    }
}
