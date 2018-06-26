package com.wesd.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * 注意这里是没有 id 的，需要你自己生成
 *
 * @Id
 * @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select SEQ_ID.nextval from dual")
 * protected Long id;
 * <p>
 * Created by ThomasYu on 2018/1/3
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class SequenceReadOnlyModel implements Serializable {
    @Column(nullable = false)
    protected Date created;
}
