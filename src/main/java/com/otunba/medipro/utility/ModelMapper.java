package com.otunba.medipro.utility;

public interface ModelMapper<A, B> {
    A mapTo(B b);
    B mapFrom(A a);
}
