package cc.bexerlmao.xcto.user.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
public class UserBo {

    @TableId
    Long id;
    String username;
    String passwordHash;
    String role;
    String courses;
    String uploadKey;
    Integer uploadBalance;
    Long uploadUsed;
    Integer status;
    LocalDateTime createTime;
    LocalDateTime updateTime;
    LocalDateTime lastLoginTime;

}
