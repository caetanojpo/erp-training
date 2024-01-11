package br.com.erptraining.service.product;

import br.com.erptraining.exception.ProductException;
import br.com.erptraining.repository.ProductRepository;
import br.com.erptraining.service.order.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteProductService {

    private final ProductRepository repository;
    private final FindProductService find;
    private final OrderItemService orderItemService;

    public void excludeById(UUID productId) {
        validateExclusionViability(productId);
        repository.deleteById(productId);
    }

    private void validateExclusionViability(UUID productId) {
        long numberOfOrders = orderItemService.isProductInOrder(productId);
        if (numberOfOrders > 0) {
            throw new ProductException("Can't exclude the Product because it is linked to the Order");
        }
    }
}
