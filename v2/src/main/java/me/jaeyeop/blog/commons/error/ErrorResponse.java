package me.jaeyeop.blog.commons.error;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "예외 응답", name = "Error Response")
public record ErrorResponse(@Schema(description = "예외 메시지") String message) {

}
