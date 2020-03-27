package com.ilirus.oauth.entities;

import com.ilirus.oauth.enums.Status;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Dto<T> {
    private Integer code;
    private String message;
    private T data;

    @NotNull
    @Contract("_ -> new")
    public static<T> Dto<T> of(@NotNull Status status) {
        return new Dto<>(status.getCode(), status.getMessage(), null);
    }

    @NotNull
    @Contract("_, _ -> new")
    public static<T> Dto<T> ofData(@NotNull Status status, T data) {
        return new Dto<>(status.getCode(), status.getMessage(), data);
    }
}
