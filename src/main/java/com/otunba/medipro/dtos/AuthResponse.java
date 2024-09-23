package com.otunba.medipro.dtos;

import java.util.List;

public record AuthResponse (
     String authorities,
     String userId
){}
