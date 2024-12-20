package com.alwx.backend.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.alwx.backend.repositories.UserActionRepository;
import com.alwx.backend.repositories.UserRepository;
import com.alwx.backend.utils.jwt.JwtTokenUtil;

import lombok.RequiredArgsConstructor;

import com.alwx.backend.models.User;
import com.alwx.backend.models.UserAction;
import com.alwx.backend.models.enums.Action;


/**
 * Сервис для логирования действий пользователей.
 */
@Service
@RequiredArgsConstructor
public class UserActionService {
    private final UserRepository userRepository;
    private final UserActionRepository userActionRepository;
    private final JwtTokenUtil jwtTokenUtil;

    /**
     * Логирует действие пользователя.
     * @param action Действие, которое было выполнено
     * @param token Токен аутентификации
     * @param vehicleId ID автомобиля, над которым было выполнено действие
     */
    public void logAction(Action action, String token, Long vehicleId) {
        if(jwtTokenUtil.getUsername(token) != null) {
            User user = userRepository.findByUsername(jwtTokenUtil.getUsername(token)).get();
                UserAction userAction = new UserAction();
                userAction.setUser(user);
                userAction.setVehicleId(vehicleId);
                userAction.setAction(action);
                userAction.setTimestamp(LocalDateTime.now());
                userActionRepository.save(userAction);
        }
    }
}
