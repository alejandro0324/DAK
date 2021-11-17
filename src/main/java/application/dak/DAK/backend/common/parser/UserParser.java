package application.dak.DAK.backend.common.parser;

import application.dak.DAK.backend.common.models.User;
import com.google.firebase.auth.ListUsersPage;

import java.util.ArrayList;
import java.util.List;

public class UserParser {

    public List<User> userParse(ListUsersPage users) {
        List<User> list = new ArrayList<>();
        var aux = users.getValues();
        aux.forEach(i -> list.add(new User(i.getEmail(), i.getUid())));
        return list;
    }

}
