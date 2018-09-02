package me.ggulmool.todo.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    private LocalDateTime createDate;

    @LastModifiedDate
    @Column(name = "last_modified_date", updatable = true)
    private LocalDateTime lastModifiedDate;

    public BaseEntity() {
    }

    @JsonProperty("created_date")
    public String getFormattedCreateDate() {
        return getFormattedDate(createDate, "yyyy-MM-dd HH:mm:ss");
    }

    @JsonProperty("last_modified_date")
    public String getFormattedLastModifiedDate() {
        return getFormattedDate(lastModifiedDate, "yyyy-MM-dd HH:mm:ss");
    }

    private String getFormattedDate(LocalDateTime dateTime, String format) {
        if (dateTime == null) {
            return "";
        }
        return dateTime.format(DateTimeFormatter.ofPattern(format));
    }
}