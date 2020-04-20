package cool.xiaokang.annotation;

import java.lang.annotation.*;

/**
 * 类列族注解
 *
 * @author xiaokang
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface ColumnFamily {
    //列族名称
    public String familyName() default "";
}
