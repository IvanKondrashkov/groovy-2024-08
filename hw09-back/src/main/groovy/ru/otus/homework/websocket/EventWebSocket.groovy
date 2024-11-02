package ru.otus.homework.websocket

import groovy.util.logging.Slf4j
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import jakarta.inject.Inject
import org.reactivestreams.Publisher
import ru.otus.homework.dto.ActionInfo
import ru.otus.homework.dto.EventInfo
import ru.otus.homework.dto.MessageInfo
import ru.otus.homework.service.impl.ActionServiceImpl

@Slf4j
@ServerWebSocket("/ws/events")
class EventWebSocket {
    @Inject
    WebSocketBroadcaster broadcaster
    @Inject
    ActionServiceImpl actionService

    @OnOpen
        Publisher<String> onOpen(WebSocketSession session) {
            log.info("onOpen run, session id={}", session.id)
        return broadcaster.broadcast('WebSocket is open!')
    }

    @OnMessage
    Publisher<List<EventInfo>> onMessage(WebSocketSession session, MessageInfo messageInfo) {
        log.info("onMessage run, session id={}", session.id)
        List<ActionInfo> actions = actionService.findAllByDate(messageInfo.message_ts)
        List<EventInfo> events = actions.stream()
                .map(it -> new EventInfo(it.id, it.name, it.startDate))
                .collect()
        log.info("Message send, message_ts={}", messageInfo.message_ts)
        return broadcaster.broadcast(events)
    }

    @OnClose
    Publisher<String> onClose(WebSocketSession session) {
        log.info("onClose run, session id={}", session.id)
        return broadcaster.broadcast('WebSocket is close!')
    }
}