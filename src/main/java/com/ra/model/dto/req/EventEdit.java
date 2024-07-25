package com.ra.model.dto.req;

import com.ra.Validator.CouponExist;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EventEdit {
    private Long id;
    @NotBlank(message = "Discount required")
    private String discount;
    @NotNull(message = "End date required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @NotNull(message = "Start date required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Date must be in future")
    private LocalDate startDate;
    @NotBlank(message = "Title required")
    private String title;
}
