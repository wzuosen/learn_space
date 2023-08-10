package cn.wzs.java_base.nio.talkroom.holder;

import cn.wzs.java_base.nio.talkroom.base.TalkUser;
import cn.wzs.java_base.nio.talkroom.utils.AssertUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class TalkUserHolder {

    private final Map<String, TalkUser> USER_HOLDER = new ConcurrentHashMap<>(16);

    public void addUser(TalkUser user) {
        AssertUtils.notNull(user);
        AssertUtils.notNull(user.getCode());
        USER_HOLDER.put(user.getCode(), user);
    }


    public void removeUser(String code) {
        if (code == null) {
            return;
        }
        USER_HOLDER.remove(code);
    }

    public Collection<TalkUser> getAllUsers() {
        return new ArrayList<>(USER_HOLDER.values());
    }

    public Collection<TalkUser> getAllUsersIgnore(String... codes) {
        Collection<TalkUser> allUsers = getAllUsers();
        if (codes != null && codes.length > 0) {
            Iterator<TalkUser> iterator = allUsers.iterator();
            Set<String> codeSet = new HashSet<>(Arrays.asList(codes));
            while (iterator.hasNext()) {
                if (codeSet.contains(iterator.next().getCode())) {
                    iterator.remove();
                }
            }
        }
        return allUsers;
    }

    public TalkUser getUser(String code) {
        return USER_HOLDER.get(code);
    }
}
