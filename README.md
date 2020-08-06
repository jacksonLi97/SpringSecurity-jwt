## SpringSecurity整合JWT

### 1.导入依赖

​		导入Spring Security依赖后，springboot项目启动时便自动加载Spring Security，在不做任何设置的情况下，会自动生成密码，打印在console，默认用户名是user。

```xml
<!--security-->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
<!--jwt-->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt</artifactId>
    <version>0.9.1</version>
</dependency>
<dependency>
    <groupId>com.auth0</groupId>
    <artifactId>java-jwt</artifactId>
    <version>3.4.0</version>
</dependency>
```

### 2.编写SysUser实体类继承UserDetails类，编写role类引用GrantedAuthority接口

![image-20200806162229858](C:%5CUsers%5Clizhongjie%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200806162229858.png)

![image-20200806162400109](C:%5CUsers%5Clizhongjie%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200806162400109.png)

### 3. 编写UserService

> 继承UserDetailsService类![image-20200806162559701](C:%5CUsers%5Clizhongjie%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200806162559701.png)

### 4. 编写UserServiceImpl实现类，实现loadUserByUsername方法

![image-20200806162805185](C:%5CUsers%5Clizhongjie%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200806162805185.png)

### 5. 验证用户名密码生成token

1. 过滤器生成token并添加到前端responseHeader

> 认证过滤器：config/JwtAuthenticationFilter.java
>
> 验证用户名密码，生成1个token返回给客户端
>  * 继承了UsernamePasswordAuthenticationFilter类，重新两个方法
>  * attemptAuthentication：接收并解析用户凭证
>  * successfulAuthentication：用户成功登录后，这个方法会被调用，在这个方法里生成token并返回

```java
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;


    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        //改登录url
//        super.setFilterProcessesUrl("/auth/login");
    }
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        
    }
    
}
```

2. api接口生成token返回前端

> controller/AuthController      login()

![image-20200806164029078](C:%5CUsers%5Clizhongjie%5CAppData%5CRoaming%5CTypora%5Ctypora-user-images%5Cimage-20200806164029078.png)

### 6. 验证token信息

> config/JwtVerifyTokenFilter

​		从requestHeader取token信息，用JwtTokenUtil解析token，如果token有效，则将解析得到的用户名与角色封装到UsernamePasswordAuthenticationToken。(如果role参数为null，无法访问Security配置中hasRole的路径)

```java
@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader(HEADER_STRING);
        //验证token格式是否正确
        if (authHeader!=null && authHeader.startsWith(TOKEN_PREFIX)) {
            final String authToken = authHeader.replace(TOKEN_PREFIX, "");
            //从token中获取username和role
            String username = JwtTokenUtil.getUsernameFromToken(authToken);
            String role = JwtTokenUtil.getRoleFromToken(authToken);
            if (username != null) {
                //验证token是否过期
                if (!JwtTokenUtil.isTokenExpired(authToken)) {
                    try {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null,
                                Collections.singleton(new SimpleGrantedAuthority(role)));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }catch (Exception e){
                        e.toString();
                    }
                }else {
                    log.info("token expire");
                }
            }
        }
        chain.doFilter(request, response);
    }
```

### 7.config/WebSecurityConfig配置类

```java
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //添加到IOC容器。密码加密，不使用明文密码
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		  内存中生成一个用户，没整合数据库
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password("{noop}123")
//                .roles("USER");
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf()
                .disable()//关闭csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//关闭session
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS,"/**").permitAll()
                .antMatchers(HttpMethod.POST, "/auth/**").permitAll()
                .antMatchers("/index").permitAll()
                .and()
                //添加过滤器，验证过滤器在鉴权过滤器前
//                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtVerifyTokenFilter(authenticationManager()));

        http
                .headers().cacheControl();
    }
}
```



``` java
SecurityContextHolder.getContext().getAuthentication = null 问题
```

