package cc.bexerlmao.xcto.user.service.impl;

import cc.bexerlmao.xcto.user.exception.ApiException;
import cc.bexerlmao.xcto.user.mapper.UserMapper;
import cc.bexerlmao.xcto.user.pojo.UserBo;
import cc.bexerlmao.xcto.user.service.UserService;
import cc.bexerlmao.xcto.user.util.Sha256Util;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    /** 注册邀请码，通过环境变量 XCTO_INVITE_CODE（或配置 xcto.invite-code）注入；为空则关闭注册 */
    @Value("${xcto.invite-code:}")
    private String inviteCode;

    private final UserMapper userMapper;

    public UserServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public UserBo register(String username, String password, String inviteCode) {
        if (username == null || username.isBlank() || password == null || password.length() < 6) {
            throw new ApiException(400, "用户名不能为空，密码长度至少 6 位");
        }
        if (this.inviteCode == null || this.inviteCode.isBlank()) {
            throw new ApiException(403, "注册未开放（未配置邀请码）");
        }
        if (inviteCode == null || !this.inviteCode.equals(inviteCode.trim())) {
            throw new ApiException(400, "邀请码错误");
        }
        if (userMapper.findByUsername(username) != null) {
            throw new ApiException(409, "用户名已存在");
        }
        userMapper.insertUser(username, Sha256Util.hash(password));
        return getUserById(userMapper.findByUsername(username).getId());
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        UserBo user = userMapper.findByUsername(username);
        if (user == null || !Sha256Util.hash(password).equals(user.getPasswordHash())) {
            throw new ApiException(401, "用户名或密码错误");
        }
        if (user.getStatus() == null || user.getStatus() != 1) {
            throw new ApiException(403, "账号已被禁用");
        }
        // sa-token 登录：token 默认同时写入 Cookie（供 Web 前端）并可通过返回值交给脚本/客户端
        StpUtil.login(user.getId());
        userMapper.updateLastLogin(user.getId());

        Map<String, Object> result = new HashMap<>();
        result.put("tokenName", StpUtil.getTokenName());
        result.put("tokenValue", StpUtil.getTokenValue());
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("uploadBalance", user.getUploadBalance());
        return result;
    }

    @Override
    public UserBo getUserById(Long userId) {
        UserBo user = userMapper.findById(userId);
        if (user == null) {
            throw new ApiException(404, "用户不存在");
        }
        return maskPassword(user);
    }

    @Override
    public void deductUploadBalance(Long userId, int count) {
        int updated = userMapper.deductUploadBalance(userId, count);
        if (updated == 0) {
            throw new ApiException(403, "上传次数余额不足，请联系管理员充值");
        }
    }

    @Override
    public void grantUploadBalance(Long adminId, Long userId, int amount) {
        requireAdmin(adminId);
        if (amount == 0) {
            throw new ApiException(400, "调整额度不能为 0");
        }
        userMapper.grantUploadBalance(userId, amount);
    }

    @Override
    public List<UserBo> listAllUsers(Long adminId) {
        requireAdmin(adminId);
        return userMapper.findAll().stream().map(this::maskPassword).toList();
    }

    private void requireAdmin(Long userId) {
        UserBo user = userMapper.findById(userId);
        if (user == null || !"admin".equals(user.getRole())) {
            throw new ApiException(403, "需要管理员权限");
        }
    }

    @Override
    public UserBo maskPassword(UserBo user) {
        user.setPasswordHash(null);
        return user;
    }

}
