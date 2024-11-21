package ma.yc.Citronix.sale.infrastructure.repository;

import ma.yc.Citronix.sale.domain.model.aggregate.Sale;
import ma.yc.Citronix.sale.domain.model.valueObject.SaleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, SaleId> {
}
