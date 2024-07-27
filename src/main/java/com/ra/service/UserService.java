package com.ra.service;

import com.ra.model.dto.req.ChangePasswordRequest;
import com.ra.model.dto.req.UserEdit;
import com.ra.model.entity.User;

import java.util.List;

public interface UserService {
User editUser(UserEdit userEdit);
boolean  changePassword(ChangePasswordRequest changePasswordRequest);
}
