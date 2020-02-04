package ru.shcherbakov.realgwenthelper.data

import io.reactivex.subjects.PublishSubject

class Row {
   /* private val _cardList = ArrayList<Card>()
    private val _calculatedCardList: Iterable<Card> = _cardList
    private val bondedCardsSets = HashSet<HashSet<Card>>()

    private var activeBuffs = 0

    var pureScore = PublishSubject.create<Int>()
    var affectedScore = 0

    val cardList: Iterable<Card>
        get() {
            return _calculatedCardList
        }

    fun addCard(card: Card, bondedCard: Card? = null) {
        _cardList.add(card)
        when (card.bonus) {
            Card.BONUS_BOND -> {
                if (bondedCard == null) {
                    val set = HashSet<Card>()
                    set.add(card)
                    bondedCardsSets.add(set)
                } else {
                    val bondedSet = bondedCardsSets.find { it.contains(bondedCard) }
                    bondedSet!!.add(card)
                }
            }

            Card.BONUS_BUFF -> activeBuffs++
            Card.BONUS_HORN -> activeHorns++
            Card.BONUS_MUSHROOM -> activeMushrooms++
        }
        updateScore()
    }

    fun removeCard(card: Card) {
        when (card.bonus) {
            Card.BONUS_BOND -> bondedCardsSets.find { it.contains(card) }!!.remove(card)
            Card.BONUS_BUFF -> activeBuffs--
            Card.BONUS_HORN -> activeHorns--
            Card.BONUS_MUSHROOM -> activeMushrooms--
        }
        _cardList.remove(card)
        updateScore()
    }


    fun editCard(oldCard: Card, editedCard: Card) {
        removeCard(oldCard)
        addCard(editedCard)
    }

    fun applyEffect(effect: Int) {
        when (effect) {
            EFFECT_CLEAR -> weather = EFFECT_CLEAR
            EFFECT_WEATHER -> weather = EFFECT_WEATHER
            EFFECT_HORN -> activeHorns++
            EFFECT_MUSHROOM -> activeMushrooms++
            EFFECT_ADD_BRAN -> isBranAffected = true
            EFFECT_REMOVE_BRAN -> isBranAffected = false
            EFFECT_SCORCH -> {
                //TODO: Тут проблема. Надо разобраться как хранить выводимые значения и как на них применять казнь
            }
        }
        updateScore()
    }

    private fun updateScore() {
        //TODO: доделать
    }

    companion object {
        val EFFECT_SCORCH = -2
        val EFFECT_WEATHER = -1
        val EFFECT_CLEAR = 0
        val EFFECT_HORN = 1
        val EFFECT_MUSHROOM = 2
        val EFFECT_ADD_BRAN = 3
        val EFFECT_REMOVE_BRAN = 4
    }*/

}