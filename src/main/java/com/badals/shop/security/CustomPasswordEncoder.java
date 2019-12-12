package com.badals.shop.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;


public class CustomPasswordEncoder implements PasswordEncoder {

    private static final Logger logger = LoggerFactory.getLogger(CustomPasswordEncoder.class);
    public static final String SALT_SEP = "SSSAAALLLTTT";

    @Override
    public String encode(CharSequence rawPassword) {
        String hashed = BCrypt.hashpw(rawPassword.toString(), BCrypt.gensalt(12));
        return hashed;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {

        logger.info(encodedPassword);

        String[] p = encodedPassword.split(SALT_SEP);
        String hash = p[0];

        String salt = "";
        if (p.length > 1)
            salt = p[1];

        logger.info(hash);
        logger.info(salt);


        if (hash.startsWith("$2")) {
            if (hash.charAt(2) == 'y')
                hash = hash.replace("$2y$", "$2a$");
            return BCrypt.checkpw(rawPassword.toString(), hash);
        }

        return DigestUtils.sha1Hex(salt + DigestUtils.sha1Hex(salt + DigestUtils.sha1Hex(rawPassword.toString()))).equals(hash);

    }
}
