package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public List<User> getUsers() {
        List<User> users = jdbcTemplate.query("select id from users u ;", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                return user;
            }
        });
        ArrayList<User> resultUsers = new ArrayList<>();
        for (User user : users) {
            resultUsers.add(getUser(user.getId()));
        }
        return resultUsers;
    }


    public User getUser(Integer id) {
        return jdbcTemplate.queryForObject("SELECT u.id, u.email, u.login, u.name, u.birthday, lf.id_friend\n" +
                        "FROM users u \n" +
                        "LEFT JOIN list_friends lf ON u.id = lf.id_user\n" +
                        "WHERE id= ?",
                new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                Set<Integer> listFriends = new HashSet<>();
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                Integer idFriend = rs.getInt("id_friend");
                do {
                    listFriends.add(rs.getInt("id_friend"));
                } while (rs.next());
                user.setListFriends(listFriends);
                return user;
            }
        }, id);
    }

    public List<User> getFriendsUser(Integer idUser) {
        List<User> userListFriend = jdbcTemplate.query("select id_friend as id \n" +
                "from list_friends lf where lf.id_user =?;", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                return user;
            }
        },  idUser);

        List<User> listFriend = new ArrayList<>();
        for (User user : userListFriend) {
            listFriend.add(getUser(user.getId()));
        }
        return listFriend;
    }

    public List<User> getListMutualFriend(Integer userId, Integer otherId) {
        List<User> userListFriend = jdbcTemplate.query("SELECT id_friend AS id\n" +
                "FROM list_friends\n" +
                "WHERE id_user = ?  AND id_friend IN (\n" +
                "    SELECT id_friend\n" +
                "    FROM list_friends\n" +
                "    WHERE id_user = ? \n" +
                ");", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                return user;
            }
        }, userId, otherId);

        List<User> listFriend = new ArrayList<>();
        for (User user : userListFriend) {
            listFriend.add(getUser(user.getId()));
        }
        return listFriend;

    }

    public void deleteFriend(int userId, int friendId) {
        jdbcTemplate.update("delete from list_friends where id_user =? and id_friend = ?;", userId, friendId);
    }

    public List<Integer> getListFriend(int friendId) {
       return jdbcTemplate.query("select id_friend  from list_friends lf where id_user = ?;", new RowMapper<Integer>() {
            @Override
            public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
                Integer id = (rs.getInt("id_friend"));
                return id;
            }
        }, friendId);
    }

    public void addFriend(int userId, int friendId) {
        List<Integer> frendUser = getListFriend(userId);
        List<Integer> frendfrend = getListFriend(friendId);
        int i1 = 0;
        for (int i = 0; i < frendUser.size(); i++) {
            if (friendId == frendUser.get(i)) {
               i1++;
            }
        }
        for (int i = 0; i < frendfrend.size(); i++) {
            if (userId == frendfrend.get(i)) {
                i1++;
            }
        }
        if (i1 == 2) {
            jdbcTemplate.update("update list_friends set user_frends = 1 where id_user = ? and id_friend = ?;", userId, friendId);
            jdbcTemplate.update("update list_friends set user_frends = 1 where id_user = ? and id_friend = ?;", friendId, userId);
            return;
        }
        jdbcTemplate.update("INSERT INTO list_friends (id_user, id_friend, USER_FRIENDS) VALUES (?, ?, 0);", userId, friendId);
        List<Integer> frendUser1 = getListFriend(userId);
        List<Integer> frendfrend1 = getListFriend(friendId);
        int i11 = 0;
        for (int i = 0; i < frendUser1.size(); i++) {
            if (friendId == frendUser1.get(i)) {
                i11++;
            }
        }
        for (int i = 0; i < frendfrend1.size(); i++) {
            if (userId == frendfrend1.get(i)) {
                i11++;
            }
        }
        if (i11 == 2) {
            jdbcTemplate.update("update list_friends set user_frends = 1 where id_user = ? and id_friend = ?;", userId, friendId);
            jdbcTemplate.update("update list_friends set user_frends = 1 where id_user = ? and id_friend = ?;", friendId, userId);
        }
    }

    public User postUser(User user) {
        Date date = Date.from(user.getBirthday().atStartOfDay(ZoneId.systemDefault()).toInstant());
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("INSERT INTO users (email, login, name, birthday)\n" +
                    "VALUES\n" +
                    "    (?, ?, ?, ?); ", new String[]{"id"});
            ps.setString(1,user.getEmail());
            ps.setString(2,user.getLogin());
            ps.setString(3,user.getName());
            ps.setDate(4, sqlDate);
            return ps;
        }, keyHolder);
        Integer keyUser = keyHolder.getKey().intValue();
        return getUser(keyUser);
    }

    public User putUser(User user) {
        User userPut = getUser(user.getId());
        jdbcTemplate.update("update users \n" +
                "set  email = ?, login = ?, name = ?, birthday = ?\n" +
                "where id =?;",  user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }
}
