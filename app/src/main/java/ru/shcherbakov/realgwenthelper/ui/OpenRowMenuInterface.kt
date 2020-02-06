package ru.shcherbakov.realgwenthelper.ui

import ru.shcherbakov.realgwenthelper.data.Row

interface OpenRowMenuInterface {

    fun showRowMenu(row: Row, type: Int)

    fun closeRowMenu()
}