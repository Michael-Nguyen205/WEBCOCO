package spring.boot.webcococo.services.impl;

import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import spring.boot.webcococo.entities.Action;
import spring.boot.webcococo.entities.Contact;
import spring.boot.webcococo.models.requests.ContactRequest;
import spring.boot.webcococo.repositories.ActionsRepository;
import spring.boot.webcococo.repositories.ContactRepository;
import spring.boot.webcococo.services.IActionService;
import spring.boot.webcococo.services.IContactService;

import java.util.List;



@Service
public class ContactServiceImpl extends BaseServiceImpl<Contact, Integer, ContactRepository> implements IContactService {
//
//
//@Autowired
//private ActionsRepository actionsRepository;



    private  final ContactRepository contactRepository;
    public ContactServiceImpl(ContactRepository contactRepository, ContactRepository contactRepository1) {
        super(contactRepository); // Truyền repository vào lớp cha
        this.contactRepository = contactRepository1;
    }


    @Override
    public Contact createContact(ContactRequest request, String locale) {

        Contact contact = new Contact();
        contact.setFullName(request.getFullName());
        contact.setEmail(request.getEmail());
        contact.setPhoneNumber(request.getPhoneNumber());
        contact.setMessage(request.getMessage());
        contact.setStatus(Contact.Status.PENDING);
        contact.setLocale(locale);
        save(contact);
        return contact;
    }

    @Override
    public List<Contact> getAllContact(String keyword,String status,String  locale ,PageRequest pageRequest ) {

        try {
            Page<Contact> contactPage = contactRepository.searchContacts(getValue(locale), getValue(status), getValue(keyword), pageRequest);

            return contactPage.map(ProductResponse::fromProduct);


            return contactPage;
        } catch (Exception e) {
            throw e;
        }

        return null;

    }


    private String getValue(String value) {
        if(StringUtils.isEmpty(value)) {
            return "ALL";
        }
        return value;
    }

    private String getValue(Long value) {
        if(value == null || value == 0) {
            return "ALL";
        }
        return value.toString();
    }

    private String getValue( Float value) {
        if(value == null || value == 0) {
            return "ALL";
        }
        return value.toString();
    }


}
