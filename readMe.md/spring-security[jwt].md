# spring-security[jwt]
程式參考:
1. [Spring Boot & Android Studio教學-作者hank910416 ](https://ithelp.ithome.com.tw/articles/10329996)
2. [把玩 Spring Security-作者Ching Yi, Chan](https://qrtt1.medium.com/hands-on-spring-security-1-77228057a8b9)
該專案結構依功能拆分為以下3類:
**1. SecurityConfig**
**2. ApplicationConfig**
**3. JwtAuthenticationFilter**

---

# 1. SecurityConfig
規範請求路徑是否需要驗證

```java
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthFilter;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //禁止CSRF（跨站請求偽造）保護。
        http.csrf(AbstractHttpConfigurer::disable)
                //對所有訪問HTTP端點的HttpServletRequest進行限制
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                "/error/**",
                                "/api/auth/**"
                        )
                        //指定上述匹配規則中的路徑，允許所有用戶訪問，即不需要進行身份驗證。
                        .permitAll()
                        //其他尚未匹配到的路徑都需要身份驗證
                        .anyRequest().authenticated()
                )
                //注意未加入該行，請求不進行驗證，進而導致權限拒絕。
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

```

<div STYLE="page-break-after: always;"></div>

# 2. ApplicationConfig
取得驗證資料，本例以email取得角色權限。
```java
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig  {

    private final AppUserRepository appUserRepository;


    @Bean
    public UserDetailsService userDetailsService(){
        return username -> appUserRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```

<div STYLE="page-break-after: always;"></div>

# 3. JwtAuthenticationFilter

當一個請求進入時，實際驗證流程。
```java
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    final String BEARER = "Bearer ";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if(authHeader == null || !authHeader.startsWith(BEARER)){
            filterChain.doFilter(request, response);
            return;
        }
        jwt = authHeader.substring(BEARER.length());
        userEmail = jwtService.extractUsername(jwt);
        if(userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if(jwtService.isTokenValid(jwt, userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        }
    }
}
```
