package com.example.userservice.controllers;


import com.example.userservice.dtos.*;
//import com.example.userservice.exceptions.InvalidPasswordException;
//import com.example.userservice.exceptions.TokenNotExistsOrAlreadyExpiredException;
import com.example.userservice.exceptions.InvalidPasswordException;
import com.example.userservice.exceptions.TokenInvalidOrExpiredException;
import com.example.userservice.exceptions.UserAlreadyExistsException;
//import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.Models.Token;
import com.example.userservice.Models.User;
import com.example.userservice.exceptions.UserNotFoundException;
import com.example.userservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) throws UserNotFoundException, InvalidPasswordException {
        LoginResponseDTO responseDTO = new LoginResponseDTO();

        String loginToken = userService.login(
                loginRequestDTO.getEmail(),
                loginRequestDTO.getPassword()
        ).getValue();
        responseDTO.setTokenValue(loginToken);
        responseDTO.setMessage("SUCCESS");

        ResponseEntity<LoginResponseDTO> responseEntity = new ResponseEntity<>(
                responseDTO,
                HttpStatus.OK
        );
        return responseEntity;
    }

    @PostMapping("/register")
    public User signup(@RequestBody SignupRequestDTO signupRequestDTO) throws UserAlreadyExistsException {

        return userService.signup(
                signupRequestDTO.getName(),
                signupRequestDTO.getEmail(),
                signupRequestDTO.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDTO requestDTO) throws TokenInvalidOrExpiredException {
        Token token = userService.logout(requestDTO.getTokenValue());
        ResponseEntity<Void> responseEntity = new ResponseEntity<>(
                token.isDeleted() == true ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR
        );
        return responseEntity;
    }


    @GetMapping("/validate/{token}")
    public UserDTO validateToken(@PathVariable(name = "token") String tokenValue){
        Token token = userService.validateToken(tokenValue);
        UserDTO userDTO = UserDTO.from(token.getUser());
        return userDTO;
    }


}
