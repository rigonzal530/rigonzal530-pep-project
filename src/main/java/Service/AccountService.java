package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    // constructor to initialize accountDAO dependency
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }
    
    /**
     * Registers a new user account within the Account table if all requirements were met
     * 
     * 1. username must not be blank
     * 2. password must be at least 4 characters long
     * 3. username must not already be in use within the Account table 
     * 
     * @param userAcc - an Account object without an account_id to be registered within the Account table
     * @return A fully populated Account object (has its account_id) on successful insertion, or null on requirements failure / insertion failure
     */
    public Account registerUserAccount(Account userAcc) {
        // the username must not be blank, password must be at least 4 characters long, and an Account with a matching username must not already exist
        boolean newUserRequirements = !(userAcc.getUsername().isBlank()) && (userAcc.getPassword().length() >= 4) && !(this.accountDAO.usernameAlreadyExists(userAcc.getUsername()));

        // if all the new user account requirements were met, then attempt to register them
        if (newUserRequirements) {
            return this.accountDAO.insertUser(userAcc);
        }

        return null;
    }
}
