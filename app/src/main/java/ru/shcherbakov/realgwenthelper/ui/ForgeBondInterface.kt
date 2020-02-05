package ru.shcherbakov.realgwenthelper.ui

import ru.shcherbakov.realgwenthelper.data.Card

interface ForgeBondInterface {

    fun forgeBond(card: Card, bond: ArrayList<Card>? = null)
}