package ru.shcherbakov.realgwenthelper

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.CoreMatchers.`is`
import org.junit.Test
import ru.shcherbakov.realgwenthelper.data.Card
import ru.shcherbakov.realgwenthelper.data.Row

class RowTest {

    @Test
    fun row_AddingThreeSimpleCards_ReturnsTrue() {
        val row = Row()
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(3))
    }

    @Test
    fun row_TestingBuff_ReturnsFour() {
        var row = Row()
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.hornActive = true
        assertThat(row.cardList.sumBy { it.cost }, `is`(4))

        row = Row()
        row.hornActive = true
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(4))

        row = Row()
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.hornActive = true
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(4))
    }

    @Test
    fun row_TestingWeather_ReturnsTwo() {
        var row = Row()
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.badWeather = true
        assertThat(row.cardList.sumBy { it.cost }, `is`(2))

        row = Row()
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.badWeather = true
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(2))

        row = Row()
        row.badWeather = true
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(2))
    }

    @Test
    fun row_TestingBuffWithHero_ReturnsTwelve() {
        var row = Row()
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
        row.hornActive = true
        assertThat(row.cardList.sumBy { it.cost }, `is`(12))
    }

    @Test
    fun row_TestingWeatherWithHero_ReturnsTwelve() {
        var row = Row()
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
        row.badWeather = true
        assertThat(row.cardList.sumBy { it.cost }, `is`(11))
    }

    @Test
    fun row_TestingWeatherAndBuff_ReturnsTwo() {
        var row = Row()
        row.badWeather = true
        row.hornActive = true
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(2))
    }

    @Test
    fun row_TestingBadWeatherWithBran_ReturnsFive() {
        var row = Row()
        row.badWeather = true
        row.bran = true
        row.addCard(Card(10, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(5))
    }

    @Test
    fun row_TestingBonds_ReturnsForty() {
        val row = Row()
        val mainCard = Card(4, Card.TYPE_UNIT, Card.BONUS_BOND)
        row.addCard(mainCard)
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), mainCard)
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), mainCard)
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(40))
    }

    @Test
    fun row_TestingBuff_ReturnsNine() {
        val row = Row()
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BUFF))
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(9))
    }

    @Test
    fun row_TestingBuffWithHero_ReturnsNineteen() {
        val row = Row()
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BUFF))
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_NONE))
        row.addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
        assertThat(row.cardList.sumBy { it.cost }, `is`(19))
    }

    @Test
    fun row_TestingMushrooms_ReturnsFiftyFour() {
        val row = Row()
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(2, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(2, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.enableMushroom()
        assertThat(row.cardList.sumBy { it.cost }, `is`(54))
    }

    @Test
    fun row_TestingMushroomsWithErmin_ReturnsSixtyFour() {
        val row = Row()
        row.addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(2, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(2, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(1, Card.TYPE_UNIT, Card.BONUS_BERSERK))
        row.addCard(Card(10, Card.TYPE_HERO, Card.BONUS_MUSHROOM))
        row.enableMushroom()
        assertThat(row.cardList.sumBy { it.cost }, `is`(64))
    }

    @Test
    fun row_RealRow_ReturnsHundred() {
        val row = Row()
        row.apply {
            val blueStripe = Card(4, Card.TYPE_UNIT, Card.BONUS_BOND)
            addCard(blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(8, Card.TYPE_HERO, Card.BONUS_BUFF))
            addCard(Card(7, Card.TYPE_UNIT, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(15, Card.TYPE_HERO, Card.BONUS_NONE))
        }
        assertThat(row.cardList.sumBy { it.cost }, `is`(100))
    }

    @Test
    fun row_RealRowScorched_ReturnsSixtyOne() {
        val row = Row()
        row.apply {
            val blueStripe = Card(4, Card.TYPE_UNIT, Card.BONUS_BOND)
            addCard(blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(7, Card.TYPE_UNIT, Card.BONUS_NONE))
            addCard(Card(8, Card.TYPE_HERO, Card.BONUS_BUFF))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(15, Card.TYPE_HERO, Card.BONUS_NONE))
            scorch()
        }
        assertThat(row.cardList.sumBy { it.cost }, `is`(61))
    }

    @Test
    fun row_RealRowWeatherBran_ReturnsFifty() {
        val row = Row()
        row.bran = true
        row.badWeather = true
        row.apply {
            val blueStripe = Card(4, Card.TYPE_UNIT, Card.BONUS_BOND)
            addCard(blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(4, Card.TYPE_UNIT, Card.BONUS_BOND), blueStripe)
            addCard(Card(7, Card.TYPE_UNIT, Card.BONUS_NONE))
            addCard(Card(8, Card.TYPE_HERO, Card.BONUS_BUFF))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(10, Card.TYPE_HERO, Card.BONUS_NONE))
            addCard(Card(15, Card.TYPE_HERO, Card.BONUS_NONE))
        }
        assertThat(row.cardList.sumBy { it.cost }, `is`(78))
    }
}