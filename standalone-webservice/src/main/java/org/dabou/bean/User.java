package org.dabou.bean;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;import java.lang.String;

@XmlJavaTypeAdapter(UserAdapter.class)
public interface User {

    String getName();
}

