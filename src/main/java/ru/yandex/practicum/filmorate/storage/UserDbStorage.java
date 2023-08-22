package ru.yandex.practicum.filmorate.storage;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@AllArgsConstructor
public class UserDbStorage implements UserStorage{
    private final JdbcTemplate jdbcTemplate;
    @Override
    public List<User> getUsers() {
        String sql = "SELECT u.id, u.email, u.login, u.name, u.birthday, lf.id_friend FROM users u JOIN list_friends lf ON u.id = lf.id_user WHERE lf.user_frends = 1";
        List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(rs.getDate("birthday").toLocalDate());

                int friendId = rs.getInt("id_friend");
                if (!rs.wasNull()) {
                    Set<Integer> listFriends = user.getListFriends();
                    listFriends.add(friendId);
                    user.addListFriend(friendId);
                }

                return user;
            }
        });

        return users;
    }

    public User getUser(Integer id) {
        return jdbcTemplate.queryForObject("select u.id ,\n" +
                        "u.email ,\n" +
                        "u.login ,\n" +
                        "u.name,\n" +
                        "u.birthday ,\n" +
                        "lf.id_friend \n" +
                        "from users u join list_friends lf on u.id = lf.id_user where lf.id_user=? and lf.user_frends =1",
                new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                Set<Integer> listFriends = new HashSet<>();
                User user = new User();
                user.setId( rs.getInt("id"));
                user.setEmail(rs.getString("email"));
                user.setLogin(rs.getString("login"));
                user.setName(rs.getString("name"));
                user.setBirthday(formatter(rs.getString("birthday")));
                do{
                    listFriends.add(rs.getInt("id_friend"));
                } while (rs.next());
                user.setListFriends(listFriends);
                return user;
            }
        }, id);
    }



    public static LocalDate formatter (String s){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return  LocalDate.parse(s, formatter);
    }

    @Override
    public User postUser(User user) {
        return null;
    }

    @Override
    public User putUser(User user) {
        return null;
    }
}
