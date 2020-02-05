package ru.shcherbakov.realgwenthelper.ui


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import kotlinx.android.synthetic.main.dialog_buffs.view.*
import kotlinx.android.synthetic.main.fragment_row.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Row

class RowFragment private constructor(val deckRowInterface: DeckRowInterface, val rowType: Int, val playerType : Int) :
    Fragment() {

    private var row = Row()
    private lateinit var buffDialog: AlertDialog

    val score = row.liveScore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_row, container, false).apply {
        textScore.setTextColor(resources.getColor(
            if (playerType == FIRST_PLAYER){
                R.color.firstPlayerScoreColor
            } else {
                R.color.secondPlayerScoreColor
            }
        ))
        buttonBuffs.setOnClickListener {
            buffDialog = AlertDialog.Builder(context)
                .setView(layoutInflater.inflate(R.layout.dialog_buffs, null).apply {
                    buttonHorn.isChecked = row.hornActive
                    buttonBran.isChecked = row.bran

                    buttonClose.setOnClickListener {
                        buffDialog.hide()
                    }

                    buttonScorch.setOnClickListener {
                        row.scorch()
                    }

                    buttonMushroom.setOnClickListener {
                        row.enableMushroom()
                    }

                    buttonHorn.setOnClickListener{
                        row.hornActive = (it as ToggleButton).isChecked
                    }

                    buttonBran.setOnClickListener {
                        row.bran = (it as ToggleButton).isChecked
                    }
                })
                .create()
            buffDialog.show()
        }
        score.subscribe {
            textScore.text = it.toString()
        }
        buttonEditDeck.setOnClickListener {
            deckRowInterface.showRowMenu(row, rowType)
        }
    }

    companion object {
        val FIRST_PLAYER = 2
        val SECOND_PLAYER = 1

        val TYPE_INFANTRY = 0
        val TYPE_ARCHERY = 1
        val TYPE_SIEGE = 2

        fun newInstance(deckRowInterface: DeckRowInterface, rowType: Int, playerType: Int): RowFragment {
            return RowFragment(deckRowInterface, rowType, playerType)
        }
    }
}
