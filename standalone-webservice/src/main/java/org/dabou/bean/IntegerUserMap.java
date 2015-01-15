package org.dabou.bean;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "IntegerUserMap")
@XmlType(name = "IntegerUserMap")
@XmlAccessorType(XmlAccessType.FIELD)
public class IntegerUserMap {

    @XmlElement(nillable = false, name = "entry")
    List<IntegerUserEntry> entries = new ArrayList<IntegerUserEntry>();

    public List<IntegerUserEntry> getEntries() {
        return entries;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "IdentifiedUser")
    static class IntegerUserEntry {

        //Map keys cannot be null
        @XmlElement(required = true, nillable = false)
        Integer id;

        User user;

        public void setId(Integer k) {
            id = k;
        }

        public Integer getId() {
            return id;
        }

        public void setUser(User u) {
            user = u;
        }

        public User getUser() {
            return user;
        }
    }
}
