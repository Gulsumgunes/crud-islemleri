package com.example.crud_islemleri;

import static org.assertj.core.api.Assertions.assertThat;
import com.example.crud_islemleri.user.User;
import com.example.crud_islemleri.user.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired private UserRepository repo;

    @Test
    public void testFindByEmail() {
        String email = "ummugunes02@gmail.com"; // Test edeceğiniz email


        Optional<User> user = repo.findByEmail(email);

        if (user.isPresent()) {
            System.out.println("Kullanıcı bulundu: " + user.get().getFirstName() + " " + user.get().getLastName());
        } else {
            System.out.println("E-posta ile eşleşen kullanıcı bulunamadı!");
        }
    }

    @Test
    public void testAddNew() {
        String email = "ummugunes02@gmail.com";
        Optional<User> existingUser = repo.findByEmail(email);

        if (existingUser.isPresent()) {
            System.out.println("Kayıt zaten mevcut: " + email);
        } else {
            User user = new User();
            user.setEmail(email);
            user.setPassword("Ummu706");
            user.setFirstName("Gunes");
            user.setLastName("Smith");

            User savedUser = repo.save(user);
            Assertions.assertNotNull(savedUser);
            Assertions.assertTrue(savedUser.getId() > 0);
        }
    }


    @Test
    public void testListAll() {
        Iterable<User> users = repo.findAll();
        assertThat(users).isNotNull();

        long userCount = users.spliterator().getExactSizeIfKnown();
        assertThat(userCount).isGreaterThan(0);

        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void testUpdate() {
        Integer userId = 1;
        Optional<User> optionalUser = repo.findById(userId);

        assertThat(optionalUser).isPresent();

        User user = optionalUser.get();
        user.setPassword("Ummut");
        repo.save(user); // Şifreyi güncelle ve kaydet

        User updatedUser = repo.findById(userId).get();
        assertThat(updatedUser.getPassword()).isEqualTo("Ummut"); // Şifreyi kontrol et
    }

    @Test
    public void testGet() {
        Integer userId = 2;
        Optional<User> optionalUser = repo.findById(userId);

        assertThat(optionalUser).isPresent().withFailMessage("Kullanıcı ID: %d mevcut değil!", userId); // Hata mesajı

        System.out.println(optionalUser.get());
    }

    @Test
    public void testDelete() {
        Integer userId = 1;

        Optional<User> optionalUser = repo.findById(userId);

        assertThat(optionalUser).isPresent().withFailMessage("Silinecek kullanıcı ID: %d mevcut değil!", userId);

        repo.deleteById(userId); // Silme işlemi
        optionalUser = repo.findById(userId);

        assertThat(optionalUser).isNotPresent();
    }

    @BeforeEach
    public void cleanDatabase() {
        repo.deleteAll(); // Tüm kayıtları siler
    }

}

