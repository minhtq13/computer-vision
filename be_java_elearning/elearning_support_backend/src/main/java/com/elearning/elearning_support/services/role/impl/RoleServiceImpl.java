package com.elearning.elearning_support.services.role.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elearning.elearning_support.constants.message.errorKey.ErrorKey;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst;
import com.elearning.elearning_support.constants.message.messageConst.MessageConst.Resources;
import com.elearning.elearning_support.dtos.role.IRoleDetailDTO;
import com.elearning.elearning_support.dtos.role.RoleResponse;
import com.elearning.elearning_support.dtos.users.AssignBaseRolesRequest;
import com.elearning.elearning_support.dtos.users.AssignCustomRolesRequest;
import com.elearning.elearning_support.dtos.role.RoleReqDTO;
import com.elearning.elearning_support.entities.permission.Permission;
import com.elearning.elearning_support.entities.role.Role;
import com.elearning.elearning_support.entities.users.User;
import com.elearning.elearning_support.entities.users.UserRole;
import com.elearning.elearning_support.enums.commons.DeletedFlag;
import com.elearning.elearning_support.enums.users.UserTypeEnum;
import com.elearning.elearning_support.exceptions.BadRequestException;
import com.elearning.elearning_support.exceptions.exceptionFactory.ExceptionFactory;
import com.elearning.elearning_support.repositories.postgres.permission.PermissionRepository;
import com.elearning.elearning_support.repositories.postgres.role.RoleRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRepository;
import com.elearning.elearning_support.repositories.postgres.users.UserRoleRepository;
import com.elearning.elearning_support.services.role.RoleService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final ExceptionFactory exceptionFactory;

    private final UserRoleRepository userRoleRepository;

    private final UserRepository userRepository;

    @Transactional()
    @Override
    public Role createRole(RoleReqDTO roleReqDTO) {
        if (roleRepository.existsByCode(roleReqDTO.getCode())) {
            throw exceptionFactory.resourceExistedException(MessageConst.Role.EXISTED_BY_CODE,
                    MessageConst.Resources.ROLE, MessageConst.RESOURCE_EXISTED, ErrorKey.Role.CODE,
                    roleReqDTO.getCode());
        }
        Role role = new Role();
        role.setCode(roleReqDTO.getCode());
        role.setName(roleReqDTO.getName());
        role.setDescription(roleReqDTO.getDescription());
        role.setIsDefault(roleReqDTO.getIsDefault());
        role.setDisplayedName(roleReqDTO.getDisplayedName());
        role.setRoleBaseId(roleReqDTO.getRoleBaseId() != null ? roleReqDTO.getRoleBaseId() : 2);

        if (roleReqDTO.getPermissionIds() != null && !roleReqDTO.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = new HashSet<>(
                    permissionRepository.findAllById(roleReqDTO.getPermissionIds()));
            role.setPermissions(permissions);
        }
        return roleRepository.save(role);
    }

    @Transactional()
    @Override
    public Role updateRole(Long id, RoleReqDTO roleReqDTO) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> exceptionFactory.resourceNotFoundException(MessageConst.Role.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.ROLE, ErrorKey.Role.ID,
                        String.valueOf(id)));

        role.setCode(roleReqDTO.getCode());
        role.setName(roleReqDTO.getName());
        role.setDescription(roleReqDTO.getDescription());
        role.setIsDefault(roleReqDTO.getIsDefault());
        role.setDisplayedName(roleReqDTO.getDisplayedName());
        role.setRoleBaseId(roleReqDTO.getRoleBaseId() != null ? roleReqDTO.getRoleBaseId() : 2);

        if (roleReqDTO.getPermissionIds() != null) {
            Set<Long> targetIds = new HashSet<>(roleReqDTO.getPermissionIds());
            // 1) remove những permission không còn trong target
            role.getPermissions().removeIf(p -> !targetIds.contains(p.getId()));
            // 2) add những permission mới
            Set<Long> existingIds = role.getPermissions().stream()
                    .map(Permission::getId).collect(java.util.stream.Collectors.toSet());
            List<Long> toAddIds = roleReqDTO.getPermissionIds().stream()
                    .filter(pid -> !existingIds.contains(pid)).collect(Collectors.toList());
            if (!toAddIds.isEmpty()) {
                role.getPermissions().addAll(permissionRepository.findAllById(toAddIds));
            }
        }
        return roleRepository.save(role);
    }

    @Transactional()
    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> exceptionFactory.resourceNotFoundException(MessageConst.Role.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.ROLE, ErrorKey.Role.ID,
                        String.valueOf(id)));
        roleRepository.delete(role);
    }

    @Override
    public Role getRole(Long id) {
        return roleRepository.findById(id).orElseThrow(
                () -> exceptionFactory.resourceNotFoundException(MessageConst.Role.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, MessageConst.Resources.ROLE, ErrorKey.Role.ID,
                        String.valueOf(id)));
    }

    @Override
    public List<RoleResponse> getAllRoles(String type) {
        List<IRoleDetailDTO> roleDetails = roleRepository.findRolesByType(type);
        return roleDetails.stream().map(RoleResponse::new).collect(Collectors.toList());
    }

    /**
     * Find user by id and deleted_flag
     */
    private User findUserById(Long id) {
        return userRepository.findByIdAndDeletedFlag(id, DeletedFlag.NOT_YET_DELETED.getValue())
                .orElseThrow(() -> exceptionFactory.resourceNotFoundException(
                        MessageConst.User.USER_NOT_FOUND_ERROR_CODE, Resources.USER,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.User.ID, String.valueOf(id)));
    }

    private Integer convertRoleBaseIdToUserType(Long roleBaseId) {
        if (roleBaseId.equals(1L)) {
            return UserTypeEnum.ADMIN.getType();
        } else if (roleBaseId.equals(2L)) {
            return UserTypeEnum.STUDENT.getType();
        } else if (roleBaseId.equals(3L)) {
            return UserTypeEnum.TEACHER.getType();
        }
        throw new BadRequestException("Invalid role base id: " + roleBaseId, "Invalid role base id", ErrorKey.Role.ID);
    }

    private Long convertUserTypeToRoleBaseId(Integer userType) {
        if (userType.equals(UserTypeEnum.ADMIN.getType())) {
            return 1L;
        } else if (userType.equals(UserTypeEnum.STUDENT.getType())) {
            return 2L;
        } else if (userType.equals(UserTypeEnum.TEACHER.getType())) {
            return 3L;
        }
        throw new BadRequestException("Invalid user type: " + userType, "Invalid user type", ErrorKey.User.ID);
    }

    @Transactional
    @Override
    public void assignCustomRoles(AssignCustomRolesRequest request) {
        List<Long> userIds = request.getUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }

        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new BadRequestException(MessageConst.User.USER_NOT_FOUND_ERROR_CODE, MessageConst.RESOURCE_NOT_FOUND,
                    ErrorKey.User.ID);
        }

        // Check if all users have the same roleBaseId
        if (users.stream().map(User::getUserType).distinct().count() > 1) {
            throw new BadRequestException(MessageConst.User.USER_NOT_MATCH_ROLE_BASE_ID,
                    MessageConst.USER_NOT_MATCH_ROLE_BASE_ID,
                    ErrorKey.User.ROLE_BASE_ID);
        }

        for (Long userId : userIds) {
            User user = findUserById(userId);
            Long roleBaseId = convertUserTypeToRoleBaseId(user.getUserType());
            List<Long> roleIds = request.getRoleIds();

            // Get current roles of the user
            List<UserRole> currentUserRoles = userRoleRepository.findAllByUserId(user.getId());
            List<Long> currentUserRoleIds = currentUserRoles.stream().map(UserRole::getRoleId)
                    .filter(roleId -> !Arrays.asList(1L, 2L, 3L).contains(roleId)).collect(Collectors.toList());

            // Roles to add
            List<Long> rolesToAdd = roleIds.stream()
                    .filter(roleId -> !currentUserRoleIds.contains(roleId))
                    .collect(Collectors.toList());

            // Roles to remove
            List<Long> rolesToRemove = currentUserRoleIds.stream()
                    .filter(roleId -> !roleIds.contains(roleId))
                    .collect(Collectors.toList());

            // Remove roles
            if (CollectionUtils.isNotEmpty(rolesToRemove)) {
                userRoleRepository.deleteAllByUserIdAndRoleIdIn(user.getId(), rolesToRemove);
            }

            // Add new roles
            for (Long roleId : rolesToAdd) {
                Role role = roleRepository.findById(roleId)
                        .orElseThrow(() -> new BadRequestException(MessageConst.Role.NOT_FOUND,
                                MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Role.ID));
                if (!roleBaseId.equals(role.getRoleBaseId())) {
                    throw new BadRequestException(MessageConst.Role.ROLE_NOT_MATCH_USER_TYPE,
                            MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Role.ID);
                }
                UserRole newUserRole = new UserRole(user.getId(), roleId);
                userRoleRepository.save(newUserRole);
            }
        }
    }

    @Transactional
    @Override
    public void assignBaseRoles(AssignBaseRolesRequest request) {
        List<Long> userIds = request.getUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }

        List<User> users = userRepository.findAllById(userIds);
        if (users.size() != userIds.size()) {
            throw new BadRequestException(MessageConst.User.USER_NOT_FOUND_ERROR_CODE, MessageConst.RESOURCE_NOT_FOUND,
                    ErrorKey.User.ID);
        }

        Long newBaseRoleId = request.getRoleId();
        Role newBaseRole = roleRepository.findById(newBaseRoleId)
                .orElseThrow(() -> new BadRequestException(MessageConst.Role.NOT_FOUND,
                        MessageConst.RESOURCE_NOT_FOUND, ErrorKey.Role.ID));

        boolean allUsersHaveNewBaseRole = users.stream()
                .allMatch(user -> convertUserTypeToRoleBaseId(user.getUserType())
                        .equals(newBaseRole.getRoleBaseId()));

        if (allUsersHaveNewBaseRole) {
            throw new BadRequestException(MessageConst.Role.ROLE_BASE_ID_EXISTED,
                    "All selected users already have this base role.", ErrorKey.Role.ID);
        }

        for (User user : users) {
            // If user already has the base role, skip
            if (convertUserTypeToRoleBaseId(user.getUserType()).equals(newBaseRole.getRoleBaseId())) {
                continue;
            }

            // Remove all existing roles before assigning new one
            userRoleRepository.deleteAllByUserId(user.getId());
            userRoleRepository.flush(); // Ensure delete is executed before insert

            Integer userType = convertRoleBaseIdToUserType(newBaseRoleId);
            userRepository.updateUserType(user.getId(), userType);

            // Assign new base role
            userRoleRepository.save(new UserRole(user.getId(), newBaseRoleId));

        }
    }

}
