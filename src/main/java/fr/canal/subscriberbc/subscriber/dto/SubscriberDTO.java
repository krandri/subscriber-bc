package fr.canal.subscriberbc.subscriber.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriberDTO {

    private Integer subscriberId;

    @NotEmpty(message = "first name is required.")
    private String fname;

    @NotEmpty(message = "last name is required.")
    private String lname;

    @Email(message = "mail is required and in a good format")
    private String mail;

    @Pattern(regexp = "0[1-9]([\\s]?\\d{2}){4}", message = "phone is required.")
    private String phone;

    private Boolean isActiv;
}
