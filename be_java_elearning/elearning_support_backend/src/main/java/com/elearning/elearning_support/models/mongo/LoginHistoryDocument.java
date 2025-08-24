package com.elearning.elearning_support.models.mongo;

import java.util.Date;
import javax.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.elearning.elearning_support.entities.auth.AuthInfo;
import com.elearning.elearning_support.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "login_history")
public class LoginHistoryDocument {

    @Id
    private String id;

    private Long userId;

    private String ipAddress;

    private Date loggedInAt;

    public LoginHistoryDocument(AuthInfo authInfo) {
        this.setUserId(authInfo.getUserId());
        this.setIpAddress(authInfo.getIpAddress());
        this.setLoggedInAt(DateUtils.asDate(authInfo.getLastLoginAt()));
    }
}
