package cc.bexerlmao.xcto.user.mapper;

import cc.bexerlmao.xcto.user.pojo.UserBo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<UserBo> {

    UserBo findByUsername(@Param("username") String username);

    UserBo findById(@Param("id") Long id);

    void insertUser(@Param("username") String username, @Param("passwordHash") String passwordHash);

    void updateLastLogin(@Param("id") Long id);

    /**
     * 扣减上传余额并累加已用次数；余额不足时影响行数为 0。
     */
    int deductUploadBalance(@Param("id") Long id, @Param("count") int count);

    /**
     * 管理员调整上传余额（amount 可为负）。
     */
    int grantUploadBalance(@Param("id") Long id, @Param("amount") int amount);

    List<UserBo> findAll();

}
