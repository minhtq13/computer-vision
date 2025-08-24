package com.elearning.elearning_support.controllers.restful.notification;

import java.util.Collections;
import java.util.List;

import com.elearning.elearning_support.components.BaseController;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.elearning.elearning_support.dtos.notification.FCMTokenRegisterDTO;
import com.elearning.elearning_support.dtos.notification.ICountNewNotificationDTO;
import com.elearning.elearning_support.dtos.notification.NotificationFCMReqDTO;
import com.elearning.elearning_support.dtos.notification.INotificationResDTO;
import com.elearning.elearning_support.services.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping()
    @Operation(description = "Lấy danh sách thông báo của người dùng đang đăng nhập")
    public List<INotificationResDTO> getListNotification() {
        return notificationService.getListNotification();
    }

    @GetMapping("/page")
    @Operation(description = "Lấy danh sách phân trang thông báo của người dùng đang đăng nhập")
    public Page<INotificationResDTO> getPageNotification(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                         @RequestParam(name = "size", required = false, defaultValue = "10") Integer size,
                                                         @RequestParam(name = "sort", required = false, defaultValue = "id,desc") String sort) {

        Pageable pageable = BaseController.getPageable(page, size, Collections.singletonList(sort));
        return notificationService.getPageNotification(pageable);
    }

    @PostMapping("/fcm/send")
    @Operation(description = "Bắn bản ghi thông qua FCM với một 1 FCM Token")
    public void sendFCMNotification(@RequestBody NotificationFCMReqDTO reqDTO) {
        notificationService.sendFCMNotification(reqDTO);
    }

    @PutMapping("/fcm/register-token")
    @Operation(description = "Đăng ký FCM Token cho user")
    public void registerNotificationToken(@RequestBody @Validated FCMTokenRegisterDTO registerDTO) {
        notificationService.registerFCMToken(registerDTO);
    }

    @PutMapping("/new-status/update")
    @Operation(description = "Chuyển trạng thái isNew => false")
    public void updateIsNewNotification() {
        notificationService.updateIsNewNotification();
    }

    @GetMapping("/new-status/count")
    @Operation(description = "Đếm số thông báo mới")
    public ICountNewNotificationDTO countNewNotifications() {
        return notificationService.countNewNotifications();
    }

}
