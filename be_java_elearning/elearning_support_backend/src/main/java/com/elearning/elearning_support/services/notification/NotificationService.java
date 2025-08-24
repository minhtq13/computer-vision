package com.elearning.elearning_support.services.notification;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.elearning.elearning_support.dtos.notification.FCMTokenRegisterDTO;
import com.elearning.elearning_support.dtos.notification.ICountNewNotificationDTO;
import com.elearning.elearning_support.dtos.notification.NotificationFCMReqDTO;
import com.elearning.elearning_support.dtos.notification.INotificationResDTO;
import com.elearning.elearning_support.enums.notification.NotificationContentEnum;
import com.elearning.elearning_support.enums.notification.NotificationObjectTypeEnum;

@Service
public interface NotificationService {

    List<INotificationResDTO> getListNotification();

    Page<INotificationResDTO> getPageNotification(Pageable pageable);

    void saveNotification(Long senderId, Long receiverId, String content, NotificationContentEnum contentType, String objectIdentifier, NotificationObjectTypeEnum objectType);

    void saveListNotification(Set<Long> userIds, String content, NotificationContentEnum contentType, String objectIdentifier, NotificationObjectTypeEnum objectType);

    /**
     * send a notification through fcm
     */
    void sendFCMNotification(NotificationFCMReqDTO reqDTO);

    void registerFCMToken(FCMTokenRegisterDTO registerDTO);

    /**
     * Update isNew of a notification => false
     */
    void updateIsNewNotification();

    /**
     * count new notifications
     */
    ICountNewNotificationDTO countNewNotifications();

}
