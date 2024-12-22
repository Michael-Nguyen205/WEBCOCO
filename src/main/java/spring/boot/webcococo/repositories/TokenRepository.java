package spring.boot.webcococo.repositories;

import spring.boot.webcococo.entities.Tokens;

import java.util.List;

public interface TokenRepository extends BaseRepository<Tokens, Integer> {

   List<Tokens> findByUserId(String userId);
//    List<Tokens> findByUser(Users user);
    Tokens findByToken(String token);
    Tokens findByRefreshToken(String token);
}

