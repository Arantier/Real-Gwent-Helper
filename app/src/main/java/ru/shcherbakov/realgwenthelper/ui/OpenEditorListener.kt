package ru.shcherbakov.realgwenthelper.ui

import ru.shcherbakov.realgwenthelper.data.Card

interface OpenEditorListener {
    fun addCard()

    fun editCard(card: Card)
}