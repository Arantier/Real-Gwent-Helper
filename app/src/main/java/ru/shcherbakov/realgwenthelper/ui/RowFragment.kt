package ru.shcherbakov.realgwenthelper.ui


import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ToggleButton
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.dialog_buffs.view.*
import kotlinx.android.synthetic.main.fragment_row.*
import kotlinx.android.synthetic.main.fragment_row.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Row

class RowFragment private constructor(
    val openRowMenuInterface: OpenRowMenuInterface,
    val rowType: Int,
    val playerType: Int
) :
    Fragment() {

    private var row = Row()
    private lateinit var buffDialog: AlertDialog
    private lateinit var scoreDisposable: Disposable

    val score
        get() = row.liveScore
    var badWeather
        get() = row.badWeather
        set(value) {
            row.badWeather = value
        }

    fun resetRow() {
        row = Row()
        scoreDisposable = score.subscribe {
            textScore.text = it.toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_row, container, false).apply {
        textScore.setTextColor(
            resources.getColor(
                if (playerType == FIRST_PLAYER) {
                    R.color.firstPlayerScoreColor
                } else {
                    R.color.secondPlayerScoreColor
                }
            )
        )

        scoreDisposable = score.subscribe {
            textScore.text = it.toString()
        }

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
                        buffDialog.hide()
                    }

                    buttonMushroom.setOnClickListener {
                        row.enableMushroom()
                        buffDialog.hide()
                    }

                    buttonHorn.setOnClickListener {
                        row.hornActive = (it as ToggleButton).isChecked
                    }

                    buttonBran.setOnClickListener {
                        row.bran = (it as ToggleButton).isChecked
                    }
                })
                .create()
            buffDialog.show()
        }

        buttonEditDeck.setOnClickListener {
            openRowMenuInterface.showRowMenu(row, rowType)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        scoreDisposable.dispose()
    }

    companion object {
        val FIRST_PLAYER = 2
        val SECOND_PLAYER = 1

        val TYPE_INFANTRY = 0
        val TYPE_ARCHERY = 1
        val TYPE_SIEGE = 2

        fun newInstance(
            openRowMenuInterface: OpenRowMenuInterface,
            rowType: Int,
            playerType: Int
        ): RowFragment {
            return RowFragment(openRowMenuInterface, rowType, playerType)
        }
    }
}
