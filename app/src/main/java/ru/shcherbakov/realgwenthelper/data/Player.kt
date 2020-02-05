package ru.shcherbakov.realgwenthelper.data

import io.reactivex.subjects.BehaviorSubject

data class Player(
    val name: String,
    var lives: Int = 2,
    var liveScore: BehaviorSubject<Int> = BehaviorSubject.createDefault(0),
    var score: Int = 0,
    val passed: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false),
    var isPassed: Boolean = false
)