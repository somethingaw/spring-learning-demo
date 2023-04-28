package com.sfaw.springsecurityknife.dto;

import java.io.Serializable;
import java.util.Set;

/**
 * AuthResponseDTO
 * 授权返回对象，
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/21 9:54
 **/
public class AuthResponseDTO implements Serializable {

    /**
     * 注意这里 swagger 接收参数必须是 下划线格式
     */
    private String access_token;
    private String expires_in;
    /**
     * 注意这里 token_type 必须需要，swagger 接收需要
     */
    private String token_type = "Bearer";
    private String refresh_token;
    private Set<String> scopes;

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public Set<String> getScopes() {
        return scopes;
    }

    public void setScopes(Set<String> scopes) {
        this.scopes = scopes;
    }

    public AuthResponseDTO(String token) {
        super();
        this.access_token = token;
    }

    public String getToken() {
        return access_token;
    }
}
