package org.no_ip.mikelue.jpa.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.*;

/**
 * This entity is for testing
 */
@Entity
@Table(name="tt_car")
@NamedQueries(
    @NamedQuery(
        name="findByName",
        query=
        " SELECT c" +
        " FROM Car AS c"  +
        " WHERE c.name = :name"
    )
)
public class Car {
    public Car() {}

    @Id
    @Column(name="car_id")
    private Integer id;
    @Column(name="car_name", nullable=false, length=64)
    private String name;
    @Column(name="car_address", nullable=false, length=128)
    private String address;

    public Integer getId() { return this.id; }
    public void setId(Integer newId) { this.id = newId; }

	public void setName(String newName) { this.name = newName; }
	public String getName() { return this.name; }

    public void setAddress(String newAddress) { this.address = newAddress; }
    public String getAddress() { return this.address; }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null) { return false; }
        if (obj == this) { return true; }

        if (!getClass().isInstance(obj)) {
            return false;
        }

        Car another = (Car)obj;
        return new EqualsBuilder()
            .append(this.getId(), another.getId())
            .isEquals();
    }
    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(2231, 7677)
            .append(this.getId())
            .toHashCode();
    }
    @Override
    public String toString()
    {
        return String.format("[%d] '%s'(%s)", getId(), getName(), getAddress());
    }
}

