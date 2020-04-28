package umari.datafilter.impl;

import org.springframework.stereotype.Service;
import umari.datafilter.service.UDFTemplate;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class UDFTemplateImpl implements UDFTemplate {

    @PersistenceContext
    private EntityManager em;

}
