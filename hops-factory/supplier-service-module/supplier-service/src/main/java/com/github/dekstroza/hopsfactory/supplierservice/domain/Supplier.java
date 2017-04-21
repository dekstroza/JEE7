package com.github.dekstroza.hopsfactory.supplierservice.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.UUID;

@ApiModel(description = "Supplier class")
@XmlRootElement
@Entity(name = "supplier")
@Table(name = "suppliers", schema = "supplier_service")
public class Supplier implements Serializable {

    @ApiModelProperty(name = "id", dataType = "string", value = "Id of this supplier, in form of uuid as string", example = "571fbefb-f96e-40c7-b699-94ac2403eab4", notes = "Not required when creating new instance, id will be generated automatically.")
    @Id
    @GeneratedValue
    @Column
    private UUID id;

    @ApiModelProperty(name = "name", value = "Name of this supplier")
    @Column
    private String name;

    @ApiModelProperty(name = "address", value = "Address of this supplier")
    @Column
    private String address;

    @ApiModelProperty(name = "phone", value = "Phone number of this supplier, as string, no specific format.")
    @Column
    private String phone;

    @ApiModelProperty(name = "email", value = "Email address of this supplier as string.", example = "someone@somewhere.com", notes = "Must be valid email.")
    @Column
    private String email;

    public Supplier() {

    }

    /**
     * Constructor with required persistence attributes
     *
     * @param name
     * @param address
     * @param phone
     * @param email
     */
    public Supplier(String name, String address, String phone, String email) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Supplier copyFrom(final Supplier newSupplier) {
        setId(newSupplier.getId());
        setPhone(newSupplier.getPhone());
        setEmail(newSupplier.getEmail());
        setAddress(newSupplier.getAddress());
        setName(newSupplier.getName());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Supplier supplier = (Supplier) o;

        if (!getId().equals(supplier.getId()))
            return false;
        if (!getName().equals(supplier.getName()))
            return false;
        if (!getAddress().equals(supplier.getAddress()))
            return false;
        if (!getPhone().equals(supplier.getPhone()))
            return false;
        return getEmail().equals(supplier.getEmail());
    }

    @Override
    public int hashCode() {
        int result = getId().hashCode();
        result = 31 * result + getName().hashCode();
        result = 31 * result + getAddress().hashCode();
        result = 31 * result + getPhone().hashCode();
        result = 31 * result + getEmail().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Supplier{" + "id=" + id + ", name='" + name + '\'' + ", address='" + address + '\'' + ", phone='" + phone + '\'' + ", email='" + email
                   + '\'' + '}';
    }
}
