package com.wesd.persistence;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseReadOnlyModel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, updatable = false)
    protected Date created;

    @PrePersist
    protected void onCreate() {

        if (created == null) {
            created = new Date();
        }
    }
}

/**
 @MappedSuperclass注解
    可以将该实体类当成基类实体，它不会隐射到数据库表，
    但继承它的子类实体在隐射时会自动扫描该基类实体的隐射属性，添加到子类实体的对应数据库表中。

 @Column(
     name = 可选，列名（默认值为属性名）。
     unique = 可选，是否在该列上设置唯一约束（默认false）。
     nullable = 可选，是否设置该列的值可以为空（默认true）。
     insertable = 可选，该列是否作为生成的insert语句中的一列（默认true）。
     updateable = 可选，该列是否作为生成的update语句中的一列（默认true）。
     length  = 可选，列长度（默认255）。
     precision = 可选，列十进制精度（默认0）。
     scale = 可选，如果列十进制数值范围可用，在此设置（默认0）。
 )

 @PrePersist save到datastore之前被调用
 @PostPersist save到datastore之后被调用

 @PostLoad     在Entity被映射之后被调用
 @EntityListeners 指定外部生命周期事件实现类
 */
