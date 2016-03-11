package com.teamoldspice.validator;

import com.teamoldspice.model.Person;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PersonValidator implements Validator{
    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        Person person = (Person) target;

        Pattern pattern = Pattern.compile(".+@.+");
        Matcher matcher = pattern.matcher(person.getUsername());

        if (!matcher.find()){
            errors.rejectValue("username", "field.email");
        }

    }
}
