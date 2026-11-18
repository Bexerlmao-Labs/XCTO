package cc.bexerlmao.xcto.user.pojo;

import lombok.Data;

@Data
public class RegisterRequest {

    private String username;
    private String password;

    /** 注册邀请码 */
    private String inviteCode;

}
