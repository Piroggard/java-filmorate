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
        List<User> users = jdbcTemplate.query("select id from users u ;", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId( rs.getInt("id"));
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
                user.setBirthday(rs.getDate("birthday").toLocalDate());
                do{
                    listFriends.add(rs.getInt("id_friend"));
                } while (rs.next());
                user.setListFriends(listFriends);
                return user;
            }
        }, id);
    }

    public List<User> getFriendsUser (Integer idUser){
        List<User> userListFriend = jdbcTemplate.query("select id_friend as id \n" +
                "from list_friends lf where lf.id_user =? and lf.user_frends =1;", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId( rs.getInt("id"));
                return user;
            }
        },  idUser);

        List<User> listFriend = new ArrayList<>();
        for (User user : userListFriend) {
            listFriend.add(getUser(user.getId()));
        }
        return listFriend;

    }


    public List<User> getListMutualFriend (Integer userId, Integer otherId){
        List<User> userListFriend = jdbcTemplate.query("SELECT id_friend AS id\n" +
                "FROM list_friends\n" +
                "WHERE id_user = ? AND user_frends = 1 AND id_friend IN (\n" +
                "    SELECT id_friend\n" +
                "    FROM list_friends\n" +
                "    WHERE id_user = ? AND user_frends = 1\n" +
                ");", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId( rs.getInt("id"));
                return user;
            }
        },  userId , otherId);

        List<User> listFriend = new ArrayList<>();
        for (User user : userListFriend) {
            listFriend.add(getUser(user.getId()));
        }
        return listFriend;

    }

    public void deleteFriend (int userId, int friendId){
        jdbcTemplate.update("delete from list_friends where id_user =? and id_friend = ?;",userId, friendId );

    }

    public void addFriend (int userId, int friendId){
        jdbcTemplate.update(" INSERT INTO list_friends (id_user, id_friend, user_frends) VALUES (?, ?, 1);",userId, friendId );
    }

    @Override
    public User postUser(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday)\n" +
                "VALUES\n" +
                "    (?, ?, ?, ?); ", user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        return user;
    }


    public void deleteUser (int id ){
        jdbcTemplate.update("delete from list_friends where id_user =?;", id);
        jdbcTemplate.update("delete from users where id =?;", id);
    }

    @Override
    public User putUser(User user) {
        User userPut = getUser(user.getId());
        //int id = user.getId();
        //deleteUser(id);
        jdbcTemplate.update("update users \n" +
                "set  email = ?, login = ?, name = ?, birthday = ?\n" +
                "where id =?;",  user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }
}
