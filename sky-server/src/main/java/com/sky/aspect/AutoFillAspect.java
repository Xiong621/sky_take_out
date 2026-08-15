package com.sky.aspect;

import com.sky.annotation.Autofill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    //1 定义切入点    2通知,什么通知
    //1
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.Autofill)")
    public void autoFillAspectCut() {}

    //2前置通知
    //根据切入点规则来通知
    //直接在里面完善填充
    @Before("autoFillAspectCut()")
    public  void autoFill(JoinPoint joinPoint) {
        log.info("开始进行公共字段自动填充处理...");

        //获得当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();//获得方法签名对象
        Autofill autofill = signature.getMethod().getAnnotation(Autofill.class);//获得方法上的注解对象
        OperationType value = autofill.value();//获得数据库操作类型

        //获得到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();//获得方法所有参数  不是所有方法的参数
        if (args == null || args.length == 0){
            return;
        }
        Object entity = args[0];//使用object,因为不止有员工，还有菜等等。所以使用全对象
        //准备赋值的数据
        LocalDateTime time = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为应对的属性赋值,
        //思考；怎么去赋值    通过反射

        if(value == OperationType.INSERT){
            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                //通过反射为对象属性赋值
                setCreateUser.invoke(entity, currentId);
                setUpdateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, time);
                setCreateTime.invoke(entity, time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }else if (value == OperationType.UPDATE){
            try {
                Method setUpdateTime=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser=entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                setUpdateUser.invoke(entity, currentId);
                setUpdateTime.invoke(entity, time);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }


}
