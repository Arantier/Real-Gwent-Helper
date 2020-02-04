package ru.shcherbakov.realgwenthelper.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class CardViewHolder(val view: View, val openEditorListener: OpenEditorListener) :
    AbstractCardViewHolder(view) {

    fun bind(card: Card) {
        view.backgroundImage.setImageDrawable(
            view.context.getDrawable(
                if (card.type == Card.TYPE_UNIT) {
                    when (card.bonus) {
                        Card.BONUS_BOND -> R.drawable.ic_card_unit_bond_active
                        Card.BONUS_BUFF -> R.drawable.ic_card_unit_buff_active
                        Card.BONUS_BERSERK -> R.drawable.ic_card_unit_berserk_active
                        Card.BONUS_HORN -> R.drawable.ic_card_unit_horn_active
                        else -> R.drawable.ic_card_unit_default_active
                    }
                } else {
                    when (card.bonus) {
                        Card.BONUS_BUFF -> R.drawable.ic_card_hero_buff_active
                        Card.BONUS_MUSHROOM -> R.drawable.ic_card_hero_mushroom_active
                        else -> R.drawable.ic_card_hero_default_active
                    }
                }
            )
        )

        view.score.text = card.finalCost.toString()
        view.setOnClickListener { openEditorListener.editCard(card) }
    }
}