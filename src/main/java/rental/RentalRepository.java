package rental;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import rental.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {


    Page<Rental> findAllByClient_Id(Long clientId, Pageable pageable);
}
