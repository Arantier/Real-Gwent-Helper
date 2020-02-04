package ru.shcherbakov.realgwenthelper.data

import io.reactivex.subjects.BehaviorSubject

data class Player(
    val name: String,
    var lives: Int = 2,
    var score: BehaviorSubject<Int> = BehaviorSubject.createDefault(0)
)