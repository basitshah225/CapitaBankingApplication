package com.example.bankingapplication.Security;

import com.example.bankingapplication.Accounts.PhotoIDType;

import java.util.regex.Pattern;

/**
 * Verifies the inputs based on what the input should be
 */
public class InputVerifier {

    /**
     * Returns true if the value is a number, can be any size
     * @param number The number to check
     * @return true if the string is a number
     */
    public boolean isANumber(String number){
        Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
        if (number == null) {
            return false;
        }
        return pattern.matcher(number).matches();
    }

    /**
     * Returns true if the value is an integer
     * @param number The number to check
     * @return true if the string is an int
     */
    public boolean isAnInteger(String number){
        boolean isNumber = isANumber(number);
        if(isNumber){
            try {
                int num = Integer.parseInt(number);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Returns true if the value is a double
     * @param number The number to check
     * @return true if the string is a double
     */
    public boolean isADouble(String number){
        boolean isNumber = isANumber(number);
        if(isNumber){
            try {
                double d = Double.parseDouble(number);
                return true;
            } catch (NumberFormatException nfe) {
                return false;
            }
        }else{
            return false;
        }
    }

    /**
     * Returns true if the name only contains any amount of letters
     * @param name The name to check
     * @return true if the string is a name
     */
    public boolean verifyName(String name){
        if(name.isBlank()){
            return false;
        }
        else{
            return name.matches("[a-zA-Z]+");
        }

    }

    /**
     * Returns true if the address matches uk postcodes
     * @param address The address to check
     * @return true if the string is an address
     */
    public boolean verifyAddress(String address){
        String regex = "^[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}$";
        Pattern pattern = Pattern.compile(regex);
        if (address == null) {
            return false;
        }
        return pattern.matcher(address).matches();

    }


    /**
     * Returns true if the value is a double in the range specified
     * @param number The number to check
     * @return true if the number is a double in range
     */
    public boolean isANumberInRange(String number, double lowest, double highest){
        boolean isNum = isADouble(number);
        if(isNum){
            double d = Double.parseDouble(number);
            return d >= lowest && d <= highest;
        }
        return false;
    }

    /**
     * Returns true photo id is valid
     * driver's license can be 10 characters of digits and letters
     * Passport is 10 characters of numbers
     * @param number The number to check
     * @param type the type of id
     * @return true if the number is valid
     */

    public boolean verifyPhotoIDNumber(String number, Enum<PhotoIDType> type){

        if(type.equals(PhotoIDType.driversLicense)){
            if(!number.isBlank()){
                if(number.length() == 10){
                    if(number.matches("[a-zA-Z0-9]*")){
                        return true;
                    }
                }

            }
        }
        else if (type.equals(PhotoIDType.passport)){
            if(!number.isBlank()){
                if(number.length() == 10){
                    if(number.matches("[0-9]*")){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if business ID is an integer of length 9
     * @param businessID The business ID to check
     * @return true if the business ID is valid
     */
    public boolean verifyBusinessID(String businessID){
        if(businessID.length() == 9){
            if(isAnInteger(businessID)){
                return true;
            }
        }
        return false;
    }

    /**
     * Account number returns true if it's an integer
     * @param number The id to check
     * @return true if its valid
     */
    public boolean verifyAccountNumber(String number){
        return isAnInteger(number);
    }

    public boolean verifySortCode(String number){
        if(number.length() == 6){
            return isAnInteger(number);
        }
        return false;
    }


    /**
     * Checks to see if the value is within the personal over draft range
     * @param number the number to check
     * @return true if the values is a number in range
     */
    public boolean verifyPersonalOverdraft(String number) {
        return isANumberInRange(number, 0,  2500);
    }

    /**
     * Checks to see if the value is within the business loan range
     * @param number the number to check
     * @return true if the values is a number in range
     */
    public boolean verifyBusinessLoanAmount(String number) {
        return isANumberInRange(number, 0,  10000000);
    }

    /**
     * Checks to see if the value is within the isa increase range
     * @param number the number to check
     * @return true if the values is a number in range
     */
    public boolean verifyISAIncrease(String number) {
        return isANumberInRange(number, 0,  20000);
    }

    /**
     * Checks to see if the value is within the business over draft range
     * @param number the number to check
     * @return true if the values is a number in range
     */
    public boolean verifyBusinessOverdraft(String number) {
        return isANumberInRange(number, 0,  5000);
    }
}
