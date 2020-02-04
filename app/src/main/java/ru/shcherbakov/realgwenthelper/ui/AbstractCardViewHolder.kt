package ru.shcherbakov.realgwenthelper.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

abstract class AbstractCardViewHolder(private val view: View) : RecyclerView.ViewHolder(view)