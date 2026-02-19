package natto;

class Contact extends Task {
    private final String phone;
    private final String email;
    private final String address;

    public Contact(String name, String phone, String email, String address) {
        super(name);
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    @Override
    public String toString() {
        return "[C]" + super.toString()
                + " (p: " + phone
                + ", e: " + email
                + (address == null || address.isBlank() ? "" : ", a: " + address)
                + ")";
    }

    public String getPhone() {
        return phone; }
    public String getEmail() {
        return email; }
    public String getAddress() {
        return address; }
}
