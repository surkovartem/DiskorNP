package ru.rzd.discor.diskorReportConstReportBack.models.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String sub;
    private Boolean emailVerified;
    @JsonProperty("email_verified")
    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
    @JsonProperty("email_verified")
    public Boolean getEmailVerified() {
        return this.emailVerified;
    }
    private String org;
    private String userName;
    @JsonProperty("user_name")
    public void setUserName(String userName) {
        this.userName = userName;
    }
    @JsonProperty("user_name")
    public String getUserName() {
        return this.userName;
    }
    private Integer dorCode;
    private String fio;
    private Integer userId;
    private String[] authorities;
    private Long exp;
}
