package com.ordo.oauth.domain.response;

import com.ordo.oauth.domain.AlarmType;
import com.ordo.oauth.domain.entity.AlarmEntity;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AlarmResponse {
    private Integer id;
    private AlarmType alarmType; // NEW_COMMENT_ON_POST, NEW_LIKE_ON_POST
    private Integer fromUserId;
    private Integer targetId;
    private String text;
    private LocalDateTime createdAt;

    public static AlarmResponse fromAlarm(AlarmEntity alarm) {
        return new AlarmResponse(
            alarm.getId(),
            alarm.getAlarmType(),
            alarm.getFromUserId(),
            alarm.getTargetId(),
            alarm.getText(),
            alarm.getCreatedAt()
        );
    }
}
