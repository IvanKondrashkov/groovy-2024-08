package ru.otus.homework.repository

import java.util.concurrent.atomic.AtomicLong

trait TaskStorage {
    static def tasks = [:]
    static AtomicLong currentTaskId = new AtomicLong()

    void clearTasks() {
        currentTaskId = new AtomicLong()
        tasks.clear()
    }
}