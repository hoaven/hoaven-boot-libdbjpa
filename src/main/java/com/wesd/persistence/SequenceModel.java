package com.wesd.persistence;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Date;

/**
 * Created by ThomasYu on 2018/1/3
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class SequenceModel extends SequenceReadOnlyModel {
    @Column(nullable = false)
    protected Date updated;

    @Column(nullable = true)
    protected Date deleted;

    protected Integer version;
}
