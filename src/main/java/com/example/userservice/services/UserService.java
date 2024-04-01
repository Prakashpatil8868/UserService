package com.example.userservice.services;

import com.example.userservice.Models.Token;
import com.example.userservice.Models.User;
import com.example.userservice.exceptions.InvalidPasswordException;
import com.example.userservice.exceptions.TokenInvalidOrExpiredException;
import com.example.userservice.exceptions.UserAlreadyExistsException;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.repositories.TokenRepository;
import com.example.userservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class UserService {

   private UserRepository userRepository;
   private BCryptPasswordEncoder bCryptPasswordEncoder;
private TokenRepository tokenRepository;
@Autowired
   public UserService(UserRepository userRepository,TokenRepository tokenRepository, BCryptPasswordEncoder bCryptPasswordEncoder){
      this.userRepository=userRepository;
      this.bCryptPasswordEncoder=bCryptPasswordEncoder;
      this.tokenRepository=tokenRepository;
   }
   public User signup(String name, String email, String password) throws UserAlreadyExistsException {
      Optional<User> userOptional=userRepository.findByEmail(email);

      if (userOptional.isPresent()){
         throw new UserAlreadyExistsException("Already exist user");
      }

      User user=new User();
      user.setName(name);
      user.setEmail(email);
      user.setHashPassword(bCryptPasswordEncoder.encode(password));
    User savedUser=userRepository.save(user);
    return savedUser;
   }

   public Token login(String email, String password) throws UserNotFoundException, InvalidPasswordException {
   Optional<User> userOptional=userRepository.findByEmail(email);
   if(userOptional.isEmpty()){
      throw new UserNotFoundException("User with this "+email+" not exist");
   }

   User savedUser=userOptional.get();
if(! bCryptPasswordEncoder.matches(password,savedUser.getHashPassword())){
   throw new InvalidPasswordException("Password is incorrect");
}

Token token=new Token();
token.setUser(savedUser);

      Calendar calendar = Calendar.getInstance();
      calendar.add(Calendar.DATE,30);
      Date dataPlus30Days =calendar.getTime();
      token.setExpiryAt(dataPlus30Days);


      String tokenValue= RandomStringUtils.randomAlphanumeric(120);
      token.setValue(tokenValue);


      Token savedToken=tokenRepository.save(token);
      return savedToken;
}
public Token logout(String tokenValue) throws TokenInvalidOrExpiredException {
  Optional<Token> tokenOptional=tokenRepository.findTokenByValueAndExpiryAtGreaterThanAndDeleted(tokenValue,new Date(),false);
  if(tokenOptional.isEmpty()){
      throw new TokenInvalidOrExpiredException("Token not exist or already expired");
  }
  Token Token=tokenOptional.get();
  Token.setDeleted(true);
  Token savedToken=tokenRepository.save(Token);
  return savedToken;
}

    public Token validateToken(String tokenValue) {
Optional<Token>tokenOptional=tokenRepository.findTokenByValueAndExpiryAtGreaterThanAndDeleted(tokenValue,new Date(),false);
if (tokenOptional.isEmpty()){
    return null;
}
return tokenOptional.get();
    }
}
