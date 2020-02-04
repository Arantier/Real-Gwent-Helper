package ru.shcherbakov.realgwenthelper.data

import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

class Row {
    private val _heroesCardList = ArrayList<Card>()
    private val _unitsCardList = ArrayList<Card>()
    private val bondedBerserkers = ArrayList<Card>()
    private val bondedCardsList = ArrayList<ArrayList<Card>>().apply { add(bondedBerserkers) }
    private var isBadWeather = false
    private var isHorny = false
    private var isFuckingBran = false

    val liveScore = BehaviorSubject.create<Int>().apply { onNext(0) }

    var badWeather
        get() = isBadWeather
        set(value) {
            if (value != isBadWeather) {
                isBadWeather = value
                updateScore()
            }
        }
    var hornActive
        get() = isHorny
        set(value) {
            if (value != isHorny) {
                isHorny = value
                updateScore()
            }
        }

    var bran
        get() = isFuckingBran
        set(value) {
            if (value != isFuckingBran) {
                isFuckingBran = value
                updateScore()
            }
        }

    var _mushroom = false
    val mushroomActive
        get() = _mushroom

    val cardList: ArrayList<Card>
        get() {
            val isHornActive = isHorny || _unitsCardList.any { it.bonus == Card.BONUS_HORN } ||
                    _heroesCardList.any { it.bonus == Card.BONUS_HORN }
            val numberOfBuffs = _unitsCardList.count { it.bonus == Card.BONUS_BUFF } +
                    _heroesCardList.count { it.bonus == Card.BONUS_BUFF }
            _unitsCardList.forEach { card ->
                card.finalCost = card.cost
                if (isBadWeather && isFuckingBran) {
                    card.finalCost /= 2
                } else if (isBadWeather) {
                    card.finalCost = 1
                }
                // Применяем бонус связи
                card.finalCost *= bondedCardsList.find { it.contains(card) }?.count() ?: 1
                // Добавляем бонусы от поддержки ряда
                card.finalCost += numberOfBuffs
                // Если карта сама поддерживает ряд, то вычитаем её значение
                if (card.bonus == Card.BONUS_BUFF) {
                    card.finalCost -= 1
                }
                // Если карта не имеет бонус рога и активен командирский рог или сам рог глобален,
                // то умножаем цену карты на 2
                if ((card.bonus != Card.BONUS_HORN || isHorny) && isHornActive) {
                    card.finalCost *= 2
                }
            }
            return (_heroesCardList.clone() as ArrayList<Card>).apply { addAll(_unitsCardList) }
        }

    fun addCard(card: Card, bondedCard: Card? = null) {
        if (card.type == Card.TYPE_UNIT) {
            _unitsCardList.add(card)
        } else {
            _heroesCardList.add(card)
        }
        when (card.bonus) {
            Card.BONUS_BOND -> {
                if (bondedCard == null) {
                    val list = ArrayList<Card>()
                    list.add(card)
                    bondedCardsList.add(list)
                } else {
                    val bondedList = bondedCardsList.find { it.contains(bondedCard) }
                    bondedList!!.add(card)
                }
            }
            Card.BONUS_MUSHROOM -> enableMushroom()
        }
        updateScore()
    }

    fun removeCard(card: Card) {
        if (card.bonus == Card.BONUS_BOND) {
            bondedCardsList.find { it.contains(card) }
                ?.apply { remove(card) }
                .let {
                    if (it != null && it.isEmpty())
                        bondedCardsList.remove(it)
                }
        }
        if (card.type == Card.TYPE_UNIT) {
            _unitsCardList.remove(card)
        } else {
            _heroesCardList.remove(card)
        }
        updateScore()
    }

    fun editCard(oldCard: Card, editedCard: Card) {
        removeCard(oldCard)
        addCard(editedCard)
    }

    fun scorch() {
        val maxCost = cardList.filter { it.type == Card.TYPE_UNIT }
            .maxBy { it.finalCost }
            ?.finalCost ?: 0
        _unitsCardList.filter { it.finalCost == maxCost }
            .forEach {
                removeCard(it)
            }
        updateScore()
    }

    fun enableMushroom() {
        // Тут небольшая хитрая проверка на эффект карты. Если стоимость равна 4 - это берсерк,
        // т.е. должен быть бафф на ряд. Если стоимость равна 2 - это вильдкаарл,значит должна быть связь
        // Если стоимость не пойми чему равна - значит это проблемы игрока
        _mushroom = true
        _unitsCardList.filter { it.bonus == Card.BONUS_BERSERK }
            .forEach { berserkingCard ->
                if (berserkingCard.cost == 4) {
                    val berserkedCard =
                        Card(
                            berserkingCard.cost * 3 + 2,
                            Card.TYPE_UNIT,
                            Card.BONUS_BUFF
                        )
                    editCard(berserkingCard, berserkedCard)
                } else if (berserkingCard.cost == 2) {
                    val berserkedCard =
                        Card(
                            berserkingCard.cost * 3 + 2,
                            Card.TYPE_UNIT,
                            Card.BONUS_BOND
                        )
                    removeCard(berserkingCard)
                    _unitsCardList.add(berserkedCard)
                    bondedBerserkers.add(berserkedCard)
                } else {
                    val berserkedCard =
                        Card(
                            berserkingCard.cost * 3 + 2,
                            Card.TYPE_UNIT,
                            Card.BONUS_NONE
                        )
                    editCard(berserkingCard, berserkedCard)
                }

            }
        updateScore()
    }

    fun updateScore() {
        liveScore.onNext(cardList.sumBy { it.cost })
    }
}