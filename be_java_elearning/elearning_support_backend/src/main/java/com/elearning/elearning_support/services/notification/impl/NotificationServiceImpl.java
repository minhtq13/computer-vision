package com.elearning.elearning_support.services.notification.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.dtos.notification.FCMTokenRegisterDTO;
import com.elearning.elearning_support.dtos.notification.ICountNewNotificationDTO;
import com.elearning.elearning_support.dtos.notification.NotificationFCMReqDTO;
import com.elearning.elearning_support.dtos.notification.INotificationResDTO;
import com.elearning.elearning_support.entities.notification.Notification;
import com.elearning.elearning_support.enums.notification.NotificationContentEnum;
import com.elearning.elearning_support.enums.notification.NotificationObjectTypeEnum;
import com.elearning.elearning_support.repositories.postgres.notification.NotificationRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.services.notification.NotificationService;
import com.elearning.elearning_support.utils.DateUtils;
import com.elearning.elearning_support.utils.auth.AuthUtils;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    @Override
    public List<INotificationResDTO> getListNotification() {
        return notificationRepository.getListNotification(AuthUtils.getCurrentUserId());
    }

    @Override
    public Page<INotificationResDTO> getPageNotification(Pageable pageable) {
        return notificationRepository.getPageNotification(AuthUtils.getCurrentUserId(), pageable);
    }

    @Override
    public void saveNotification(Long senderId, Long receiverId, String content, NotificationContentEnum contentType, String objectIdentifier,
        NotificationObjectTypeEnum objectType) {
        Notification notification = Notification.builder()
            .userId(receiverId)
            .contentType(contentType.getType())
            .content(content)
            .title(contentType.getTitle())
            .createdBy(senderId) // null if notification from system
            .createdAt(DateUtils.getCurrentDateTime())
            .sentAt(DateUtils.getCurrentDateTime())
            .objectIdentifier(objectIdentifier)
            .objectType(objectType.getType())
            .isNew(Boolean.TRUE)
            .build();
        notificationRepository.save(notification);
    }

    @Transactional
    @Override
    public void saveListNotification(Set<Long> userIds, String content, NotificationContentEnum contentType, String objectIdentifier,
        NotificationObjectTypeEnum objectType) {
        List<Notification> lstNotifications = userIds.stream().map(userId ->
                Notification.builder()
                    .userId(userId)
                    .contentType(contentType.getType())
                    .content(content)
                    .title(contentType.getTitle())
                    .createdBy(AuthUtils.getCurrentUserId())
                    .createdAt(DateUtils.getCurrentDateTime())
                    .sentAt(DateUtils.getCurrentDateTime())
                    .objectIdentifier(objectIdentifier)
                    .objectType(objectType.getType())
                    .isNew(Boolean.TRUE)
                    .build())
            .collect(Collectors.toList());
        notificationRepository.saveAll(lstNotifications);
    }

    @Override
    public void sendFCMNotification(NotificationFCMReqDTO reqDTO) {
        try {
            Map<String, String> mapData = new HashMap<>();
            mapData.put("title", reqDTO.getTitle());
            mapData.put("content", reqDTO.getContent());
            mapData.put("target", "TU" + reqDTO.getTargetUserId());
            Message message = Message.builder()
                .setToken(reqDTO.getFcmToken())
                .putAllData(mapData)
                .build();
            firebaseMessaging.send(message);
        } catch (FirebaseMessagingException exception) {
            log.error(MessageConst.EXCEPTION_LOG_FORMAT, exception.getMessage(), exception.getCause(), exception);
        }
        log.info("=== SENT FCM MESSAGE TO {} ===", reqDTO.getFcmToken());
    }

    @Override
    public void registerFCMToken(FCMTokenRegisterDTO registerDTO) {
        userRepository.updateUserFCMToken(AuthUtils.getCurrentUserId(), registerDTO.getFcmToken());
    }

    @Override
    public void updateIsNewNotification() {
        notificationRepository.updateIsNewNotifications(AuthUtils.getCurrentUserId());
    }

    @Override
    public ICountNewNotificationDTO countNewNotifications() {
        return notificationRepository.countNewNotifications(AuthUtils.getCurrentUserId());
    }
}
