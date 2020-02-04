package ru.shcherbakov.realgwenthelper.ui

import ru.shcherbakov.realgwenthelper.data.Row

//TODO: Название поменяй
interface DeckRowInterface {

    fun showRowMenu(row: Row, type: Int)

    fun closeRowMenu()
}