    use mydb;

create table beer_audit (
    beer_style integer check (beer_style between 0 and 9), 
    price numeric(38,2), 
    quantity_on_hand integer, 
    version integer, 
    created_date datetime(6), 
    created_date_audit datetime(6), 
    update_date datetime(6), 
    upc varchar(10), 
    audit_id varchar(36) not null, 
    id varchar(36) not null, 
    beer_name varchar(50), 
    audit_event_type varchar(255),
    principal_name varchar(255), 
    primary key (audit_id)
) engine=InnoDB;
