package com.example.handlenull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class HandleNullTest {


    @ParameterizedTest
    @NullSource
    void member_name_is_null(String name) {
        assertThatThrownBy(() -> new Member(name))
                .isInstanceOf(AssertionError.class);
    }

    @Test
    void get_name_optional() {
        Member member = new Member("name");

        Optional<String> value = member.getName();

        assertThat(value.orElseThrow(RuntimeException::new))
                .isEqualTo("name");
    }
}