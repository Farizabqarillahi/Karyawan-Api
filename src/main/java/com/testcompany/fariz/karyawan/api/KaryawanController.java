package com.testcompany.fariz.karyawan.api;

import com.nimbusds.jose.JOSEException;
import com.testcompany.fariz.karyawan.model.*;
import com.testcompany.fariz.karyawan.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class KaryawanController {

    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/get-token")
    public TokenResponse getToken(@RequestParam String username) {
        if ("admin".equals(username)) {
            try {
                String token = jwtUtil.generateToken(username);
                return new TokenResponse(token);
            } catch (JOSEException e) {
                throw new RuntimeException("Gagal membuat token");
            }
        } else {
            throw new RuntimeException("Username tidak valid");
        }
    }

    @GetMapping("/")
    public String test() {
        return "Endpoints:\n- GET /get-token?username=admin\n- GET /karyawan\n\nEnjoy!";
    }

    @GetMapping("/karyawan")
    public ResponseEntity<Object> getKaryawan(@RequestHeader("Authorization") String authorization) {

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorization.substring(7);

        if (jwtUtil.validateToken(token) != null) {
            List<Karyawan> dummyKaryawanList = createDummyKaryawanData();
            return ResponseEntity.ok(dummyKaryawanList);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token tidak valid");
        }
    }

    private List<Karyawan> createDummyKaryawanData() {
        List<Karyawan> karyawanList = new ArrayList<>(Arrays.asList(
                new Karyawan("Fariz", "Backend Developer"),
                new Karyawan("Aisha", "Frontend Developer"),
                new Karyawan("Rizky", "DevOps Engineer")));
        return karyawanList;
    }
}
