package com.stretto.demo.auth.dtos;

public record NewAccountRequest (String username,
                                 String password,
                                 String email){
}