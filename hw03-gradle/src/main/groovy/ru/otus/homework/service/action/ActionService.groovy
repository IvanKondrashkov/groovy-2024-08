package ru.otus.homework.service.action

import ru.otus.homework.model.Action

import java.time.LocalDate

interface ActionService {
    Action createAction(Action action)
    void deleteAction(Long id)
    Action findAction(Long id)
    List<Action> findAllActionByDate(LocalDate date)
    int numbersOfActionByDate(LocalDate date)
}
