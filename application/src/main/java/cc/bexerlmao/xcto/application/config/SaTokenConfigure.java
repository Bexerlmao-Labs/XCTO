package cc.bexerlmao.xcto.application.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * sa-token 路由级鉴权配置。
 *
 * 登录后才能使用的接口：
 * - 题目相关：/question/**（含答题 /question/{classId}、判题 /question/check/**、
 *   题库查看 /question/getAll、上传 /question/save*）
 * - 课程列表：/chaoxingClass/list
 * - 用户中心：/user/**
 * - 登出：/auth/logout
 *
 * 保持公开的接口：/auth/login、/auth/register
 *
 * 脚本侧：登录后将 tokenValue 通过 satoken 请求头携带即可通过校验。
 */
@Configuration
public class SaTokenConfigure implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SaInterceptor(handler -> StpUtil.checkLogin()))
                .addPathPatterns(
                        "/question/**",
                        "/chaoxingClass/list",
                        "/user/**",
                        "/auth/logout"
                )
                .excludePathPatterns(
                        "/auth/login",
                        "/auth/register"
                );
    }

}
