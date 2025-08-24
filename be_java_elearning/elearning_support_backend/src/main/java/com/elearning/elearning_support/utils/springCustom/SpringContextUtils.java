package com.elearning.elearning_support.utils.springCustom;

import javax.validation.constraints.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        SpringContextUtils.applicationContext = applicationContext;
    }

    public static Object getBean(String beanName) throws BeansException {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> requiredBeanClass) throws BeansException {
        return applicationContext.getBean(requiredBeanClass);
    }

    public static <T> T getBean(String beanName, Class<T> requiredBeanClass) throws BeansException {
        return applicationContext.getBean(beanName, requiredBeanClass);
    }


}
