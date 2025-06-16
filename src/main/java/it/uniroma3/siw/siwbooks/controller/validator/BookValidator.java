package it.uniroma3.siw.siwbooks.controller.validator;


import it.uniroma3.siw.siwbooks.model.Book;
import it.uniroma3.siw.siwbooks.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BookValidator implements Validator {

    @Autowired
    private BookService bookService;

    @Override
    public boolean supports(Class<?> clazz) {
        return Book.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Book book = (Book) target;

        if(book.getTitle() != null && book.getPublicationYear() != null) {
            if(bookService.bookExistsByNameAndPublicationYear(book.getTitle(), book.getPublicationYear())){
                errors.reject("Book.duplicate");
            }
        }
    }
}