package cc.bexerlmao.xcto.user.controller;

import cc.bexerlmao.xcto.user.pojo.LoginRequest;
import cc.bexerlmao.xcto.user.pojo.RegisterRequest;
import cc.bexerlmao.xcto.user.pojo.UserBo;
import cc.bexerlmao.xcto.user.service.UserService;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /** 注册（公开） */
    @PostMapping("/auth/register")
    public UserBo register(@RequestBody RegisterRequest request) {
        return userService.register(request.getUsername(), request.getPassword());
    }

    /** 登录（公开），返回 tokenName/tokenValue，脚本侧保存 tokenValue 后通过 satoken 请求头携带 */
    @PostMapping("/auth/login")
    public Map<String, Object> login(@RequestBody LoginRequest request) {
        return userService.login(request.getUsername(), request.getPassword());
    }

    /** 登出 */
    @PostMapping("/auth/logout")
    public String logout() {
        StpUtil.logout();
        return "success";
    }

    /** 当前登录用户信息 */
    @GetMapping("/user/info")
    public UserBo info() {
        return userService.getUserById(StpUtil.getLoginIdAsLong());
    }

    /** 管理员：用户列表 */
    @GetMapping("/user/list")
    public List<UserBo> listUsers() {
        return userService.listAllUsers(StpUtil.getLoginIdAsLong());
    }

    /** 管理员：调整用户上传余额（amount 可为负） */
    @PutMapping("/user/{userId}/balance")
    public String grantBalance(@PathVariable Long userId, @RequestBody Map<String, Integer> body) {
        Integer amount = body.get("amount");
        if (amount == null) {
            throw new IllegalArgumentException("缺少 amount 字段");
        }
        userService.grantUploadBalance(StpUtil.getLoginIdAsLong(), userId, amount);
        return "success";
    }

}
