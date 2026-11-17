package cc.bexerlmao.xcto.user.service;

import cc.bexerlmao.xcto.user.pojo.UserBo;

import java.util.List;
import java.util.Map;

public interface UserService {

    /** 注册新用户（密码 SHA-256 存储），返回用户信息（不含密码） */
    UserBo register(String username, String password);

    /** 登录，返回 { tokenName, tokenValue, username, uploadBalance } */
    Map<String, Object> login(String username, String password);

    /** 当前登录用户信息（不含密码） */
    UserBo getUserById(Long userId);

    /** 校验上传余额并扣减（余额不足抛 ApiException 403） */
    void deductUploadBalance(Long userId, int count);

    /** 管理员调整用户上传余额 */
    void grantUploadBalance(Long adminId, Long userId, int amount);

    /** 管理员获取所有用户列表（不含密码） */
    List<UserBo> listAllUsers(Long adminId);

    /** 用户信息脱敏（去掉密码字段） */
    UserBo maskPassword(UserBo user);

}
