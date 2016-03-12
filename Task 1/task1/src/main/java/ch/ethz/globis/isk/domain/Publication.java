package ch.ethz.globis.isk.domain;

import java.util.List;
import java.util.Set;

/**
 * Defines the base state for a publication. Is inherited by all specialized
 * types of publications.
 */
public interface Publication extends DomainObject {

    public String getTitle();

    public void setTitle(String title);

    public List<Person> getAuthors();

    public void setAuthors(List<Person> authors);

    public Set<Person> getEditors();

    public void setEditors(Set<Person> editors);

    public int getYear();

    public void setYear(int year);

    public String getElectronicEdition();

    public void setElectronicEdition(String electronicEdition);
    
}