package ma.yc.Citronix.farm.application.dto.request.update;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record FarmUpdateDto(

        @Size(min = 2, message = "must be at least 2 characters long")
        @Pattern(regexp = "^(?!\\s*$).+", message = "cannot be blank or contain only whitespace")
        String name,

        @Size(min = 2, message = "must be at least 2 characters long")
        @Pattern(regexp = "^(?!\\s*$).+", message = "cannot be blank or contain only whitespace")
        String localization,

        @Positive(message = "Surface must be a positive number")
        Double surface,
        LocalDate creationDate
) {
    public void validate () {
        if (name != null && name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty or contain only whitespace");
        }
        if (localization != null && localization.trim().isEmpty()) {
            throw new IllegalArgumentException("Localization cannot be empty or contain only whitespace");
        }
    }
}