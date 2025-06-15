package it.uniroma3.siw.siwbooks.controller.validator;


import it.uniroma3.siw.siwbooks.model.Credentials;
import it.uniroma3.siw.siwbooks.model.Review;
import it.uniroma3.siw.siwbooks.service.CredentialsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CredentialsValidator implements Validator {
    @Autowired
    private CredentialsService credentialsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Credentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Credentials credentials = (Credentials) target;
        if(credentials.getUsername() != null && credentialsService.credentialsExistsByUsername(credentials.getUsername())) {
            errors.rejectValue("username", "Username.exists");
        }
        if(credentials.getPassword() != null && credentials.getPassword().length() < 5) {
            errors.rejectValue("password", "Password.length");
        }
        if(credentials.getUsername() != null && credentials.getUsername().length() < 5) {
            errors.rejectValue("username", "Username.length");
        }
        if(credentials.getPassword() != null && !credentials.getPassword().matches(".*\\d.*")) {
            errors.rejectValue("password", "Password.number");
        }
    }
}

