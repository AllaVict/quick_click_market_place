package quick.click.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import quick.click.core.domain.model.Advert;
import quick.click.core.domain.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AdvertRepository extends JpaRepository<Advert, Long> {

    Optional<Advert> findAdvertById(Long id);

    Optional<Advert> findAdvertByIdAndUserId(Long advertId, Long userId);

    List<Advert> findAllByUserOrderByCreatedDateDesc(User user);


}
