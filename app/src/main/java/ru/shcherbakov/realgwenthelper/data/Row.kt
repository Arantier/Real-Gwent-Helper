package ru.shcherbakov.realgwenthelper.data

import io.reactivex.subjects.PublishSubject

class Row {
    private val _heroesCardList = ArrayList<Card>()
    private val _unitsCardList = ArrayList<Card>()
    private val bondedBerserkers = ArrayList<Card>()
    private val bondedCardsList = ArrayList<ArrayList<Card>>().apply { add(bondedBerserkers) }
    private var isBadWeather = false
    private var isHorny = false
    private var isFuckingBran = false

    val liveScore = PublishSubject.create<Int>().apply { 0 }

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

    val cardList: Iterable<Card>
        get() {
            val _cardList = ArrayList<Card>()
            _heroesCardList.forEach {
                _cardList.add(it.copy())
            }
            val isHornActive = isHorny || _unitsCardList.any { it.bonus == Card.BONUS_HORN } ||
                    _heroesCardList.any { it.bonus == Card.BONUS_HORN }
            val numberOfBuffs = _unitsCardList.count { it.bonus == Card.BONUS_BUFF } +
                    _heroesCardList.count { it.bonus == Card.BONUS_BUFF }
            _unitsCardList.forEach { card ->
                var cost = card.cost
                if (isBadWeather && isFuckingBran) {
                    cost /= 2
                } else if (isBadWeather) {
                    cost = 1
                }
                // Применяем бонус связи
                cost *= bondedCardsList.find { it.contains(card) }?.count() ?: 1
                // Добавляем бонусы от поддержки ряда
                cost += numberOfBuffs
                // Если карта сама поддерживает ряд, то вычитаем её значение
                if (card.bonus == Card.BONUS_BUFF) {
                    cost -= 1
                }
                // Если карта не имеет бонус рога и активен командирский рог или сам рог глобален,
                // то умножаем цену карты на 2
                if ((card.bonus != Card.BONUS_HORN || isHorny) && isHornActive) {
                    cost *= 2
                }
                _cardList.add(Card(cost, card.type, card.bonus))
            }
            return _cardList
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
    }

    fun editCard(oldCard: Card, editedCard: Card) {
        removeCard(oldCard)
        addCard(editedCard)
    }

    fun scorch() {
        val isHornActive = isHorny || _unitsCardList.any { it.bonus == Card.BONUS_HORN } ||
                _heroesCardList.any { it.bonus == Card.BONUS_HORN }
        val numberOfBuffs = _unitsCardList.count { it.bonus == Card.BONUS_BUFF } +
                _heroesCardList.count { it.bonus == Card.BONUS_BUFF }
        val cardMap = _unitsCardList.map { card ->
            var cost = card.cost
            if (isBadWeather && isFuckingBran) {
                cost /= 2
            } else if (isBadWeather) {
                cost = 1
            }
            // Применяем бонус связи
            cost *= bondedCardsList.find { it.contains(card) }?.count() ?: 1
            // Добавляем бонусы от поддержки ряда
            cost += numberOfBuffs
            // Если карта сама поддерживает ряд, то вычитаем её значение
            if (card.bonus == Card.BONUS_BUFF) {
                cost -= 1
            }
            // Если карта не имеет бонус рога и активен командирский рог или сам рог глобален,
            // то умножаем цену карты на 2
            if ((card.bonus != Card.BONUS_HORN || isHorny) && isHornActive) {
                cost *= 2
            }
            Pair(card, cost)
        }
        val maxCost = cardMap.maxBy { it.second }?.second ?: 0
        cardMap.filter { it.second == maxCost }
            .forEach {
                removeCard(it.first)
            }
    }

    fun enableMushroom() {
        // Тут небольшая хитрая проверка на эффект карты. Если стоимость равна 4 - это берсерк,
        // т.е. должен быть бафф на ряд. Если стоимость равна 2 - это вильдкаарл,значит должна быть связь
        // Если стоимость не пойми чему равна - значит это проблемы игрока
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
    }

    fun updateScore() {
        liveScore.onNext(cardList.sumBy { it.cost })
    }
}