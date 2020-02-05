package ru.shcherbakov.realgwenthelper.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bond.view.*
import kotlinx.android.synthetic.main.item_card.view.*
import kotlinx.android.synthetic.main.item_card.view.score
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class BondViewHolder(val view: View, val forgeBondInterface: ForgeBondInterface) :
    RecyclerView.ViewHolder(view) {

    fun bind(card: Card, bond: ArrayList<Card>, onClickListener: View.OnClickListener) {
        view.score.text = bond[0].finalCost.toString()
        view.count.text = "x" + bond.size.toString()
        view.setOnClickListener {
            forgeBondInterface.forgeBond(card, bond)
            onClickListener.onClick(it)
        }
    }
}