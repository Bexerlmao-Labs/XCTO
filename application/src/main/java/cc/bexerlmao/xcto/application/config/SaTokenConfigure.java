package cc.bexerlmao.xcto.application.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token 路由级鉴权配置。
 *
 * 需要登录的接口：
 * - 题库查看：/question/getAll
 * - 题目上传/更新：/question/save、/question/saveBatch、/question/saveBatchNew、/question/update
 * - 用户中心：/user/**、/auth/logout
 *
 * 保持公开的接口：
 * - /auth/login、/auth/register（登录注册）
 * - /question/{classId}、/question/check/**（答题页面）
 * - /chaoxingClass/list（课程列表）
 *
 * 脚本侧：登录后将 tokenValue 通过 satoken 请求头携带即可通过校验。
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns(
                        "/question/getAll",
                        "/question/save",
                        "/question/saveBatch",
                        "/question/saveBatchNew",
                        "/question/update",
                        "/user/**",
                        "/auth/logout"
                )
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/register"
                );
    }

}
