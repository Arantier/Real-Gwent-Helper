package ru.shcherbakov.realgwenthelper.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class AddButtonViewHolder(val view: View, val openEditorListener: OpenEditorListener) :
    AbstractCardViewHolder(view) {

    init {
        view.backgroundImage.setImageDrawable(
            view.context.getDrawable(R.drawable.ic_card_unit_default_active)
        )
        view.score.text = "+"
        view.setOnClickListener { openEditorListener.addCard() }
    }

}