package it.uniroma3.siw.siwbooks.controller.validator;

import it.uniroma3.siw.siwbooks.model.Author;
import it.uniroma3.siw.siwbooks.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class AuthorValidator implements Validator {

    @Autowired
    private AuthorService authorService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Author.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Author author = (Author) target;

        if (author.getDateOfDeath() != null && author.getDateOfBirth() != null) {
            if (author.getDateOfDeath().isBefore(author.getDateOfBirth())) {
                errors.rejectValue("dateOfDeath", "dateOfDeath.beforeBirth", "La Data di Morte deve essere successiva alla Data di Nascita");
            }
        }

        if(author.getName() != null && author.getSurname() != null && author.getDateOfBirth() != null) {
            if(authorService.authorExistsByNameAndLastNameAndDateOfBirth(author.getName(), author.getSurname(), author.getDateOfBirth())){
                errors.reject("Author.duplicate");
            }
        }
    }
}
