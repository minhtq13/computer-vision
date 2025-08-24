package com.elearning.elearning_support.repositories.postgres.notification;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.elearning.elearning_support.constants.sql.SQLNotification;
import com.elearning.elearning_support.dtos.notification.ICountNewNotificationDTO;
import com.elearning.elearning_support.dtos.notification.INotificationResDTO;
import com.elearning.elearning_support.entities.notification.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query(nativeQuery = true, value = SQLNotification.GET_LIST_NOTIFICATION)
    List<INotificationResDTO> getListNotification(Long userId);

    @Query(nativeQuery = true, value = SQLNotification.GET_PAGE_NOTIFICATION)
    Page<INotificationResDTO> getPageNotification(Long userId, Pageable pageable);

    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = SQLNotification.UPDATE_IS_NEW_NOTIFICATION_BY_USER_ID)
    void updateIsNewNotifications(Long userId);

    @Query(nativeQuery = true, value = SQLNotification.COUNT_NEW_NOTIFICATIONS_BY_USER_ID)
    ICountNewNotificationDTO countNewNotifications(Long userId);

}
