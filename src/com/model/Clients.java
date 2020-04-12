package com.model;

/**
 * This class is used to hold records from the clients table.
 */
public class Clients implements Comparable<Clients>{
    /**
     * The client id.
     */
    private int id;
    /**
     * The client's first name.
     */
    private String firstName;
    /**
     * The client's last name.
     */
    private String lastName;
    /**
     * The client's address.
     */
    private String address;

    public Clients(int id, String firstName, String lastName, String address) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
    }

    public Clients() {
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    /**
     * Compares alphabetically two clients based on their name.
     * @param o The client to compare with this client.
     * @return 0, 1 or -1 based on the comparison.
     */
    @Override
    public int compareTo(Clients o) {
        if(this.getFirstName().compareTo(o.getFirstName()) > 0) {
            return 1;
        }
        else if(this.getFirstName().compareTo(o.getFirstName()) < 0) {
            return -1;
        }
        else if(this.getFirstName().compareTo(o.getFirstName()) == 0) {
            if(this.getLastName().compareTo(o.getLastName()) > 0) {
                return 1;
            }
            else if(this.getLastName().compareTo(o.getLastName()) < 0) {
                return -1;
            }
        }
        return 0;
    }
}
