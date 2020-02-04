package ru.shcherbakov.realgwenthelper.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_row_menu.*
import kotlinx.android.synthetic.main.fragment_row_menu.view.*
import ru.shcherbakov.realgwenthelper.R
import ru.shcherbakov.realgwenthelper.data.Card
import ru.shcherbakov.realgwenthelper.data.Row

class RowMenuFragment private constructor(
    val row: Row,
    val deckRowInterface: DeckRowInterface,
    val rowType: Int
) : Fragment(),
    OpenEditorListener, View.OnClickListener {

    var currentButtonState = BUTTON_UNIT_DEFAULT

    fun openMenu() {
        recyclerCardsList.visibility = View.GONE
        layoutMenuEdit.visibility = View.VISIBLE
        buttonRemove.visibility = View.VISIBLE
        buttonApply.visibility = View.VISIBLE

        buttonClose.setOnClickListener { closeMenu() }
    }

    fun closeMenu() {
        recyclerCardsList.visibility = View.VISIBLE
        layoutMenuEdit.visibility = View.GONE
        buttonRemove.visibility = View.GONE
        buttonApply.visibility = View.GONE
        recyclerCardsList.adapter = CardListAdapter(row.cardList, this@RowMenuFragment)
        buttonClose.setOnClickListener { deckRowInterface.closeRowMenu() }
    }

    fun setButtonActive(button: ImageButton) {
        button.setBackgroundResource(
            when (button.id) {
                buttonUnitBuff.id -> R.drawable.ic_card_unit_buff_active
                buttonUnitHorn.id -> R.drawable.ic_card_unit_horn_active
                buttonUnitBond.id -> R.drawable.ic_card_unit_bond_active
                buttonHeroDefault.id -> R.drawable.ic_card_hero_default_active
                buttonHeroBuff.id -> R.drawable.ic_card_hero_buff_active
                buttonHeroMushroom.id -> R.drawable.ic_card_hero_mushroom_active
                buttonUnitBerserk.id -> R.drawable.ic_card_unit_berserk_active
                else -> R.drawable.ic_card_unit_default_active
            }
        )
    }

    fun setButtonInActive(button: ImageButton) {
        button.setBackgroundResource(
            when (button.id) {
                buttonUnitBuff.id -> R.drawable.ic_card_unit_buff_inactive
                buttonUnitHorn.id -> R.drawable.ic_card_unit_horn_inactive
                buttonUnitBond.id -> R.drawable.ic_card_unit_bond_inactive
                buttonHeroDefault.id -> R.drawable.ic_card_hero_default_inactive
                buttonHeroBuff.id -> R.drawable.ic_card_hero_buff_inactive
                buttonHeroMushroom.id -> R.drawable.ic_card_hero_mushroom_inactive
                buttonUnitBerserk.id -> R.drawable.ic_card_unit_berserk_inactive
                else -> R.drawable.ic_card_unit_default_inactive
            }
        )
    }

    fun setButtonState(state: Int) {
        currentButtonState = state
        var activeButton: ImageButton = when (state) {
            BUTTON_UNIT_BUFF -> buttonUnitBuff
            BUTTON_UNIT_HORN -> buttonUnitHorn
            BUTTON_UNIT_BOND -> buttonUnitBond
            BUTTON_HERO_DEFAULT -> buttonHeroDefault
            BUTTON_HERO_BUFF -> buttonHeroBuff
            BUTTON_HERO_MUSHROOM -> buttonHeroMushroom
            BUTTON_UNIT_BERSERK -> buttonUnitBerserk
            else -> buttonUnitDefault
        }
        constraintLayout.children
            .filter { it != activeButton }
            .forEach { setButtonInActive(it as ImageButton) }
        setButtonActive(activeButton as ImageButton)
    }

    override fun addCard() {
        val newCard = Card(0, Card.TYPE_UNIT, Card.BONUS_NONE)
        row.addCard(newCard)
        editCard(newCard)
    }

    override fun editCard(card: Card) {
        numberPicker.value = card.cost
        val buttonValue = if (card.type == Card.TYPE_HERO) {
            when (card.bonus) {
                Card.BONUS_BUFF -> BUTTON_HERO_BUFF
                Card.BONUS_MUSHROOM -> BUTTON_HERO_MUSHROOM
                else -> BUTTON_HERO_DEFAULT
            }
        } else {
            when (card.bonus) {
                Card.BONUS_BUFF -> BUTTON_UNIT_BUFF
                Card.BONUS_HORN -> BUTTON_UNIT_HORN
                Card.BONUS_BOND -> BUTTON_UNIT_BOND
                Card.BONUS_BERSERK -> BUTTON_UNIT_BERSERK
                else -> BUTTON_UNIT_DEFAULT
            }
        }
        setButtonState(buttonValue)
        buttonRemove.setOnClickListener {
            row.removeCard(card)
            closeMenu()
        }
        buttonApply.setOnClickListener {
            val cost = numberPicker.value
            val type = when (currentButtonState) {
                BUTTON_HERO_BUFF, BUTTON_HERO_MUSHROOM, BUTTON_HERO_DEFAULT -> Card.TYPE_HERO
                else -> Card.TYPE_UNIT
            }
            val bonus = when (currentButtonState) {
                BUTTON_UNIT_BUFF, BUTTON_HERO_BUFF -> Card.BONUS_BUFF
                BUTTON_UNIT_HORN -> Card.BONUS_HORN
                BUTTON_UNIT_BOND -> Card.BONUS_BOND
                BUTTON_HERO_MUSHROOM -> Card.BONUS_MUSHROOM
                BUTTON_UNIT_BERSERK -> Card.BONUS_BERSERK
                else -> Card.BONUS_NONE
            }
            if (card.cost != cost || card.type != type || card.bonus != bonus) {
                row.editCard(card, Card(cost, type, bonus))
            }
            closeMenu()
        }
        openMenu()
    }

    override fun onClick(v: View?) {
        when (view?.id) {
            buttonUnitBuff.id -> setButtonState(BUTTON_UNIT_BUFF)
            buttonUnitHorn.id -> setButtonState(BUTTON_UNIT_HORN)
            buttonUnitBond.id -> setButtonState(BUTTON_UNIT_BOND)
            buttonHeroDefault.id -> setButtonState(BUTTON_HERO_DEFAULT)
            buttonHeroBuff.id -> setButtonState(BUTTON_HERO_BUFF)
            buttonHeroMushroom.id -> setButtonState(BUTTON_HERO_MUSHROOM)
            buttonUnitBerserk.id -> setButtonState(BUTTON_UNIT_BERSERK)
            else -> setButtonState(BUTTON_UNIT_DEFAULT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_row_menu, container, false).apply {
        when (rowType) {
            RowFragment.TYPE_INFANTRY -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_infantry))
            RowFragment.TYPE_ARCHERY -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_archery))
            RowFragment.TYPE_SIEGE -> imageRowIcon.setImageDrawable(context.getDrawable(R.drawable.ic_siege))
        }
        buttonClose.setOnClickListener {
            deckRowInterface.closeRowMenu()
        }

        recyclerCardsList.adapter = CardListAdapter(row.cardList, this@RowMenuFragment)

        constraintLayout.children
            .forEach {
                it.setOnClickListener(this@RowMenuFragment)
            }
    }

    companion object {

        val BUTTON_UNIT_DEFAULT = 0
        val BUTTON_UNIT_BUFF = 1
        val BUTTON_UNIT_HORN = 2
        val BUTTON_UNIT_BOND = 3
        val BUTTON_HERO_DEFAULT = 4
        val BUTTON_HERO_BUFF = 5
        val BUTTON_HERO_MUSHROOM = 6
        val BUTTON_UNIT_BERSERK = 7

        fun newInstance(
            row: Row,
            deckRowInterface: DeckRowInterface,
            rowType: Int
        ): RowMenuFragment {
            return RowMenuFragment(row, deckRowInterface, rowType)
        }
    }
}
