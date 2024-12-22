package spring.boot.webcococo.repositories;

//import org.springframework.data.jpa.repository.JpaRepository;


import org.springframework.stereotype.Repository;
import spring.boot.webcococo.entities.Users;

import java.util.Optional;
@Repository
public interface UserRepository extends BaseRepository<Users, String> {
//    boolean existsByEmail(String email);


    //    Optional<Users> findByPhoneNumber(String phoneNumber);
//    Optional<Users> findByEmail(String email);
   Boolean existsByEmail(String email);



    Optional<Users> findById(String integer);

    Optional<Users> findByUsername(String username);

    Optional<Users> findByEmail(String email);
    Optional<Users> findByPhoneNumber(String phoneNumber);
}