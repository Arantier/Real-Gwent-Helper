package ru.shcherbakov.realgwenthelper.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_row.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.DeckRowInterface
import ru.shcherbakov.realgwenthelper.data.Row

class RowFragment private constructor(val deckRowInterface: DeckRowInterface, val type: Int) : Fragment() {

    private val row = Row()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_row, container, false)
        view.buttonEditDeck.setOnClickListener {
            deckRowInterface.showRowMenu(row, type)
        }
        return view
    }

    companion object {
        val TYPE_INFANTRY = 0
        val TYPE_ARCHERY = 1
        val TYPE_SIEGE = 2

        val DECK_ROW_INTERFACE = "deck_row_interface"

        fun newInstance(deckRowInterface: DeckRowInterface, type: Int): RowFragment {
            return RowFragment(deckRowInterface, type)
        }
    }
}
