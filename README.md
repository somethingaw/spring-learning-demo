# spring-learning-demo
Some demos in the process of learning spring！

## spring-security-knife

利用knife 搭建api接口页面，利用spring security进行接口授权认证。

| 框架名称               | 版本号    |
|--------------------|--------|
| spring boot 版本     | 2.7.11 |
| spring security 版本 | 5.7.8  |
| knife4j            | 4.1.0  |

## 包含内容

1. knife4j 结合 spring security 进行oauth2 的授权校验
2. 接口的 permission 校验
3. 接口的 hasRole 校验
4. 模拟简单自定义登录页面
5. 登录成功操作 实现 AuthenticationSuccessHandler

## TODO

1. 改造 token，使用对象
2. 改造 appcode 和 appSecret，使其与用户体系关联上