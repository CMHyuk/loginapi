package com.example.memberapi.web;

import com.example.memberapi.web.argumentresolver.LoginMemberArgumentResolver;
import com.example.memberapi.web.interceptor.LoginCheckInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class Config implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(1)
                .addPathPatterns("/member/update/{id}", "/member/delete/{id}",
                        "/post/write", "/post/edit/{postId}", "/post/delete/{postId}",
                        "/post/{id}/comment", "post/{postId}/editComment/{commentId}",
                        "post/{postId}/deleteComment/{commentId}");
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }
}
