package com.raf.rafvodic.services;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.raf.rafvodic.entities.User;
import com.raf.rafvodic.repositories.user.UserRepository;
import org.apache.commons.codec.digest.DigestUtils;

import javax.inject.Inject;
import java.util.Date;
import java.util.List;

public class UserService {

    @Inject
    UserRepository userRepository;

    public String login(String email, String password)
    {
        String hashedPassword = DigestUtils.sha256Hex(password);

        User user = this.userRepository.findUserByEmail(email);
        if (user == null || !user.getHashedPassword().equals(hashedPassword)) {
            System.out.println("User not found or password is incorrect");
            return null;
        }

        Date issuedAt = new Date();
        Date expiresAt = new Date(issuedAt.getTime() + 24*60*60*1000); // One day

        Algorithm algorithm = Algorithm.HMAC256("secret");

        // JWT-om mozete bezbedono poslati informacije na FE
        // Tako sto sve sto zelite da posaljete zapakujete u claims mapu
        return JWT.create()
                .withIssuedAt(issuedAt)
                .withExpiresAt(expiresAt)
                .withSubject(email)
                .withClaim("role", user.getUserType().toString())
                .withClaim("userId", user.getId())
                .sign(algorithm);
    }

    public boolean isAuthorized(String token){
        Algorithm algorithm = Algorithm.HMAC256("secret");
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT jwt = verifier.verify(token);

        String email = jwt.getSubject();
//        jwt.getClaim("role").asString();

        User user = this.userRepository.findUserByEmail(email);

        if (user == null || jwt.getClaim("role").asString().equals("EDITOR")){
            return false;
        }
//        if (user == null){
//            return false;
//        }


        return true;
    }


    public User findUserById(int id) {
        return userRepository.findUserById(id);
    }
    public List<User> getAllUsers(int page, int limit) {
        int offset = (page - 1) * limit;
        return userRepository.getAllUsers(offset, limit);
    }
    public User addUser(User user) {
        return userRepository.addUser(user);
    }

    // update
    public User updateUser(User user) {
        return userRepository.updateUser(user);
    }
}
