/*
* Author: Mehmet Fatih KIZILDAG
* Since: 06.04.2022
* */
import java.util.ArrayList;
import java.util.Random;

public class Assignment02_20190808041 {
    public static void main(String[] args) {
        Bank b = new Bank("My Bank", "My Bank's Address");
        b.addCompany(1, "Company 1");
        b.getCompany(1).openAccount("1234", 0.05);
        b.addAccount(b.getCompany(1).getAccount("1234"));
        b.getAccount("1234").deposit(500000);
        b.getCompany(1).getAccount("1234").deposit(500000);
        b.getCompany(1).openAccount("1235", 0.03);
        b.addAccount(b.getCompany(1).getAccount("1235"));
        b.getCompany(1).getAccount("1235").deposit(25000);
        b.addCompany(2, "Company 2");
        b.getCompany(2).openAccount("2345", 0.03);
        b.addAccount(b.getCompany(2).getAccount("2345"));
        b.getCompany(2).getAccount("2345").deposit(350);
        b.addCustomer(1, "Customer", "1");
        b.addCustomer(2, "Customer", "2");
        Customer c = b.getCustomer(1);
        c.openAccount("3456");
        c.openAccount("3457");
        c.getAccount("3456").deposit(150);
        c.getAccount("3457").deposit(250);
        c = b.getCustomer(2);
        c.openAccount("4567");
        c.getAccount("4567").deposit(1000);
        b.addAccount(c.getAccount("4567"));
        c = b.getCustomer(1);
        b.addAccount(c.getAccount("3456"));
        b.addAccount(c.getAccount("3457"));
        System.out.println(b.toString());
    }
}
class Bank {
    private String Name, Address;
     ArrayList<Customer> Customers = new ArrayList<Customer>();
     ArrayList<Company> Companies = new ArrayList<Company>();
     ArrayList<Account> Accounts = new ArrayList<Account>();
     Bank(String name,String Address){
         this.Name=name;
         this.Address=Address;
     }

    public String getName() {
        return this.Name;
    }
    public void setName(String name) {
        this.Name = name;
    }
    public String getAddress() {
        return this.Address;
    }
    public void setAddress(String address) {
        this.Address = address;
    }
    public void addCustomer(int id, String name,String surname) {
        Customers.add(new Customer(id,name, surname));
    }
    public void addCompany(int id, String name) {
        Companies.add(new Company(id, name));
    }
    public void addAccount(Account acc) {
        Accounts.add(acc);
    }
    public Customer getCustomer(int id) {
        if (Customers.size() != 0) {
            for (Customer i : Customers) {
                if (i.getId() == id) {
                    return i;
                }
            }
        } else {
            throw new CustomerNotFoundException(id);
        }
        return null;
    }
    public Customer getCustomer(String name,String surname) {
        if (Customers.size() != 0) {
            for (Customer i : Customers) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
        } else {
            throw new CustomerNotFoundException(name,surname);
        }
        return null;
    }
    public Company getCompany(int id){
        if (Companies.size() != 0) {
            for (Company i : Companies) {
                if (i.getId()==id) {
                    return i;
                }
            }
        } else {
            throw new CompanyNotFoundException(id);
        }
        return null;
    }
    public Company getCompany(String name){
        if (Companies.size() != 0) {
            for (Company i : Companies) {
                if (i.getName().equals(name)) {
                    return i;
                }
            }
        } else {
            throw new CompanyNotFoundException(name);
        }
        return null;
    }
    public Account getAccount(String accountNum){
        if (Accounts.size() != 0) {
            for (Account i : Accounts) {
                if (i.getAcctNum().equals(accountNum)) {
                    return i;
                }
            }
        } else {
            throw new CompanyNotFoundException(accountNum);
        }
        return null;
    }
    public void transferFunds(String accountFrom,String accountTo,double amount){
            for (Account i : Accounts) {
                if(i.getAcctNum()==accountFrom){
                    i.withdrawal(amount);
                }
                else throw new AccountNotFoundException(accountFrom);
            }
            for (Account i: Accounts){
                if (i.getAcctNum()==accountTo){
                    i.deposit(amount);
                }
                else throw new AccountNotFoundException(accountTo);
            }
    }
    public void closeAccount(String accountNum){
        for (Account i : Accounts) {
            if (i.getAcctNum()==accountNum && i.getBalance()<=0) {
                Accounts.remove(i);
            }
            else if (i.getAcctNum()!=accountNum){
                throw new AccountNotFoundException(accountNum);
            }
            else {throw new BalanceRemainingException(i.getBalance());}
        }
    }

    final String companyToString(){
        String company="";
        for(Company i: Companies){
        company+= i.getName()+"\n"+"\t"+i.getBusinessAccounts();
        }
        return company;
    }
    final String customerToString(){
        String customer="";
        for(Customer i: Customers){
            customer+= i.getName()+"\n"+"\t"+i.getPersonalAccounts();
        }
        return customer;
    }
    @Override
    public String toString(){
        return this.Name+this.Address+"\n"+"\t" +companyToString()+customerToString();
    }


}
class Account{
    private String AccountNumber;
    private double Balance;

    public Account(String accountNumber) {
        this.AccountNumber=accountNumber;
        this.Balance=0;
    }
    public Account(String accountNumber,double balance){
        this.AccountNumber=accountNumber;
        if (balance<0){
            throw new InvalidValueException(balance);
        }
        else
            this.Balance=balance;
    }
    public String getAcctNum(){
        return AccountNumber;
    }
    public double getBalance(){
        return Balance;
    }
    public void deposit(double amount){
        if (amount>=0){
            Balance+=amount;
        }
        else throw new InvalidAmountException(amount);
    }
    public void withdrawal(double amount){
        if (amount>=0 || amount<=Balance){
            Balance-=amount;
        }
        else throw new InvalidAmountException(amount);
    }
    @Override
    public String toString(){
        return "Account "+AccountNumber+" has "+ Balance;
    }
}
class PersonalAccount extends Account{
    private String Name;
    private String Surname;
    private String PIN;


    public PersonalAccount(String accountNumber, String name, String surname) {
        super(accountNumber);
        this.Name = name;
        this.Surname = surname;
        Random rand = new Random();
        this.PIN=String.format("%04d%n", rand.nextInt(10000));
    }
    public PersonalAccount (String accountNumber,String name,String surname,double balance){
        super(accountNumber,balance);
        this.Name=name;
        this.Surname=surname;
        Random rand = new Random();
        this.PIN=String.format("%04d%n", rand.nextInt(10000));
    }
    public String getAccountNumber(){
        return super.getAcctNum();
    }
    public String getName(){
        return Name;
    }
    public void setName(String name){
        this.Name=name;
    }
    public String getSurname(){
        return Surname;
    }
    public void setSurname(String surname){
        this.Surname=surname;
    }
    public String getPIN(){
        return PIN;
    }
    public void setPIN(String PIN){
        this.PIN=PIN;
    }
    public String toString(){
        return "Account "+super.getAcctNum()+" belonging to "+Name+" "+Surname.toUpperCase()+" has "+super.getBalance();
    }
}
class BusinessAccount extends Account{
    private double InterestRate;

    public BusinessAccount(String accountNumber,double rate) {
        super(accountNumber);
        if (rate>=0){
        this.InterestRate=rate;}
        else throw new InvalidValueException(rate);
    }
    public BusinessAccount(String accountNumber,double balance,double rate){
        super(accountNumber,balance);
        this.InterestRate=rate;
    }
    public double getRate(){
        return InterestRate;
    }
    public void setRate(double rate){
        if (rate>=0){
        this.InterestRate=rate;}
    }
    public double calculateInterest(){
        return getBalance()*InterestRate;
    }

}
class Customer{
    private int id;
    private String Name;
    private String Surname;
    private ArrayList<PersonalAccount> PersonalAccounts=new ArrayList<>();

    public Customer(int id,String name,String surname){
        this.Name=name;
        this.Surname=surname;
        if (id>0){
            this.id=id;}
        else throw new InvalidValueException(id);
    }
    public String getPersonalAccounts(){
        String res="";
        for (PersonalAccount i:PersonalAccounts){
            res+= i.getAcctNum()+"\t"+i.getBalance()+"\n\t";
        }
        return res;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getSurname() {
        return Surname;
    }
    public void setSurname(String surname) {
        Surname = surname;
    }
    public int getId(){
         return this.id;
     }
    public void setId(int id){
        if (id>0){
         this.id=id;}
        else throw new InvalidValueException(id);
     }
    public void openAccount(String acctNum){
        PersonalAccounts.add(new PersonalAccount(acctNum,Name,Surname));
    }
    public PersonalAccount getAccount(String accountNum){
        int counter=0;
        for (PersonalAccount i: PersonalAccounts){
            if (i.getAcctNum()==accountNum){
                counter++;
                return i;
            }
        }
        if (counter!=1){
            throw new AccountNotFoundException(accountNum);
        }
        return null;
    }
    public void closeAccount(String accountNum){
        for (PersonalAccount i:  PersonalAccounts) {
            if (i.getAcctNum()==accountNum){
                if (i.getBalance()<=0){
                    PersonalAccounts.remove(i);
                    }
                else {throw new BalanceRemainingException(i.getBalance());}
            }
            else throw new AccountNotFoundException(accountNum);
        }
    }

    public String toString(){
        return Name+" "+Surname.toUpperCase();
    }
}
class Company{
    private int id;
    private String Name;
    private ArrayList<BusinessAccount> BusinessAccounts=new ArrayList<>();

    public Company(int id,String name){
        this.Name=name;

        if (id>0){
        this.id=id;}
        else throw new InvalidValueException(id);
    }
    public String getBusinessAccounts(){
        String res="";
        for (BusinessAccount i:BusinessAccounts){
            res+= i.getAcctNum()+"\t"+i.getRate()+"\t"+i.getBalance()+"\n"+"\t";
        }
        return res;
    }
    public int getId(){
        return this.id;
    }
    public void setId(int id){
        if (id>0){
            this.id=id;}
        else throw new InvalidValueException(id);
    }
    public String getName(){
        return Name;
    }
    public void setName(String name){
        this.Name=name;
    }
    public void openAccount(String acctNum,double rate){
        BusinessAccounts.add(new BusinessAccount(acctNum,rate));
    }
    public BusinessAccount getAccount(String acctNum){
        int counter=0;
        for (BusinessAccount i: BusinessAccounts){
            if (i.getAcctNum()==acctNum){
                counter++;
                return i;
            }
        }
        if (counter!=1){
         throw new AccountNotFoundException(acctNum);}
        return null;
    }
    public void closeAccount(String accountNum){
        for (Account i:  BusinessAccounts) {
            if (i.getAcctNum()==accountNum){
                if (i.getBalance()<=0){
                    BusinessAccounts.remove(i);
                }
                else {throw new BalanceRemainingException(i.getBalance());}
            }
            else throw new AccountNotFoundException(accountNum);
        }
    }

    public String toString(){
        return Name;
    }
}
class AccountNotFoundException extends RuntimeException{
    private String acctNum;

    public AccountNotFoundException(String acctNum) {
        this.acctNum=acctNum;
    }
    public String toString() {
        return "AccountNotFoundException: " + acctNum;
    }
}
class BalanceRemainingException extends RuntimeException{
    private double balance;

    public BalanceRemainingException(double balance) {
        this.balance=balance;
    }
    double getBalance(){
        return balance;
    }
    public String toString() {
        return "BalanceRemainingException: " + balance;
    }
}
class CompanyNotFoundException extends RuntimeException{
    private String name;
    private int id;

    public CompanyNotFoundException(int id) {
        this.id=id;
        this.name=null;
    }
    public CompanyNotFoundException(String name) {
        this.name=name;
        this.id=0;
    }
    public String toString() {
        if (this.id==0){
        return "CompanyNotFoundException: name - " + name;}
        else
            return "CompanyNotFoundException: id -"+id;
    }
}
class CustomerNotFoundException extends RuntimeException{
    private String name, surname;
    private int id;

    public CustomerNotFoundException(int id){
        this.id=id;
        this.name=null;
        this.surname=null;
    }
    public CustomerNotFoundException(String name,String surname){
        this.id=0;
        this.name=name;
        this.surname=surname;
    }

    public String toString() {
        if (this.name!=null && this.surname!=null){
        return "CustomerNotFoundException: name - " + name +" "+surname;}
        else
            return "CustomerNotFoundException: id - "+id;
    }
}
class InvalidAmountException extends RuntimeException{
    private double amount;
    public InvalidAmountException(double amount){
        this.amount=amount;
    }
    public String toString(){
        return "InvalidAmountException: "+amount;
    }
}
class InvalidValueException extends RuntimeException{
    private double value;
    public InvalidValueException(double value){
        this.value=value;
    }
    public String toString(){
        return "InvalidValueException: "+value;
    }
}
