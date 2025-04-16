package project.auth.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.auth.security.domain.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);
}
