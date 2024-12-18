package ru.otus.homework.service.impl

import io.micronaut.data.model.Sort
import jakarta.inject.Inject
import jakarta.inject.Singleton
import io.micronaut.transaction.annotation.Transactional
import ru.otus.homework.model.Action
import ru.otus.homework.dto.ActionInfo
import io.micronaut.data.model.Pageable
import ru.otus.homework.mapper.ActionMapper
import ru.otus.homework.service.ActionService
import ru.otus.homework.repository.UserRepository
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
    UserRepository userRepository
    @Inject
    ActionMapper actionMapper

    @Transactional
    ActionInfo save(Action action, UUID userId) {
        def actions = findAllByStartDateAndEndDate(userId, action.startDate, action.endDate)
        if (actions.isEmpty()) {
            userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException('User not found!')
            )
            def actionDb = actionRepository.save(action)
            return actionMapper.toActionInfo(actionDb)
        }
        throw new EntityNotValidException('Action create, time not valid!')
    }

    @Transactional
    ActionInfo updateById(Action action, UUID userId, UUID actionId) {
        def actions = findAllByStartDateAndEndDate(userId, action.startDate, action.endDate)
        if (actions.isEmpty()) {
            userRepository.findById(userId).orElseThrow(
                    () -> new EntityNotFoundException('User not found!')
            )
            def actionDb = actionRepository.findById(actionId).orElseThrow(
                    () -> new EntityNotFoundException('Action not found!')
            )
            action.setId(actionDb.id)
            actionDb = actionRepository.update(action)
            return actionMapper.toActionInfo(actionDb)
        }
        throw new EntityNotValidException('Action update, time not valid!')
    }

    @Transactional(readOnly = true)
    ActionInfo findById(UUID userId, UUID actionId) {
        def actionDb = actionRepository.findByInitiator_IdAndId(userId, actionId).orElseThrow(
                () -> new EntityNotFoundException('Action not found!')
        )
        return actionMapper.toActionInfo(actionDb)
    }

    @Transactional(readOnly = true)
    List<ActionInfo> findAll(UUID userId, Pageable pageable) {
        def defaultPageable = Pageable.from(pageable.number, pageable.size, Sort.of(Sort.Order.asc('id')))
        pageable = pageable.sort.isSorted() ? pageable : defaultPageable
        return actionRepository.findByInitiator_Id(userId, pageable).getContent().stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @Transactional
    void deleteById(UUID userId, UUID actionId) {
        actionRepository.findByInitiator_IdAndId(userId, actionId).orElseThrow(
                () -> new EntityNotFoundException('Action not found!')
        )
        actionRepository.deleteById(actionId)
    }

    @Override
    @Transactional(readOnly = true)
    List<ActionInfo> findAllByDate(UUID userId, LocalDateTime date) {
        return actionRepository.findAllByDate(userId, date).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    List<ActionInfo> findAllByStartDateAndEndDate(UUID userId, LocalDateTime startDate, LocalDateTime endDate) {
        return actionRepository.findAllByStartDateAndEndDate(userId, startDate, endDate).stream()
                .map(it -> actionMapper::toActionInfo(it))
                .collect()
    }

    @Override
    @Transactional(readOnly = true)
    long countAllByDate(UUID userId, LocalDateTime date) {
        return actionRepository.countAllByDate(userId, date)
    }
}