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

// import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class KaryawanController {

    @Autowired
    private JwtUtil jwtUtil;

    // Endpoint untuk mendapatkan token
    @GetMapping("/get-token")
    public TokenResponse getToken(@RequestParam String username) {
        if ("admin".equals(username)) {
            try {
                // Generate token dengan menambahkan informasi pengguna yang terautentikasi
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
            // Missing or invalid authorization header
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorization.substring(7); // Extract token after "Bearer "

        if (jwtUtil.validateToken(token) != null) {
            // Token is valid, return dummy data for now (replace with actual data retrieval
            // logic)
            List<Karyawan> dummyKaryawanList = createDummyKaryawanData();
            return ResponseEntity.ok(dummyKaryawanList);
        } else {
            // Invalid token
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

// // Endpoint untuk mendapatkan daftar karyawan
// @GetMapping("/karyawan")
// public List<Karyawan> getKaryawan(@RequestHeader("Authorization") String
// token) {
// System.out.println("Menerima token: " + token); // Tambahkan titik log
// if (token != null && !token.isEmpty()) {
// // Memvalidasi keaslian token yang diterima
// String username = jwtUtil.validateToken(token);
// System.out.println("Username dari token: " + username); // Tambahkan titik
// log
// if (username != null) {
// // Jika token valid, kembalikan daftar karyawan
// System.out.println("Token valid. Mengembalikan daftar karyawan."); //
// Tambahkan titik log
// return karyawanList;
// } else {
// throw new RuntimeException("Token tidak valid");
// }
// } else {
// throw new RuntimeException("Token tidak valid");
// }
// }
