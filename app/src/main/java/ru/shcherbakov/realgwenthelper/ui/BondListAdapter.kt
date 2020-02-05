package ru.shcherbakov.realgwenthelper.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class BondListAdapter(
    private val card: Card,
    private val bondList: ArrayList<ArrayList<Card>>,
    private val forgeBondInterface: ForgeBondInterface,
    val onClickListener: View.OnClickListener
) : RecyclerView.Adapter<BondViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BondViewHolder(
        LayoutInflater.from(parent.context).inflate(
            R.layout.item_bond, parent, false
        ), forgeBondInterface
    )

    override fun getItemCount() = bondList.size

    override fun onBindViewHolder(holder: BondViewHolder, position: Int) {
        holder.bind(card, bondList[position], onClickListener)
    }
}
