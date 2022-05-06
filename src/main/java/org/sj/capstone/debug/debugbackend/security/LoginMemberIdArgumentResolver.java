package org.sj.capstone.debug.debugbackend.security;

import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import static java.util.Objects.nonNull;

public class LoginMemberIdArgumentResolver  implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter)  {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(LoginMemberId.class);
        boolean hasMemberType = Long.class.isAssignableFrom(parameter.getParameterType());

        return hasLoginAnnotation && hasMemberType;
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal instanceof MemberContext ? ((MemberContext) principal).getMemberId() : null;
    }
}
