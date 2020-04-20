package cool.xiaokang.annotation;

import java.lang.annotation.*;

/**
 * 字段列族和值
 *
 * @author xiaokang
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface PerColumn {
    //列族名
    public String name() default "";

    //列限定符（具体的列名）
    public String qualifier() default "";
}
