package ru.shcherbakov.realgwenthelper.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_deck_menu.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.DeckRowInterface

class DeckMenuFragment private constructor(val deckRowInterface: DeckRowInterface, val rowType: Int): Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_deck_menu, container, false).apply {
        when (rowType){
            RowFragment.TYPE_INFANTRY -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_infantry))
            RowFragment.TYPE_ARCHERY -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_archery))
            RowFragment.TYPE_SIEGE -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_siege))
        }
        buttonClose.setOnClickListener {
            deckRowInterface.closeRowMenu()
        }
    }

    companion object{

        fun newInstance(deckRowInterface: DeckRowInterface, rowType: Int) : DeckMenuFragment {
            return DeckMenuFragment(deckRowInterface, rowType)
        }
    }
}
