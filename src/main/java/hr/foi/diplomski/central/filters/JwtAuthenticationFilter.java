package hr.foi.diplomski.central.filters;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hr.foi.diplomski.central.constants.SecurityConstants;
import hr.foi.diplomski.central.model.Rola;
import hr.foi.diplomski.central.model.User;
import hr.foi.diplomski.central.model.dto.UserDto;
import hr.foi.diplomski.central.utils.GsonLocalDateAdapter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.stream.Collectors;

import static hr.foi.diplomski.central.constants.SecurityConstants.REQUEST_PASSWORD;
import static hr.foi.diplomski.central.constants.SecurityConstants.REQUEST_USERNAME;
import static hr.foi.diplomski.central.utils.CommonUtils.convertToLocalDateTime;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final Long tokenDuration;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, Long tokenDuration) {
        this.authenticationManager = authenticationManager;
        this.tokenDuration = tokenDuration;

        setFilterProcessesUrl(SecurityConstants.AUTH_LOGIN_URL);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        var username = request.getParameter(REQUEST_USERNAME);
        var password = request.getParameter(REQUEST_PASSWORD);
        var authenticationToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain filterChain, Authentication authentication) throws IOException {
        var user = ((User) authentication.getPrincipal());

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var signingKey = SecurityConstants.JWT_SECRET.getBytes();
        Date expirationDate = new Date(System.currentTimeMillis() + tokenDuration);

        var token = Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(signingKey), SignatureAlgorithm.HS512)
                .setHeaderParam("typ", SecurityConstants.TOKEN_TYPE)
                .setIssuer(SecurityConstants.TOKEN_ISSUER)
                .setAudience(SecurityConstants.TOKEN_AUDIENCE)
                .setSubject(user.getUsername())
                .setExpiration(expirationDate)
                .claim("rol", roles)
                .compact();

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setExpiration(convertToLocalDateTime(expirationDate));
        userDto.setRoles(user.getRoles().stream().map(Rola::getNaziv).collect(Collectors.toList()));
        userDto.setToken(token);

        Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new GsonLocalDateAdapter()).create();

        response.getWriter().write(gson.toJson(userDto));
        response.addHeader(SecurityConstants.TOKEN_HEADER, SecurityConstants.TOKEN_PREFIX + token);
    }



}
