package ru.otus.homework.service.impl

import io.micronaut.data.model.Sort
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.ReadOnly
import io.micronaut.transaction.annotation.Transactional
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.ActionMapper
import ru.otus.homework.service.ActionService
import ru.otus.homework.repository.ActionRepository
import ru.otus.homework.exception.EntityNotValidException
import ru.otus.homework.exception.EntityNotFoundException
import java.time.LocalDateTime

@Singleton
class ActionServiceImpl implements ActionService {
    @Inject
    TaskServiceImpl taskService
    @Inject
    ActionRepository actionRepository
    @Inject
    ActionMapper actionMapper

    @Transactional
    ActionInfo save(Action action) {
        def actions = findAllByStartDateAndEndDate(action.startDate, action.endDate)
        if (actions.isEmpty()) {
            def actionDb = actionRepository.save(action)
            def actionInfo = actionMapper.toActionInfo(actionDb)
            return actionInfo
        }
        throw new EntityNotValidException('Action create, time not valid!')
    }

    @Transactional
    ActionInfo updateById(Action action, UUID actionId) {
        def actions = findAllByStartDateAndEndDate(action.startDate, action.endDate)
        if (actions.isEmpty()) {
            def actionDb = actionRepository.findById(actionId).orElseThrow(
                    () -> new EntityNotFoundException('Action not found!')
            )
            action.setId(actionDb.id)
            actionDb = actionRepository.update(action)
            def actionInfo = actionMapper.toActionInfo(actionDb)
            return actionInfo
        }
        throw new EntityNotValidException('Action update, time not valid!')
    }

    @ReadOnly
    ActionInfo findById(UUID actionId) {
        def actionDb = actionRepository.findById(actionId).orElseThrow(
                () -> new EntityNotFoundException('Action not found!')
        )
        return actionMapper.toActionInfo(actionDb)
    }

    @ReadOnly
    List<ActionInfo> findAll(Pageable pageable) {
        def defaultPageable = Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc('id')))
        pageable = pageable.sort.isSorted() ? pageable : defaultPageable
        return actionRepository.findAll(pageable).getContent().stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @Transactional
    void deleteById(UUID actionId) {
        actionRepository.findById(actionId).orElseThrow(
                () -> new EntityNotFoundException('Action not found!')
        )
        actionRepository.deleteById(actionId)
    }

    @ReadOnly
    @Override
    List<ActionInfo> findAllByDate(LocalDateTime date) {
        return actionRepository.findAllByDate(date).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @ReadOnly
    @Override
    List<ActionInfo> findAllByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate) {
        return actionRepository.findAllByStartDateAndEndDate(startDate, endDate).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @ReadOnly
    @Override
    long countAllByDate(LocalDateTime date) {
        return actionRepository.countAllByDate(date)
    }
}