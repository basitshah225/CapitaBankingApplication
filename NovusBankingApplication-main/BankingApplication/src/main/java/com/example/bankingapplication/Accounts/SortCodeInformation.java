package com.example.bankingapplication.Accounts;

/**
 * Class which represents the sort code information
 * Can return different sort codes from Account Manager depending on the branch passed in
 */
public class SortCodeInformation {

    /**
     * The name of the branch
     */
    private final String bankBranch;

    /**
     * Constructor for class
     * @param _bankBranch The name of the branch
     */
    public SortCodeInformation(String _bankBranch){
        bankBranch = _bankBranch;
    }

    /**
     * @return The sort code based on the branch
     */
    public int getSortCode(){
        if(bankBranch.equals("Capita")){
            return 101010;
        }else if(bankBranch.equals("South")){
            return 111111;
        }
        return 0;
    }
}
