package com.wesd.persistence;

import lombok.*;

import javax.persistence.*;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseModel extends BaseReadOnlyModel {
    @Column(nullable = false)
    protected Date updated;

    @Column(nullable = true)
    protected Date deleted;

    @Version
    @Column(nullable = false)
    protected Integer version = 0;

    @Override
    @PrePersist
    protected void onCreate() {
        super.onCreate();
        updated = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updated = new Date();
    }
}

