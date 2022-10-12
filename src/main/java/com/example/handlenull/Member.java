package com.example.handlenull;

import java.util.Optional;

public class Member {

    private final String name;

    public Member(String name) {
        validName(name);

        this.name = name;
    }

    private void validName(String name) {
        assert name != null;
    }

    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }
}
