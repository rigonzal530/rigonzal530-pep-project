package Service;

import DAO.AccountDAO;

public class AccountService {
    private AccountDAO accountDAO;

    // constructor to initialize accountDAO dependency
    public AccountService() {
        this.accountDAO = new AccountDAO();
    }
    
}
