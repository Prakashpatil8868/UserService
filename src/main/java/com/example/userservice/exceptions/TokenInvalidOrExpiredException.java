package com.example.userservice.exceptions;

public class TokenInvalidOrExpiredException extends Exception{
public TokenInvalidOrExpiredException(String message){
    super(message);
}
}
