package com.elearning.elearning_support.services.permission.impl;

import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.dtos.permission.PermissionReqDTO;
import com.elearning.elearning_support.entities.permission.Permission;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.permission.PermissionRepository;
import com.elearning.elearning_support.services.permission.PermissionService;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    private final ExceptionFactory exceptionFactory;

    @Override
    @Transactional
    public Permission createPermission(PermissionReqDTO permissionReqDTO) {
        Permission permission = new Permission();
        if (permissionRepository.existsByCode(permissionReqDTO.getCode())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Permission.EXISTED_BY_CODE,
                    MessageConst.Resources.PERMISSION,
                    MessageConst.RESOURCE_EXISTED,
                    ErrorKey.Permission.CODE, permissionReqDTO.getCode());
        }
        permission.setCode(permissionReqDTO.getCode());
        permission.setName(permissionReqDTO.getName());
        permission.setDescription(permissionReqDTO.getDescription());
        if (permissionReqDTO.getParentId() == null) {
            permission.setParentId(-1L);
        } else {
            permission.setParentId(permissionReqDTO.getParentId());
        }
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public Permission updatePermission(Long id, PermissionReqDTO permissionReqDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Permission.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.PERMISSION,
                        ErrorKey.Permission.ID, String.valueOf(id)));

        permission.setCode(permissionReqDTO.getCode());
        permission.setName(permissionReqDTO.getName());
        permission.setDescription(permissionReqDTO.getDescription());
        if (permissionReqDTO.getParentId() == null) {
            permission.setParentId(-1L);
        } else {
            permission.setParentId(permissionReqDTO.getParentId());
        }
        return permissionRepository.save(permission);
    }

    @Override
    @Transactional
    public void deletePermission(Long id) {
        permissionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Permission.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.PERMISSION,
                        ErrorKey.Permission.ID, String.valueOf(id)));
        permissionRepository.deleteById(id);
    }

    @Override
    public Permission getPermission(Long id) {
        return permissionRepository.findById(id)
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(MessageConst.Permission.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.PERMISSION,
                        ErrorKey.Permission.ID, String.valueOf(id)));
    }

    @Override
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    @Override
    public List<Permission> getGroupedPermissions() {
        // This is a simplified version. A more complex logic might be needed to handle
        // the hierarchy
        return permissionRepository.findAll().stream()
                .filter(p -> p.getParentId() != null && p.getParentId() == -1L)
                .collect(Collectors.toList());
    }
}
