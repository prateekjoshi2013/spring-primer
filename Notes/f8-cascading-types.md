### What is cascading ?
- Cascading is the way to achieve this. When we perform some action on the target entity, the same action will be applied to the associated entity.

### JPA Cascade Type

- All JPA-specific cascade operations are represented by the javax.persistence.CascadeType enum containing entries:

- ALL
    - CascadeType.ALL propagates all operations — including Hibernate-specific ones — from a parent to a child entity.
    
    ```java
        @Entity
        public class Person {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private int id;
            private String name;
            @OneToMany(mappedBy = "person", cascade = CascadeType.ALL)
            private List<Address> addresses;
        }

        @Entity
        public class Address {
            @Id
            @GeneratedValue(strategy = GenerationType.AUTO)
            private int id;
            private String street;
            private int houseNumber;
            private String city;
            private int zipCode;
            @ManyToOne(fetch = FetchType.LAZY)
            private Person person;
        }
    ```

- PERSIST
    
    - The persist operation makes a transient instance persistent. 
    
    - Cascade Type PERSIST propagates the persist operation from a parent to a child entity. 
    
    - When we save the person entity, the address entity will also get saved.

    ```java
        @Test
        public void whenParentSavedThenChildSaved() {
            Person person = new Person();
            Address address = new Address();
            address.setPerson(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            session.clear();
        }
    ```

- MERGE
    
    - The merge operation copies the state of the given object onto the persistent object with the same identifier. 
    
    - CascadeType.MERGE propagates the merge operation from a parent to a child entity.

    ```java
        @Test
        public void whenParentSavedThenMerged() {
            int addressId;
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            addressId = address.getId();
            session.clear();

            Address savedAddressEntity = session.find(Address.class, addressId);
            Person savedPersonEntity = savedAddressEntity.getPerson();
            savedPersonEntity.setName("devender kumar");
            savedAddressEntity.setHouseNumber(24);
            session.merge(savedPersonEntity);
            session.flush();
        }
    ```

- REMOVE

    - As the name suggests, the remove operation removes the row corresponding to the entity from the database and also from the persistent context.

    - CascadeType.REMOVE propagates the remove operation from parent to child entity.
    
    - Similar to JPA’s CascadeType.REMOVE, we have CascadeType.DELETE, which is specific to Hibernate. There is no difference between the two.

    ```java
        @Test
        public void whenParentRemovedThenChildRemoved() {
            int personId;
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            personId = person.getId();
            session.clear();

            Person savedPersonEntity = session.find(Person.class, personId);
            session.remove(savedPersonEntity);
            session.flush();
        }
    ```

- REFRESH
    
    - Refresh operations reread the value of a given instance from the database. 
    
    - In some cases, we may change an instance after persisting in the database, but later we need to undo those changes.

    - In that kind of scenario, this may be useful. 
   
    - When we use this operation with Cascade Type REFRESH, the child entity also gets reloaded from the database whenever the parent entity is refreshed.

    ```java
        @Test
        public void whenParentRefreshedThenChildRefreshed() {
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            person.setName("Devender Kumar");
            address.setHouseNumber(24);
            session.refresh(person);
            
            assertThat(person.getName()).isEqualTo("devender");
            assertThat(address.getHouseNumber()).isEqualTo(23);
        }
    ```

- DETACH

    - The detach operation removes the entity from the persistent context. When we use CascadeType.DETACH, the child entity will also get removed from the persistent context.

    ```java
        @Test
        public void whenParentDetachedThenChildDetached() {
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            
            assertThat(session.contains(person)).isTrue();
            assertThat(session.contains(address)).isTrue();

            session.detach(person);
            assertThat(session.contains(person)).isFalse();
            assertThat(session.contains(address)).isFalse();
        }
    ```

- Hibernate Cascade Type

- Hibernate supports three additional Cascade Types along with those specified by JPA. 

- These Hibernate-specific Cascade Types are available in org.hibernate.annotations.CascadeType:

- REPLICATE
    
    - The replicate operation is used when we have more than one data source and we want the data in sync. 
    
    - With CascadeType.REPLICATE, a sync operation also propagates to child entities whenever performed on the parent entity.

    ```java
        @Test
        public void whenParentReplicatedThenChildReplicated() {
            Person person = buildPerson("devender");
            person.setId(2);
            Address address = buildAddress(person);
            address.setId(2);
            person.setAddresses(Arrays.asList(address));
            session.unwrap(Session.class).replicate(person, ReplicationMode.OVERWRITE);
            session.flush();
            
            assertThat(person.getId()).isEqualTo(2);
            assertThat(address.getId()).isEqualTo(2);
        }
    ```
    - Because of CascadeType.REPLICATE, when we replicate the person entity, its associated address also gets replicated with the identifier we set.

- SAVE_UPDATE

    - CascadeType.SAVE_UPDATE propagates the same operation to the associated child entity. 
    
    - It’s useful when we use Hibernate-specific operations like save, update and saveOrUpdate. 

    ```java
        @Test
        public void whenParentSavedThenChildSaved() {
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.saveOrUpdate(person);
            session.flush();
        }
    ```
    - Because of CascadeType.SAVE_UPDATE, when we run the above test case, we can see that the person and address both got saved.

- LOCK

    - Unintuitively, CascadeType.LOCK reattaches the entity and its associated child entity with the persistent context again.

    ```java
        @Test
        public void whenDetachedAndLockedThenBothReattached() {
            Person person = buildPerson("devender");
            Address address = buildAddress(person);
            person.setAddresses(Arrays.asList(address));
            session.persist(person);
            session.flush();
            
            assertThat(session.contains(person)).isTrue();
            assertThat(session.contains(address)).isTrue();

            session.detach(person);
            assertThat(session.contains(person)).isFalse();
            assertThat(session.contains(address)).isFalse();
            session.unwrap(Session.class)
            .buildLockRequest(new LockOptions(LockMode.NONE))
            .lock(person);

            assertThat(session.contains(person)).isTrue();
            assertThat(session.contains(address)).isTrue();
        }
    ```
    - As we can see, when using CascadeType.LOCK, we attached the entity person and its associated address back to the persistent context.



