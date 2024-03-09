package fr.canal.subscriberbc.subscriber.controller;

import fr.canal.subscriberbc.subscriber.dto.SubscriberDTO;
import fr.canal.subscriberbc.subscriber.exception.ExistingSubscriberException;
import fr.canal.subscriberbc.subscriber.exception.SubscriberNotFoundException;
import fr.canal.subscriberbc.subscriber.service.SubscriberService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/subscribers")
@Validated
public class SubscriberController {

    private SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping
    public ResponseEntity create(@RequestBody @Valid final SubscriberDTO subscriberDTO) {
        try {
            return ResponseEntity.ok(subscriberService.create(subscriberDTO));
        } catch (ExistingSubscriberException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<SubscriberDTO>> search(@RequestParam(value = "search") final String search) {
        return ResponseEntity.ok(subscriberService.searchSubscribers(search));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<SubscriberDTO> update(@RequestBody final SubscriberDTO subscriberDTO, @PathVariable String id) throws SubscriberNotFoundException {
        return ResponseEntity.ok(subscriberService.update(id, subscriberDTO));
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity cancel(@PathVariable final String id) throws SubscriberNotFoundException {
        subscriberService.cancel(id);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            final MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
