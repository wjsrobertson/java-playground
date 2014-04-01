package net.xylophones.protbuf;

public class Main {

    public static void main(String[] args) {
        AddressBookProtos.Person person = AddressBookProtos.Person.newBuilder()
                .setEmail("wjsrobertson@gmail.com")
                .setName("will")
                .setId(10)
                .build();

        System.out.println("size: " + person.getSerializedSize());
        System.out.println("initialised: " + person.isInitialized());

        System.out.println( person.toByteArray() );

        System.out.println(person);
    }

}
