package com.stretto.demo.auth.dtos;

public record NewAccountRequest (String name,
                                 String username,
                                 String password,
                                 String email){
}