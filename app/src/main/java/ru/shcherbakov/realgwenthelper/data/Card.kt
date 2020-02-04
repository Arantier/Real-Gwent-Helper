package ru.shcherbakov.realgwenthelper.data

data class Card(
    val cost: Int,
    val type: Int,
    val bonus: Int
) {
    companion object {
        val TYPE_UNIT = 0
        val TYPE_HERO = 1

        val BONUS_NONE = 0
        val BONUS_BERSERK = 1
        val BONUS_BOND = 2
        val BONUS_BUFF = 3
        val BONUS_HORN = 4
        val BONUS_MUSHROOM = 5
    }
}