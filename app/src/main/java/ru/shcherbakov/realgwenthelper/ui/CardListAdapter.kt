package ru.shcherbakov.realgwenthelper.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class CardListAdapter(
    private val cardList: ArrayList<Card>,
    private val openEditorListener: OpenEditorListener
) :
    RecyclerView.Adapter<AbstractCardViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractCardViewHolder {
        return if (viewType == ITEM_CARD) {
            CardViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card, parent, false
                ), openEditorListener
            )
        } else {
            AddButtonViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_card, parent, false
                ), openEditorListener
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < cardList.size) ITEM_CARD else ITEM_BUTTON
    }

    override fun getItemCount() = cardList.size + 1

    override fun onBindViewHolder(holder: AbstractCardViewHolder, position: Int) {
        if (holder.itemViewType == ITEM_CARD) {
            (holder as CardViewHolder).bind(cardList[position])
        }
    }

    companion object {
        val ITEM_CARD = 0
        val ITEM_BUTTON = 1
    }
}
