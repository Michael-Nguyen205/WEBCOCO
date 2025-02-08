package spring.boot.webcococo.services;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import spring.boot.webcococo.entities.Contact;
import spring.boot.webcococo.models.requests.ContactRequest;

import java.util.List;

public interface IContactService {

    Contact createContact(ContactRequest request , String locale);


    List<Contact> getAllContact( String keyword ,String status ,  String locale, PageRequest request);



}
