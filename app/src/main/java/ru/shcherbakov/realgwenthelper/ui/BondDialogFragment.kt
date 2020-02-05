package ru.shcherbakov.realgwenthelper.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import kotlinx.android.synthetic.main.dialog_bond.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card

class BondDialogFragment(
    val card: Card,
    val bonds: ArrayList<ArrayList<Card>>,
    val forgeBondInterface: ForgeBondInterface
) : DialogFragment(), View.OnClickListener {

    override fun onClick(v: View?) {
        dismiss()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dialog_bond, container, false).apply {
        buttonNewBond.setOnClickListener {
            forgeBondInterface.forgeBond(card)
            dismiss()
        }
        recyclerBondList.adapter =
            BondListAdapter(card, bonds, forgeBondInterface, this@BondDialogFragment)
    }

    override fun onCancel(dialog: DialogInterface?) {
        super.onCancel(dialog)
        forgeBondInterface.forgeBond(card)
    }
}