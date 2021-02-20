package com.hgz.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.Test;

import java.util.Date;

/**
 * @Author: DJA
 * @Date: 2019/11/14
 */
public class JwtTokenTest {

    @Test
    public void jwtTokenCreateTest(){
        JwtBuilder builder = Jwts.builder()
                .setId("666").setSubject("行走在牛A的路上")
                .setIssuedAt(new Date())
                //添加自定义属性
                .claim("role","admin")
                .setExpiration(new Date(new Date().getTime()+600000))
                .signWith(SignatureAlgorithm.HS256,"java1907");

        String jwtToken = builder.compact();
        System.out.println(jwtToken);
    }

    @Test
    public void jwtTokenParseTest(){
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2NjYiLCJzdWIiOiLooYzotbDlnKjniZtB55qE6Lev5LiKIiwiaWF0IjoxNTczNzA2NTM3LCJyb2xlIjoiYWRtaW4iLCJleHAiOjE1NzM3MDcxMzd9.SuY5opRLetRVxm8xcICrIq5o7SB8E_eBWjA-0p3Adrw";

        Claims claims = Jwts.parser().setSigningKey("java1907")
                .parseClaimsJws(token).getBody();

        System.out.println(claims.getId());
        System.out.println(claims.getSubject());
        System.out.println(claims.getIssuedAt());
        System.out.println(claims.getExpiration());
        //获取属性
        System.out.println(claims.get("role"));

    }
}
