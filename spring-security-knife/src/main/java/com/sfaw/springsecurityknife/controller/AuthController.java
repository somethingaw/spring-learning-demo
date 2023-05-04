package com.sfaw.springsecurityknife.controller;

import com.sfaw.springsecurityknife.dto.AuthRequestDTO;
import com.sfaw.springsecurityknife.dto.AuthResponseDTO;
import com.sfaw.springsecurityknife.dto.RegisterRequestDTO;
import com.sfaw.springsecurityknife.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * LoginController
 * 认证接口
 * @author ArthurW
 * @version 1.0
 * @date 2023/4/18 15:57
 **/
@Controller
@RequestMapping("/auth")
@Tag(name = "登录信息")
public class AuthController {
    @Autowired
    private AuthService authService;

    // 备注 ，认证接口必须是个get请求
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponseDTO> authenticate(@RequestBody AuthRequestDTO dto) {
        return ResponseEntity.ok(authService.authenticate(dto));
    }

    /**
     * 简单模式授权接口， 必须是个get接口，使用client_id 获取token
     *
     * @param clientId
     * @param redirectUri
     * @param type
     * @param state
     * @param response
     * @return
     * @throws IOException
     */
    @Operation(summary = "implicit")
    @GetMapping(value = "/oauth/implicit/authorize")
    public ResponseEntity<Object> authorize(@RequestParam("client_id") String clientId,
                                            @RequestParam("redirect_uri") String redirectUri,
                                            @RequestParam("response_type") String type,
                                            @RequestParam("state") String state,
                                            HttpServletResponse response) throws IOException {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName(clientId);
        dto.setPassword("123");
        AuthResponseDTO dto1 = authService.register(dto);
        // 注意回传 access_token
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUri + "?code=" + dto1.getToken() + "&state=" + state + "&response_type=" + type + "&access_token=" + dto1.getToken())
                .header("Content-Type", "application/json;charset=UTF-8")
                .build();

    }

    /**
     * 密码模式, 使用用户名。密码获取token
     *
     * @param username
     * @param password
     * @param type
     * @param scope
     * @param response
     * @return
     */
    @Operation(summary = "password")
    @PostMapping(value = "/oauth/password/token")
    @ResponseBody
    public ResponseEntity<Object> token(@RequestParam("username") String username,
                                        @RequestParam("password") String password,
                                        @RequestParam("grant_type") String type,
                                        @RequestParam("scope") String scope,
                                        HttpServletResponse response) {
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName(username);
        dto.setPassword(password);
        Set<String> scopes = new HashSet<>(List.of(scope.split(" ")));
        AuthResponseDTO dto1 = authService.register(dto);
        dto1.setScopes(scopes);
        dto1.setAccess_token(dto1.getToken());

        return ResponseEntity.ok(dto1);
    }

    /**
     * 客户端模式，填写 client_id 和 client_secret会放到header ，http_basic 中
     *
     * @param response
     * @return
     */
    @Operation(summary = "client")
    @PostMapping(value = "/oauth/client/token")
    @ResponseBody
    public ResponseEntity<Object> clientToken(@RequestParam("grant_type") String grantType,
                                              HttpServletResponse response,
                                              HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        String basic = new String(Base64Coder.decode(auth.replace("Basic ", "")));
        String[] basicAuth = basic.split(":");
        String clientId = basicAuth[0];
        String clientSecret = basicAuth[1];
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName(clientId);
        dto.setPassword(clientSecret);
        AuthResponseDTO dto1 = authService.register(dto);
        dto1.setAccess_token(dto1.getToken());

        return ResponseEntity.ok(dto1);
    }

    /**
     * 授权码模式，理论上 使用 用户名密码登录，然后重定向，再后端使用 client_id 和 client_secret 进行token获取
     * swagger 是传入 client_id 和 client_secret， 然后应该跳转到 授权页面，授权后，调用token 接口，拿到token
     * 这里简化了，认证接口
     *
     * @param clientId
     * @param redirectUri
     * @param type
     * @param state
     * @param response
     * @return
     * @throws IOException
     */
    @GetMapping(value = "/oauth/authcode/authorize")
    @Operation(summary = "auth code")
    public ResponseEntity<Object> authcodeAuthorize(@RequestParam("client_id") String clientId,
                                                    @RequestParam("redirect_uri") String redirectUri,
                                                    @RequestParam("response_type") String type,
                                                    @RequestParam("state") String state,
                                                    HttpServletResponse response) throws IOException {

        // 这里需要自己做认证 ，跳转到自己的授权页面，
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUri + "?code=test" + "&state=" + state + "&response_type=" + type)
                .header("Content-Type", "application/json;charset=UTF-8")
                .build();

    }

    @Operation(summary = "client")
    @PostMapping(value = "/oauth/authcode/token")
    @ResponseBody
    public ResponseEntity<Object> authcodeToken(@RequestParam("client_id") String clientId,
                                                @RequestParam("client_secret") String clientSecret,
                                                @RequestParam("grant_type") String grantType,
                                                @RequestParam("code") String code,
                                                HttpServletRequest request,
                                                HttpServletResponse response) {
        // todo 这里code type 校验都没做
        RegisterRequestDTO dto = new RegisterRequestDTO();
        dto.setName(clientId);
        dto.setPassword(clientSecret);
        AuthResponseDTO dto1 = authService.register(dto);
        dto1.setAccess_token(dto1.getToken());
        return ResponseEntity.ok(dto1);
    }

    /**
     * @param response
     * @return
     */
    @Operation(summary = "refresh token")
    @PostMapping(value = "/oauth/refresh/token")
    @ResponseBody
    public ResponseEntity<Object> refreshToken(HttpServletRequest request,
                                               HttpServletResponse response) {
        AuthResponseDTO authResponseDTO = authService.refreshToken(request);
        return ResponseEntity.ok(authResponseDTO);
    }

}

