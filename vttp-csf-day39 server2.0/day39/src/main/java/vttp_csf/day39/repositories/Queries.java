package vttp_csf.day39.repositories;

public interface Queries {
    public static final String SQL_FIND_EMPLOYEES_BY_EMAIL = "select * from employees where email_address = ?";
}
